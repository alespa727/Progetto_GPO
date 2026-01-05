import { useState, useRef, useEffect } from "react";
import "../styles/Chat.css";
import { chat_type, chat, channel } from "../types.tsx";
import Chiamata from "./Chiamata.tsx";

function Chat({chat, type}: {chat: chat, type: string}) {
  if (!chat) return <div className="chat-box"></div>; 
  const aggiungiMessaggio = () => {
    if (nuovoTesto.trim() === "") return; 
    
    setMessages([...messages, nuovoTesto]);

    setNuovoTesto("");
    setTimeout(() => {
      if (messagesRef.current) {
        messagesRef.current.scrollTo({
          top: messagesRef.current.scrollHeight,
          behavior: "smooth",
        });
      }
    }, 10);
  };

  const [nuovoTesto, setNuovoTesto] = useState("");
  var arr: string[] = getChat(chat, type);
  const [messages, setMessages] = useState<string[]>(arr);
  const messagesRef = useRef<null | HTMLDivElement>(null)

  var header_text;
  if(type==="server"){
    header_text="# ";
  }else{
    header_text="Chat con";
  }

  useEffect(() => {
    if (!chat) {
      setMessages([]);
      return;
    }

    const arr = getChat(chat, type);
    setMessages(arr);
  }, [chat]);

   return (
    <div className="chat-box">
        <div className="chat-header"> {header_text} {chat.name}</div>
        
        <Chiamata isActive={false}></Chiamata>

        <div className="messages" ref={messagesRef}>
          {[...messages].reverse().map((msg: string, index: number) => (
            <div key={index} className="message">
               <div className="text"><b>Utente</b> {msg}</div>
            </div>
          ))}
        </div>

        <div className="message-box">
         <input 
            value={nuovoTesto} 
            onChange={(e) => setNuovoTesto(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && aggiungiMessaggio()}
            placeholder="Scrivi qui..."
          />
        </div>
    </div>
  );
}

function getChat(chat: chat, type: string): string[]{
  return [];
}


export default Chat