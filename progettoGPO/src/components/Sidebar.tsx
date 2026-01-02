import { useState } from "react";
import "../styles/Sidebar.css";

function Sidebar({servers, setMode}: {servers: string[], setMode: (mode: string)=>void}) {

  return (
    <div className={`sidebar`}>
      <div className="circle logo" onClick={()=>{setMode("chats")}}></div>
      <div className="break-line"/>
      {
        servers.map((server, index)=>{
          return <div className="circle" onClick={()=>{setMode("servers")}}></div>;
        })
      }

    </div>
  );
}

export default Sidebar;
