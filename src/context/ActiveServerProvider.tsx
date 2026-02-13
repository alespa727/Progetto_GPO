import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { Channel, Server } from "../types";
import { useSocket } from "./SocketProvider";

interface ActiveServerContextType {
  activeServer: Server | null;
  activeChannel: Channel | null;
  setActiveServer: (server: Server | null) => void;
  setActiveChannel: (channel: Channel | null) => void;
}

const ActiveServerContext = createContext<ActiveServerContextType | null>(null);

export const ActiveServerProvider = ({ children }: { children: ReactNode }) => {
  const [activeServer, setActiveServer] = useState<Server | null>(null);
  const [activeChannel, setActiveChannel] = useState<Channel | null>(null);
  const socket = useSocket();


  const changeServer = (server: Server | null): void=>{
    if(server === activeServer) return;
    if(activeServer){
      socket?.emit("leave_server", { serverId: activeServer.id});
      console.log("leave_server", { serverId: activeServer.id});
      setActiveChannel(null);
    }
      
    
    if(server!==null){
      socket?.emit("join_server", { serverId: server.id })
      console.log("join_server", { serverId: server.id });
    }

    setActiveServer(server);
    return; 
  }

  useEffect(()=>{
    if(activeServer)
      changeChannel(activeServer.sections[0].channels[0]);

  }, [activeServer]);

  const changeChannel = (channel: Channel | null): void=>{
    if(channel === activeChannel) return;
    if(activeChannel){
      socket?.emit("leave_channel", { channelId: activeChannel.id});
      console.log("leave_channel", { channelId: activeChannel.id});
      setActiveChannel(null);
    }
      
    
    if(channel!==null){
      socket?.emit("join_channel", { channelId: channel.id })
      console.log("join_channel", { channelId: channel.id });
    }

    setActiveChannel(channel)
    return; 
  }


  

  return (
    <ActiveServerContext.Provider value={{ activeServer, activeChannel, setActiveServer: changeServer, setActiveChannel: changeChannel  }}>
      {children}
    </ActiveServerContext.Provider>
  );
};

export const useActiveServerContext = () => {
  const context = useContext(ActiveServerContext);
  if (!context) throw new Error("useActiveServerContext deve essere usato dentro ActiveServerProvider");
  return context;
};
