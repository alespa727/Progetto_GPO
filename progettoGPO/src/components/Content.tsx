import { useState } from "react";
import Chats from "./Chats.tsx";
import Chat from "./Chat.tsx";
import Canali from "./Canali.tsx";
import "../styles/Content.css";

function Content({mode="chats"}: {mode: string}) {
  const [chats, setChats] = useState(["ale.", "Tommy"]);
  const [activeChat, setActiveChat] = useState<string>("");

  if(mode==="servers"){
    return (
        <div className="content">
            <div className="server">
               <Canali server="server di ale."></Canali>
            </div>
        </div>
      );
  }else if(mode==="chats"){
    return (
        <div className="content">
            <div className="server">
                <Chats chats={chats} onChatClick={(chat: string) => {setActiveChat(chat);}} ></Chats>
                <Chat chat={activeChat}></Chat>
            </div>
        </div>
      );
  }
  
}


export default Content;
