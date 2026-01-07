import Chats from "./Chats.tsx";
import Chat from "./Chat.tsx";
import Canali from "./Canali.tsx"
import "../styles/Content.css";
import { Channel, PrivateChatResponse, Server } from "../types.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatProvider.tsx";
import { useActiveServerContext } from "../context/ActiveServerProvider.tsx";

function Content() {

  const chats = useChats();
  const mode = useMode().mode;

  switch(mode){
    case ClientMode.Server:
      return (
        <div className="content">
          <div className="server">
            <Canali></Canali>
            <Chat></Chat>
          </div>
        </div>
      );
    case ClientMode.Chats:
      console.log(chats);
      if(!chats) return;
      return (
        <div className="content">  
          <div className="server">
            <Chats></Chats>
            <Chat></Chat>
          </div>
        </div>
      );
  }

}




export default Content;
