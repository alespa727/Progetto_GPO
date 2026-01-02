import { useState } from "react";
import "../styles/Chats.css";

function Chats({chats, onChatClick}: {chats: string[], onChatClick: (chat: string)=>void}) {
 
  return (
    <div className="chats">
        <div className="lista">
            {chats.map((chat, index)=>{
              return <div className="chat" onClick={() => onChatClick(chat)}>{chat}</div>
            })}
        </div>
    </div>
  );
}

export default Chats;
