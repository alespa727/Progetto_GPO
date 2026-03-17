import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { Channel, ChannelType, ClientHttp, endpoint, endpoint2, Section, Server } from "../types";
import { useAccount } from "./UserProvider";

type ServersContextProps={
  servers: Server[] | null,
  forceUpdate: () => void
}

const ServerContext = createContext<ServersContextProps | null>(null);

export const ServerProvider = ({ children }: { children: ReactNode }) => {
  const [servers, setServers] = useState<Server[] | null>(null);
  const [update, setUpdate] = useState<boolean>(false);
  const account = useAccount();
  
  const forceUpdate = ()=>{
    setUpdate(!update);
  }


  useEffect(() => {
    const fetchServers = async () => {
    
      try {
        if(!account) return;
        let list: Server[] = [];

        let servers = await ClientHttp.getCommunities();
     
        for (let i = 0; i < servers.length; i++) {
          const element = servers[i]
          list.push(Server.fromJSON(element))
        }
      
        setServers(list);

      } catch (err) {
        console.error("Errore nel fetch dei server:", err);
      }
    };

    fetchServers();
  }, [account, update]);

  return (
    <ServerContext.Provider value={{servers, forceUpdate}}>
      {children}
    </ServerContext.Provider>
  );
};

export const useServers = () => {
  return useContext(ServerContext)?.servers;
};

export const updateServers = () => {
  const ctx = useContext(ServerContext);

  return ctx?.forceUpdate ?? (() => {});
};
