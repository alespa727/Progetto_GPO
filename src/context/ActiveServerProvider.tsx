import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { Channel, ChannelType, Section, Server } from "../types";
import { useSocket } from "./SocketProvider";
import { useActiveRoomContext } from "./RoomContext";
import { useRoomContext } from "@livekit/components-react";
import { RoomEvent } from "livekit-client";
import { useServers } from "./ServerListContext";

interface ActiveServerContextType {
  activeServer: Server | null;
  activeChannel: Channel | null;
  vc: Channel | null;
  setActiveServer: (server: Server | null) => void;
  setActiveChannel: (channel: Channel | null) => void;
  setActiveVC: (channel: Channel | null) => void;
}

const ActiveServerContext = createContext<ActiveServerContextType | null>(null);

export const ActiveServerProvider = ({ children }: { children: ReactNode }) => {
  const { setUrl, setToken, setTitle } = useActiveRoomContext()

  const url = "wss://progettogpo-dfna4rrr.livekit.cloud";

  const servers = useServers();
  const [activeServer, setActiveServer] = useState<Server | null>(null);
  const [activeChannel, setActiveChannel] = useState<Channel | null>(null);
  const [vc, setActiveVC] = useState<Channel | null>(null);
  const room = useRoomContext();
  const socket = useSocket();


  useEffect(()=>{
    console.log("cambio")
    if(activeServer)
      servers?.forEach((s)=>{
        if(activeServer.id===s.id) setActiveServer(s)
      })
  }, [servers])

  useEffect(() => {
    if (!socket) return;

    const handleVoiceStateUpdate = (voiceState: Record<number, any[]>) => {

      setActiveServer(prev => {
        if (!prev) return prev;

        const updatedSections = prev.sections.map(section => {

          const updatedChannels = section.channels.map(channel => {

            if (channel.type !== ChannelType.VOICE) {
              return channel;
            }

            const updatedChannel = new Channel(
              channel.id,
              channel.sectionId,
              channel.communityId,
              channel.type,
              channel.name,
              channel.description,
              channel.createdAt
            );

            updatedChannel.users = [...(voiceState[channel.id] ?? [])];

            console.log(
              "update channel:",
              channel.name,
              updatedChannel.users
            );

            return updatedChannel;
          });

          return new Section(
            section.id,
            section.name,
            updatedChannels
          );
        });

        return new Server(
          prev.id,
          prev.name,
          updatedSections,
          prev.description,
          prev.owner,
          prev.createdAt,
          prev.inviteCode
        );
      });
    };

    socket.on("voice_state_update", handleVoiceStateUpdate);

    return () => {
      socket.off("voice_state_update", handleVoiceStateUpdate);
    };
  }, [socket]);


  const changeServer = (server: Server | null): void => {
    if (server === activeServer) return;
    if (activeServer) {
      socket?.emit("leave_server", { serverId: activeServer.id });
      console.log("leave_server", { serverId: activeServer.id });
      setActiveChannel(null);
    }

    if (server !== null) {
      socket?.emit("join_server", { serverId: server.id })
      console.log("join_server", { serverId: server.id });
    }

    setActiveServer(server);
    return;
  }

  const changeChannel = (channel: Channel | null): void => {
    if (channel === activeChannel) return;
    if (activeChannel) {
      socket?.emit("leave_channel", { channelId: activeChannel.id });
      console.log("leave_channel", { channelId: activeChannel.id });
      setActiveChannel(null);
    }


    if (channel !== null) {
      socket?.emit("join_channel", { channelId: channel.id })
      console.log("join_channel", { channelId: channel.id });
    }

    setActiveChannel(channel)
    return;
  }

  const changeVoiceChannel = async(channel: Channel | null): Promise<void> => {

    if (!activeServer) return;

    await room.disconnect();

    if (channel === null) {
      if (vc) {
        socket?.emit("leave_vc", {
          channelId: vc.id,
          serverId: activeServer.id
        });
        console.log("leave_vc", {
          channelId: vc.id,
          serverId: activeServer.id
        });
        setActiveVC(null);
      }
      return;
    }

    if (vc?.id === channel.id) return;

    if (vc) {
      socket?.emit("leave_vc", {
        channelId: vc.id,
        serverId: activeServer.id
      });
      console.log("leave_vc", {
        channelId: vc.id,
        serverId: activeServer.id
      });
    }

    socket?.emit("join_vc", {
      channelId: channel.id,
      serverId: activeServer.id
    });

    console.log("join_vc", {
      channelId: channel.id,
      serverId: activeServer.id
    });

    const handleToken = (data: any) => {
      setUrl(url);
      setToken(data.token);
      setTitle("Channel-" + channel.id);
      socket?.off("tokenVC", handleToken)
    }
    socket?.on("tokenVC", handleToken)


    setActiveVC(channel);
  };


  useEffect(() => {
    if (!socket || !room) return;

    const leaveVC = () => {

      if (!activeServer || !vc) return;

      socket.emit("leave_vc", {
        channelId: vc.id,
        serverId: activeServer.id
      });
      
      setActiveVC(null);

      console.log("leave_vc (disconnect)", {
        channelId: vc.id,
        serverId: activeServer.id
      });
    };

    room.on(RoomEvent.Disconnected, leaveVC);

    return () => {
      room.off(RoomEvent.Disconnected, leaveVC);
    };

  }, [room, socket, activeServer, vc]);



  return (
    <ActiveServerContext.Provider value={{ activeServer, activeChannel, vc, setActiveServer: changeServer, setActiveChannel: changeChannel, setActiveVC: changeVoiceChannel }}>
      {children}
    </ActiveServerContext.Provider>
  );
};

export const useActiveServerContext = () => {
  const context = useContext(ActiveServerContext);
  if (!context) throw new Error("useActiveServerContext deve essere usato dentro ActiveServerProvider");
  return context;
};
