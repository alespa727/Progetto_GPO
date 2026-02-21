import { useState, useEffect, useRef } from "react";
import "../../styles/Chat.css";
import { Message } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useChatContext } from "../../context/ChatContext.tsx";
import { Header } from "./ChatHeader.tsx";
import { ListaMessaggi } from "./ListaMessaggi.tsx";
import { MessageInput } from "./MessageInput.tsx";
import axios from "axios";
import { useAccount } from "@/context/UserProvider.tsx";
import ChatCall from "../chiamata/ChatCall.tsx";
import { data } from "framer-motion/client";

function Chat() {
  const socket = useSocket();
  const messagesRef = useRef<Message[]>([]);
  const [, forceUpdate] = useState(0);
  const chat = useChatContext();
  const account = useAccount();
  const messages = messagesRef.current;
  const [header, setHeader] = useState<string>("");

  function addMessage(msg: Message) {
    messagesRef.current.push(msg);
    forceUpdate(prev => prev + 1);
  }

  useEffect(() => {
    if (!chat) return;
    if (!socket) return;
    const chatId = chat.id;
    joinChat(chat.id, chat.friend.username);

    return () => { socket.emit("leave_chat", { chatId }); };
  }, [chat, socket]);

  useEffect(() => {
    if (!socket) return;
    const handleNewMessage = (data: any) => {
      console.log(data)
      if (!data) {
        console.warn("Received null message from socket", data);
        return;
      }
      const newMessage = Message.fromJSON(data);

      if (newMessage.username !== account?.username) {
        addMessage(newMessage)
      }
    };
    socket.on("newMessage", handleNewMessage);

    const handleDeletedMessage = (data: any) => {
      removeMessageById(data.messageId);
    }

    const removeMessageById = (id: number) => {
      messagesRef.current = messagesRef.current.filter(msg => msg.messageId !== id);
    }
    socket.on("deletedMessage", handleDeletedMessage);

    return () => {
      socket.off("newMessage", handleNewMessage);
      socket.off("deletedMessage", handleDeletedMessage);
    };
  }, [socket]);

  const fetchMessages = async (id: number) => {

    if (!chat) return
    let endpointUrl = `/api/services/chats/${id}/messages`;
   
    try {
      const res = await axios.get(endpointUrl, {
        withCredentials: true
      });

      const newMessages = res.data.messages.map((msg: any) => Message.fromJSON(msg));
      messagesRef.current = newMessages;
      forceUpdate(prev => prev + 1);
    } catch (err) {
      console.error("Errore fetch messages:", err);
    }
  };

  if (!chat) return <div className="hidden"></div>;

  const render = () => {
    return (
      <>
        <div className="flex flex-col flex-1 min-h-0">

          <Header value={header}></Header>
          <ChatCall></ChatCall>
          <ListaMessaggi messages={messages} />
          <MessageInput forceUpdate={forceUpdate} addMessage={addMessage}></MessageInput>

        </div>
      </>

    );
  };

  const joinChat = (chatId: number, otherUser: string) => {
    if (!socket) return;
    setHeader(otherUser);
    fetchMessages(chatId);
    socket.emit("join_chat", { chatId });
  }

  return render();
}


export default Chat