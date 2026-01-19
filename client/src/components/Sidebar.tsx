import { useState } from "react";
import "../styles/Sidebar.css";
import { Server } from "../types";
import { useServers } from "../context/ServerListContext";
import { ClientMode, useMode } from "../context/ModeProvider";
import { useActiveServerContext } from "../context/ActiveServerProvider";
import { ServerLoop, ServerLoopProps } from "./server/ServerLoop";
import { SidebarServerName } from "./server/SidebarServer";
import { ServerPicture } from "./server/ServerPicture";
function Sidebar() {

  const setClientMode = useMode().setMode;
  const servers: Server[] | null = useServers();
  const setActiveServer = useActiveServerContext().setActiveServer;
  const circleStyle = "w-[65%] rounded-full aspect-square bg-white/10 self-center ";

  if(servers===null) return;

  return (
    <div className="w-16 flex content-center flex-col">
      <div className={circleStyle.concat("mb-1")} onClick={()=>{setActiveServer(null); setClientMode(ClientMode.Chats)}}></div>
      <div className="m-3 border-white/20 border"/>
      <ServerLoop servers={servers}>
          <ServerPicture alt="" style={circleStyle.concat("mt-2")}></ServerPicture>
      </ServerLoop>
    </div>
  );
}

export default Sidebar;
 