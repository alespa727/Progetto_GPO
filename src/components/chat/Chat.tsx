import { useState, useEffect, useRef } from "react";
import "../../styles/Chat.css";
import { ClientHttp, Message } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useChatContext } from "../../context/ChatContext.tsx";
import { Header } from "./ChatHeader.tsx";
import { ListaMessaggi } from "./ListaMessaggi.tsx";
import { MessageInput } from "./MessageInput.tsx";
import { useAccount } from "@/context/UserProvider.tsx";
import ChatCall from "../chiamata/ChatCall.tsx";

function Chat() {
  const socket = useSocket();
  const messagesRef = useRef<Message[]>([]);
  const [, forceUpdate] = useState(0);
  const [fetched, setFetched] = useState(false);
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
    window.location.hash = "/chat/" + chat.friend.username;
    joinChat(chat.id, chat.friend.username);

    messagesRef.current = [];
    forceUpdate(prev => prev + 1);

    setFetched(false);

    return () => { socket.emit("leave_chat", { chatId }); };
  }, [chat, socket]);

  useEffect(() => {
    if (!socket) return;

    const handleNewMessage = (data: any) => {
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
      forceUpdate(prev => prev + 1);
    }

    const handleModifiedMessage = (data: any) => {
      
      modifyMessageById(data.messageId, data.text);
      forceUpdate(prev => prev + 1);
    }

    const removeMessageById = (id: number) => {
      messagesRef.current = messagesRef.current.filter(msg => msg.messageId !== id);
    }
    const modifyMessageById = (id: number, text: string) => {
      let message = messagesRef.current.find(msg =>
        msg.messageId === id
      );

      if(message) {
        message.message = text;
        forceUpdate(prev => prev + 1);
      }
    }
    socket.on("deletedMessage", handleDeletedMessage);
    socket.on("modifiedMessage", handleModifiedMessage);


    return () => {
      socket.off("newMessage", handleNewMessage);
      socket.off("deletedMessage", handleDeletedMessage);
    };
  }, [socket]);

  const fetchMessages = async () => {

    if (!chat) return

    try {
      const newMessages = await ClientHttp.getMessages(chat.id);
      newMessages.forEach(m => m.sent = true)
      messagesRef.current = newMessages;
      setFetched(true);
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
          <ListaMessaggi messages={messagesRef} skeleton={!fetched} empty={messages.length === 0} />
          <MessageInput forceUpdate={forceUpdate} addMessage={addMessage}></MessageInput>

        </div>
      </>

    );
  };

  const joinChat = (chatId: number, otherUser: string) => {
    if (!socket) return;
    setHeader(otherUser);
    fetchMessages();
    socket.emit("join_chat", { chatId });
  }

  return render();
}


export default Chat