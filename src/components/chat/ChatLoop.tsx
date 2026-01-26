import React from "react";
import { ChatContext } from "../../context/ChatContext";
import { Chat } from "@/types";

export interface ChatLoopProps {
  /** The chats to loop over. Use `useChats()` hook to get participants. */
  chats: Chat[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ChatLoop({ chats, ...props }: ChatLoopProps) {
  return (
    <div className={"lista"}>
      {chats.map((c) => (
        <ChatContext value={c} key={c.id}>
            {
                props.children
            }
        </ChatContext>
      ))}
    </div>
  );
}
