import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { endpoint, endpoint2, Server } from "../types";

const ServerContext = createContext<Server[] | null>(null);

export const ServerProvider = ({ children }: { children: ReactNode }) => {
  const [servers, setServers] = useState<Server[] | null>(null);

  useEffect(() => {
    const fetchServers = async () => {
      try {
        
        let list: Server[] = [];
        const res = await axios.get(endpoint2+"/servers")
        console.log(res)
        res.data.forEach((element: any) => {
            list.push(Server.fromJSON(element));
        }); 
        setServers(list);
        
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchServers();
  }, []);

  return (
    <ServerContext.Provider value={servers}>
      {children}
    </ServerContext.Provider>
  );
};

export const useServers = () => {
  return useContext(ServerContext);
};
