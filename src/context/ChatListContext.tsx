import { Chat, ClientHttp, endpoint2 } from "@/types";
import axios from "axios";
import { createContext, useContext, useEffect, useState, ReactNode, useMemo, SetStateAction, Dispatch } from "react";
import { useAccount } from "./UserProvider";

type ChatContextProps = {
  chats: Chat[],
  setChats: Dispatch<SetStateAction<Chat[]>>;
  forceUpdate: () => void
}

const ChatContext = createContext<ChatContextProps | null>(null);

export const ChatProvider = ({ children }: { children: ReactNode }) => {
  const [chats, setChats] = useState<Chat[]>([]);
  const [update, setUpdate] = useState<boolean>(false);
  const account = useAccount();

  const forceUpdate = () => {
    setUpdate(!update);
  }

  useEffect(() => {
    const fetchChats = async () => {
      try {
        if (!account) return;
        const chats = await ClientHttp.fetchChats()

        setChats(chats);
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchChats();
  }, [account, update]);

  return (
    <ChatContext.Provider value={{ chats, setChats, forceUpdate }}>
      {children}
    </ChatContext.Provider>
  );
};

export const useChats = () => {
  return useContext(ChatContext);
};
