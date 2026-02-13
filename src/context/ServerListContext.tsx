import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { Channel, ChannelType, endpoint, endpoint2, Section, Server } from "../types";
import { useAccount } from "./UserProvider";

const ServerContext = createContext<Server[] | null>(null);

export const ServerProvider = ({ children }: { children: ReactNode }) => {
  const [servers, setServers] = useState<Server[] | null>(null);
  const account = useAccount();
  const example: Server[] = [
    new Server(
      1,
      "Example Server",
      [
        new Section(
          1,
          "General",
          [
            new Channel(
              1,
              ChannelType.VOICE,
              "General Voice",
              "Canale vocale principale"
            ),
            new Channel(
              2,
              ChannelType.TEXT,
              "general-chat",
              "Chat generale del server"
            )
          ]
        )
      ],
      "Server di esempio",
      new Date()
    )
  ];

  useEffect(() => {
    const fetchServers = async () => {
    
      try {
        if(!account) return;
        let list: Server[] = [];
        const res = await axios.get(endpoint2 + "/services/communities", {
          withCredentials: true
        });
        console.log("SERVER", res.data.communities[0])
        for (let i = 0; i < res.data.communities.length; i++) {
          const element = res.data.communities[i]
          console.log("server",Server.fromJSON(element));
          list.push(Server.fromJSON(element))
        }
      
        console.log(list)

        setServers(example);
        setServers(list);

      } catch (err) {
        console.error("Errore nel fetch dei server:", err);
      }
    };

    fetchServers();
  }, [account]);

  return (
    <ServerContext.Provider value={servers}>
      {children}
    </ServerContext.Provider>
  );
};

export const useServers = () => {
  return useContext(ServerContext);
};
