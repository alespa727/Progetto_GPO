import React from "react";
import { ChatContext } from "../../context/ChatContext";
import { Chat } from "@/types";

export interface ChatLoopProps {
  /** The chats to loop over. Use `useChats()` hook to get participants. */
  chats: Chat[];
  filter?: string;
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ChatLoop({ chats, filter, ...props }: ChatLoopProps) {
  return (
    <div className={"p-2.5 mt-1.5 pt-0"}>
      {chats.map((c) => {
        if(filter){
          if(c.friend.username.includes(filter)){
            return (
              <ChatContext value={c} key={c.friend.username}>
                  {
                      props.children
                  }
              </ChatContext>
            )
          }
          return;
        }
        return (
        <ChatContext value={c} key={c.friend.username}>
            {
                props.children
            }
        </ChatContext>)
      })}
    </div>
  );
}
