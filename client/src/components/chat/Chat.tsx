import { useState, useRef, useEffect, act } from "react";
import "../../styles/Chat.css";
import axios from "axios";
import { Message, ChannelType, PrivateChatResponse } from "../../types.tsx";
import Chiamata from "../Chiamata.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useActiveChatContext } from "../../context/ActiveChatProvider.tsx";
import { useActiveServerContext } from "../../context/ActiveServerProvider.tsx";
import { useUser } from "../../context/UserProvider.tsx";
import { Room } from "livekit-client";
import { RoomContext } from "@livekit/components-react";
import { useChatContext } from "../../context/ChatContext.tsx";


function Chat() {

  const user = useUser();
  const socket = useSocket();
  const [nuovoTesto, setNuovoTesto] = useState<string>("");
  const messagesRef = useRef<null | HTMLDivElement>(null)
  const activeChat = useChatContext();
  const [messages, setMessages] = useState<Message[]>([]);
  const [header, setHeader] = useState<string>("");
  const [isCallActive, setCallActive] = useState<boolean>(false);

  const scrollToBottom = () => {
    if (messagesRef.current) {
      messagesRef.current.scrollTo({
        top: messagesRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  };

  const aggiungiMessaggio = () => {
    if(!socket) return;
    if (!nuovoTesto) return;
    if (nuovoTesto.trim() === "") return;
    if (!user) return;
    let id = activeChat ? activeChat.chatId : null;
    let message = new Message(nuovoTesto, user);
    let type = "chat";
    socket.emit("sendMessage", { id, type, message });

    setNuovoTesto("");
  };

  useEffect(() => {
    if (!socket) return;
    const handleNewMessage = (data: any) => {
      setMessages((prevMessages) => [...prevMessages, data]);
    };
    socket.on("newMessage", handleNewMessage);

    return () => {
      socket.off("newMessage", handleNewMessage);
    };
  }, [socket]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const fetchMessages = async (id: number) => {
    let endpoint;

    endpoint = `http://localhost:4000/chat/${id}/messages`;

    try {
      const res = await axios.get(endpoint);
      if (res.data.message) return;
      const newMessages = res.data.map((msg: any) => Message.fromJSON(msg));
      setMessages(newMessages);

      setTimeout(() => {
        scrollToBottom();
      }, 50);
    } catch (err) {
      console.error("Errore fetch messages:", err);
    }
  };

  const render = () => {
    return (
      <div className="chat-box">
        <div className="chat-header"> {header} </div>

        <div className="messages" ref={messagesRef}>
          {[...messages].reverse().map((msg: Message, index: number) => (
            <div key={index} className="message">
              <div key={index} className="text"><b>{msg.sender.username}</b> {msg.text}</div>
            </div>
          ))}
        </div>

        <div className="absolute bottom-0 left-0 w-full box-border p-1">
          <input
            value={nuovoTesto || ""}
            onChange={(e) => setNuovoTesto(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                aggiungiMessaggio();
              }
            }}
            placeholder="Scrivi qui..."
          />
        </div>
      </div>
    );
  };

  const joinChat = (chatId: number, otherUser: string) => {
     if(!socket) return;
    setHeader("Chat con " + otherUser);
    fetchMessages(chatId);
    socket.emit("join_chat", { chatId });
  }

  useEffect(() => {
    if (!activeChat) return;
    if (!socket) return;
    const chatId = activeChat.chatId;
    joinChat(activeChat.chatId, activeChat.otherUser.username);

    return () => { socket.emit("leave_chat", { chatId }); };
  }, [activeChat, socket]);


  if (!activeChat) return <div className="chat-box"></div>;

  return render();
}


export default Chat