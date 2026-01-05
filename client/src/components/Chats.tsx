import "../styles/Chats.css";
import { chat_type, chat } from "../types.tsx";

function Chats({chats, onChatClick}: {chats: chat[], onChatClick: (chat: chat)=>void}) {
  return (
    <div className="chats">
        <div className="lista">
            {chats.map((chat, index)=>{
              return <div className="chat" onClick={() => onChatClick(chat)}><div className="pfp"/>{chat.name}</div>
            })}
        </div>
    </div>
  );
}

export default Chats;
