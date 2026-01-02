import { useState, useRef, useEffect } from "react";
import "../styles/Chat.css";

function Chat({chat}: {chat: string}) {
  if (!chat) return <div className="chat-box"></div>; 
  const aggiungiMessaggio = () => {
    if (nuovoTesto.trim() === "") return; 
    
    setMessages([...messages, nuovoTesto]);

    setNuovoTesto("");
  };

  const [nuovoTesto, setNuovoTesto] = useState("");
  var arr: string[] = getChat(chat);
  const [messages, setMessages] = useState<string[]>(arr);

  useEffect(() => {
    if (!chat) {
      setMessages([]);
      return;
    }

    const arr = getChat(chat);
    setMessages(arr);
  }, [chat]);

   return (
    <div className="chat-box">
        <div className="chat-header"> Chat con {chat}</div>
       
        <div className="messages">
          {messages.map((msg: string, index: number) => (
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

function getChat(chat: string): string[]{
  return [];
}


export default Chat