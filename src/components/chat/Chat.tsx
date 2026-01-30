import { useState, useEffect } from "react";
import "../../styles/Chat.css";
import { endpoint, Message } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useChatContext } from "../../context/ChatContext.tsx";
import { Header } from "./ChatHeader.tsx";
import { ListaMessaggi } from "../common/ListaMessaggi.tsx";
import { MessageInput } from "./MessageInput.tsx";
import ChatCall from "../chiamata/ChatCall.tsx";
import axios from "axios";

function Chat() {
  const socket = useSocket();

  const chat = useChatContext();
  const [messages, setMessages] = useState<Message[]>([]);
  const [header, setHeader] = useState<string>("");

  const addMessage = (message: Message)=>{
    setMessages((prevMessages) => [...prevMessages, message]);
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
      setMessages((prevMessages) => [...prevMessages, Message.fromJSON(data)]);
    };
    socket.on("newMessage", handleNewMessage);

    const handleDeletedMessage = (data: any) => {
      removeMessageById(data.messageId);
    }

    const removeMessageById = (id: number) => {
      setMessages(prevMessages => prevMessages.filter(msg => msg.messageId !== id));
    }
    socket.on("deletedMessage", handleDeletedMessage);

    return () => {
      socket.off("newMessage", handleNewMessage);
      socket.off("deletedMessage", handleDeletedMessage);
    };
  }, [socket]);

  const fetchMessages = async (id: number) => {
  
    if(!chat) return
    let endpointUrl = `${endpoint}/services/chats/${id}/messages`;
    console.log(endpointUrl)
    try {
      const res = await axios.get(endpointUrl,{
                    withCredentials: true
                });

      const newMessages = res.data.messages.map((msg: any) => Message.fromJSON(msg));
      setMessages(newMessages);
    } catch (err) {
      console.error("Errore fetch messages:", err);
    }
  };

  if (!chat) return <div className="chat-box"></div>;

  const render = () => {
    return (
      <>
        <div className="flex flex-col flex-1 min-h-0">

          <Header value={header}></Header>
          <ChatCall></ChatCall>
          <ListaMessaggi messages={messages} />
          <MessageInput addMessage={addMessage} lastMessage={messages.at(messages.length-1)}></MessageInput>

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