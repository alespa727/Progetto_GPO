import { Chat, endpoint } from "@/types";
import axios from "axios";
import { createContext, useContext, useEffect, useState, ReactNode } from "react";

const ChatContext = createContext<Chat[]>([]);

export const ChatProvider = ({ children }: { children: ReactNode }) => {
  const [chats, setChats] = useState<Chat[]>([]);

  useEffect(() => {
    const fetchChats = async () => {
      try {
       
        const res = await axios.get(endpoint+"/services/chats", {
                    withCredentials: true
                });
        const chats : Chat[] = res.data.chats.map((c: any)=>Chat.fromJSON(c))
        console.log(chats)
        setChats(chats);
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchChats();
  }, []);

  return (
    <ChatContext.Provider value={chats}>
      {children}
    </ChatContext.Provider>
  );
};

export const useChats = () => {
  return useContext(ChatContext);
};
