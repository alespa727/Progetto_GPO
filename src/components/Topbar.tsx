import { useEffect, useRef, useState } from "react";
import "../styles/Topbar.css";
import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useSocketStatus } from "@/context/SocketProvider";
import { useAccount } from "@/context/UserProvider";

function Topbar() {
  const topbar = useRef<HTMLDivElement>(null);
  const account = useAccount();
  const chatContext = useActiveChatContext();
  const [header, setHeader] = useState<string>("Discord")
  const socket = useSocketStatus();

  useEffect(()=>{
    if(chatContext.activeChat){
      setHeader(chatContext.activeChat.friend.username)
    }
  }, [chatContext.activeChat])
  
  if(!socket || !account) return;

  return (
    <div className="topbar q-electron-drag">
      <div className="drag-layer" ref={topbar}></div>
      <div className="title">{header}</div>
    </div>
  );
}

export default Topbar;
