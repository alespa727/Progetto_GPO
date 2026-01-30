import { useEffect, useRef, useState } from "react";
import "../styles/Topbar.css";
import { useActiveChatContext } from "@/context/ActiveChatProvider";

function Topbar() {
  const topbar = useRef<HTMLDivElement>(null);
  const chatContext = useActiveChatContext();
  const [header, setHeader] = useState<string>("Discord")

  useEffect(()=>{
    if(chatContext.activeChat){
      setHeader(chatContext.activeChat.friend.username)
    }
  }, [chatContext.activeChat])
  

  return (
    <div className="topbar q-electron-drag">
      <div className="drag-layer" ref={topbar}></div>
      <div className="title">{header}</div>
    </div>
  );
}

export default Topbar;
