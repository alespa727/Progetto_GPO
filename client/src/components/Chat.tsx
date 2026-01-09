import { useState, useRef, useEffect, act } from "react";
import "../styles/Chat.css";
import axios from "axios";
import { PrivateChat, PrivateChatResponse, Channel, Message, User } from "../types.tsx";
import Chiamata from "./Chiamata.tsx";
import { useSocket } from "../context/SocketProvider.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatProvider.tsx";
import { useActiveChatContext } from "../context/ActiveChatProvider.tsx";
import { useActiveServerContext } from "../context/ActiveServerProvider.tsx";
import { useUser } from "../context/UserProvider.tsx";


function Chat() {

  const user = useUser();
  const socket = useSocket();
  const [nuovoTesto, setNuovoTesto] = useState<string>("");
  const messagesRef = useRef<null | HTMLDivElement>(null)
  const mode = useMode().mode;
  const activeChat = useActiveChatContext().activeChat;
  const activeServer = useActiveServerContext().activeServer;
  const activeChannel = useActiveServerContext().activeChannel;
  const setActiveChannel = useActiveServerContext().setActiveChannel;
  const [messages, setMessages] = useState<Message[]>([]);
  const [header, setHeader] = useState<string>("");


  useEffect(() => {
    if (!socket) return;

    const handleNewMessage = (data: any) => {
      console.log(data);
      setMessages((prevMessages) => [...prevMessages, data]);
    };

    socket.on("newMessage", handleNewMessage);

    return () => {
      socket.off("newMessage", handleNewMessage);
    };
  }, [socket]);

  useEffect(() => {
    if (messagesRef.current) {
      messagesRef.current.scrollTo({
        top: messagesRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  }, [messages]);

  const aggiungiMessaggio = () => {
    if (!nuovoTesto) return;
    if (nuovoTesto.trim() === "") return;
    if(!user) return;
    let id = activeChat ? activeChat.chatId : activeChannel ? activeChannel.id : null ;
    let message = new Message(nuovoTesto, user);
    let type = ClientMode.Chats === mode ? "chat" : "channel";
    socket.emit("sendMessage", { id, type, message});

    setNuovoTesto("");
  };

  const fetchMessages = async (id: number) => {
    let endpoint;
    switch (mode) {
      case ClientMode.Chats:
        endpoint = `http://localhost:4000/chat/${id}/messages`;
        break;

      case ClientMode.Server:
        endpoint = `http://localhost:4000/channel/${id}/messages`;
        break;
    }
    try {
      const res = await axios.get(endpoint);
      const newMessages = res.data.map((msg: any) => Message.fromJSON(msg));
      setMessages(newMessages);

      setTimeout(() => {
        messagesRef.current?.scrollTo({
          top: messagesRef.current.scrollHeight,
          behavior: "smooth",
        });
      }, 50);
    } catch (err) {
      console.error("Errore fetch messages:", err);
    }
  };

  const render = () => {
    return (
      <div className="chat-box">
        <div className="chat-header"> {header} </div>

        <Chiamata isActive={false}></Chiamata>

        <div className="messages" ref={messagesRef}>
          {[...messages].reverse().map((msg: Message, index: number) => (
            <div key={index} className="message">
              <div key={index} className="text"><b>{msg.sender.username}</b> {msg.text}</div>
            </div>
          ))}
        </div>

        <div className="message-box">
        <input
          value={nuovoTesto || ""} 
          onChange={(e) => setNuovoTesto( e.target.value )}
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

  const chatId = activeChat?.chatId;

  useEffect(() => {
    if (mode === ClientMode.Server) return;
    if (!activeChat) return;
    if (!socket) return;

    const chatId = activeChat.chatId;
    setHeader("Chat con " + activeChat.otherUser.username);
    fetchMessages(chatId);

    console.log("emit join_chat", chatId);
    socket.emit("join_chat", { chatId });

    return () => { socket.emit("leave_chat", { chatId }); };

  }, [activeChat, mode, socket]);

  useEffect(() => {
    if (mode === ClientMode.Chats) return;
    if (!activeChannel) return;
    if (!socket) return;

    const channelId = activeChannel.id;
    console.log("emit join_channel", channelId);
    socket.emit("join_channel", { channelId });

    return () => { socket.emit("leave_channel", { channelId }); };

  }, [activeChannel, mode, socket]);

  useEffect(() => {
    if (!activeChannel) return;
    setHeader("# " + activeChannel.title);
    fetchMessages(activeChannel.id);
  }, [activeChannel]);

  useEffect(() => {
    switch (mode) {
      case ClientMode.Chats:
        if (!activeChat) return;
        setActiveChannel(null);
        fetchMessages(activeChat.chatId);
        break;

      case ClientMode.Server:
        if (!activeServer) return;
        setActiveChannel(activeServer.sections[0].channels[0]);
        break;
    }
  }, [mode])

  switch (mode) {
    case ClientMode.Chats:

      if (!activeChat) return <div className="chat-box"></div>;

      break;

    case ClientMode.Server:

      break;
  }
  return render();
}


export default Chat