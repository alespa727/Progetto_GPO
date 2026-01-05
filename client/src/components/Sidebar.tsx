import { useState } from "react";
import "../styles/Sidebar.css";
import { server } from "../types";
function Sidebar({servers, setMode, setActiveServer}: {servers: server[], setMode: (mode: string)=>void,  setActiveServer: (server: server)=>void}) {

  return (
    <div className={`sidebar`}>
      <div className="circle logo" onClick={()=>{setMode("chats")}}></div>
      <div className="break-line"/>
      {
        servers.map((server: server, index)=>{
          return <div className="circle" onClick={()=>{setMode("servers"); setActiveServer(server)}}></div>;
        })
      }
    </div>
  );
}

export default Sidebar;
