import React from "react";
import { Server } from "../../types";
import { ServerContext } from "../../context/ServerContext";

export interface ServerLoopProps {
  servers: Server[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ServerLoop({ servers, ...props }: ServerLoopProps) {
  return (
    <>
    <div className="max-h-[80%] flex flex-col gap-2 mb-2 overflow-scroll [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]">
        {servers.map((s) => (
            <ServerContext value={s} key={s.id}>
                {props.children}
            </ServerContext>
        ))}
    </div>
</>
  );
}
