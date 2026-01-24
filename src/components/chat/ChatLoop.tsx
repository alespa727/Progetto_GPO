import React from "react";
import { PrivateChatResponse } from "../../types";
import { ChatContext } from "../../context/ChatContext";

export interface ChatLoopProps {
  /** The chats to loop over. Use `useChats()` hook to get participants. */
  chats: PrivateChatResponse[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ChatLoop({ chats, ...props }: ChatLoopProps) {
  return (
    <div className={"lista"}>
      {chats.map((c) => (
        <ChatContext value={c} key={c.chatId}>
            {
                props.children
            }
        </ChatContext>
      ))}
    </div>
  );
}
