import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { Room, RoomEvent, TrackType } from "livekit-client";
import { useSocket } from "./SocketProvider";
import { RoomAudioRenderer, RoomContext, useTracks } from "@livekit/components-react";
import { useActiveChatContext } from "./ActiveChatProvider";

interface ActiveRoomContextType {
  url: string;
  token: string;
  room: Room;
  title: string;
  setTitle: (title: string) => void;
  setUrl: (url: string) => void;
  setToken: (token: string) => void;
}

const ActiveRoomContext = createContext<ActiveRoomContextType | null>(null);

export const ActiveRoomProvider = ({ children }: { children: ReactNode }) => {
  const [url, setUrl] = useState<string>("");
  const [token, setToken] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [room] = useState<Room>(new Room());
  const socket = useSocket();
  useEffect(() => {
    if (!url || !token) return;
    if (token.length === 0) {
      if (room.state === "connected") {
        const channelId = room.name.replace(/^channel_/, "");
        socket?.emit("leave_channel", { channelId });
        room.disconnect();
        setTitle("");
      }
      return;
    }

    // Event listeners
    const handleEvents = () => {
      const onConnected = () => {
        console.log("Connesso alla stanza:", room.name);
        const result = room.name.replace(/^channel_/, "");
        //socket?.emit("join_channel", { channelId: result });
      
      };

      const onDisconnected = () => {
        console.log("Disconnesso da:", room.name);
        //socket?.emit("leave_call", {chatId: chat.activeChat?.id});
      };

      room.on(RoomEvent.Connected, onConnected);

      room.on(RoomEvent.Disconnected, onDisconnected);

      room.on(RoomEvent.ParticipantConnected, (p) => {
        console.log("Partecipante connesso:", p.identity);
      });

      room.on(RoomEvent.ParticipantDisconnected, (p) => {
        console.log("Partecipante disconnesso:", p.identity);
      });
      
      room.on(RoomEvent.TrackPublished, ()=>{

      });

      return ()=>{
          room.off(RoomEvent.Connected, onConnected);
          room.off(RoomEvent.Disconnected, onDisconnected);

      }
    };

    handleEvents();

    // Connect
    const connectRoom = async () => {
      try {
        console.log(url, token);
        await room.connect(url, token, { rtcConfig: { iceTransportPolicy: "all" } });
      } catch (err) {
        console.error("Errore connessione stanza:", err);
      }
    };

    connectRoom();

    // Cleanup
    return () => {
      const channelId = room.name.replace(/^channel_/, "");
      console.log("leave");
      console.log(socket);

      if (socket && socket.connected) {
        socket.emit("leave_channel", { channelId: channelId.toString() });
        console.log("Evento leave_channel inviato:", channelId);
      }
      if (room.state === "connected") {
        room.disconnect();
        console.log("Room disconnect eseguito");
      }

    };

  }, [url, token]);

  return (
    <ActiveRoomContext.Provider value={{ title, url, token, room, setTitle, setUrl, setToken }}>
      <RoomContext value={room}>
          {children}
        <RoomAudioRenderer></RoomAudioRenderer>
      </RoomContext>
    </ActiveRoomContext.Provider>
  );
};

export const useActiveRoomContext = () => {
  const ctx = useContext(ActiveRoomContext);
  if (!ctx)
    throw new Error("useActiveRoomContext deve essere usato dentro ActiveRoomProvider");
  return ctx;
};


export const useRoomStatus = () => {
  const ctx = useContext(ActiveRoomContext);
  if (!ctx)
    throw new Error("useActiveRoomContext deve essere usato dentro ActiveRoomProvider");
  return ctx.room.state==="connected";
};
