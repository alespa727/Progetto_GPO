import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { Channel, Server } from "../types";

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

  useEffect(()=>{
    if(activeServer)
      setActiveChannel(activeServer.sections[0].channels[0]);
  }, [activeServer]);

  return (
    <ActiveServerContext.Provider value={{ activeServer, activeChannel, setActiveServer, setActiveChannel  }}>
      {children}
    </ActiveServerContext.Provider>
  );
};

export const useActiveServerContext = () => {
  const context = useContext(ActiveServerContext);
  if (!context) throw new Error("useActiveServerContext deve essere usato dentro ActiveServerProvider");
  return context;
};
