import { createContext, useContext, useState, ReactNode } from "react";
import { PrivateChatResponse } from "../types";

interface ActiveChatContextType {
  activeChat: PrivateChatResponse | null;
  setActiveChat: (Chat: PrivateChatResponse | null) => void;
}

const ActiveChatContext = createContext<ActiveChatContextType | null>(null);

export const ActiveChatProvider = ({ children }: { children: ReactNode }) => {
  const [activeChat, setActiveChat] = useState<PrivateChatResponse | null>(null);

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
