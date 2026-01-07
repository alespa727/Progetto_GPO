import { useState } from "react";
import "../styles/Sidebar.css";
import { Server } from "../types";
import { useServers } from "../context/ServerContext";
import { ClientMode, useMode } from "../context/ModeProvider";
import { useActiveServerContext } from "../context/ActiveServerProvider";
function Sidebar() {

  const setClientMode = useMode().setMode;
  const servers: Server[] | null = useServers();
  const setActiveServer = useActiveServerContext().setActiveServer;

  if(servers===null) return;

  return (
    <div className={`sidebar`}>
      <div className="circle logo" onClick={()=>{setActiveServer(null); setClientMode(ClientMode.Chats)}}></div>
      <div className="break-line"/>
      {
        servers.map((server: Server, index)=>{
          return <div className="circle" key={server.id} onClick={()=>{setClientMode(ClientMode.Server); setActiveServer(server); console.log(server)}}></div>;
        })
      }
    </div>
  );
}

export default Sidebar;
