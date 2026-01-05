import Chats from "./Chats.tsx";
import Chat from "./Chat.tsx";
import Canali from "./Canali.tsx";
import "../styles/Content.css";
import { chat_type, chat, server } from "../types.tsx";

interface ContentProps {
  mode: string;
  server: server;
  chats: chat[];
  setChats: React.Dispatch<React.SetStateAction<chat[]>>;
  activeChat: chat;
  setActiveChat: React.Dispatch<React.SetStateAction<chat>>;
}

function Content({
  mode,
  server,
  chats,
  setChats,
  activeChat,
  setActiveChat
}: ContentProps) {

  if (mode === "servers") {
    return (
      <div className="content">
        <div className="server">
          <Canali server={server} onChannelClick={(chat: chat) => { setActiveChat(chat); }}></Canali>
          <Chat chat={activeChat} type="server"></Chat>
        </div>
      </div>
    );
  } else if (mode === "chats") {
 
    return (
      <div className="content">  
        <div className="server">
          <Chats chats={chats} onChatClick={(chat: chat) => { setActiveChat(chat); }} ></Chats>
          <Chat chat={activeChat} type="friend"></Chat>
        </div>
      </div>
    );
  }

}




export default Content;
