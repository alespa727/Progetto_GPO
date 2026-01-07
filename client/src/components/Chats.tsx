import { useActiveChatContext } from "../context/ActiveChatProvider.tsx";
import { useChats } from "../context/ChatProvider.tsx";
import "../styles/Chats.css";
import { PrivateChat, PrivateChatResponse } from "../types.tsx";

function Chats() {
  const chats = useChats();
  if(!chats) return;
  const setChat = useActiveChatContext().setActiveChat;
  return (
    <div className="chats">
        <div className="lista">
            {chats.map((chat, index)=>{
              return <div key={chat.chatId} className="chat" onClick={() => {setChat(chat); console.log("Selezionata: "); console.log(chat)}}><div className="pfp"/>{chat.otherUser.username}</div>
            })}
        </div>
    </div>
  );
}

export default Chats;
