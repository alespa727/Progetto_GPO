import React from "react";
import { Server } from "../../types";
import { ServerContext } from "../../context/ServerContext";

export interface ServerLoopProps {
  /** The chats to loop over. Use `useChats()` hook to get participants. */
  servers: Server[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ServerLoop({ servers, ...props }: ServerLoopProps) {
  return (
    <>
    <div className="flex flex-col gap-2 mb-2">
      {servers.map((s) => (
        <ServerContext value={s} key={s.id}>
          {
            props.children
          }
        </ServerContext>
      ))}
    </div>
      
    </>
  );
}
