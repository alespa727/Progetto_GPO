import { createContext, useContext, useState, ReactNode } from "react";
import { Chat } from "../types";

interface ActiveChatContextType {
  activeChat: Chat | null;
  setActiveChat: (Chat: Chat | null) => void;
}

const ActiveChatContext = createContext<ActiveChatContextType | null>(null);

export const ActiveChatProvider = ({ children }: { children: ReactNode }) => {
  const [activeChat, setActiveChat] = useState<Chat | null>(null);

  return (
    <ActiveChatContext.Provider value={{ activeChat, setActiveChat }}>
      {children}
    </ActiveChatContext.Provider>
  );
};

export const useActiveChatContext = () => {
  const context = useContext(ActiveChatContext);
  if (!context) throw new Error("useActiveChatContext deve essere usato dentro ActiveChatProvider");
  return context;
};
