import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { PrivateChatResponse, User } from "../types";

const ChatContext = createContext<PrivateChatResponse[] | null>(null);

export const ChatProvider = ({ children }: { children: ReactNode }) => {
  const [Chats, setChats] = useState<PrivateChatResponse[] | null>(null);

  useEffect(() => {
    const fetchChats = async () => {
      try {
        let list: PrivateChatResponse[] = [];
        const res = await axios.get("http://localhost:4000/chats")
        res.data.forEach((element: any) => {
            list.push(PrivateChatResponse.fromJSON(element));
        }); 
        console.log(list)
        setChats(list);
      
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchChats();
  }, []);

  return (
    <ChatContext.Provider value={Chats}>
      {children}
    </ChatContext.Provider>
  );
};

export const useChats = () => {
  return useContext(ChatContext);
};
