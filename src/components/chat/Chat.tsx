import { useState, useRef, useEffect } from "react";
import "../../styles/Chat.css";
import axios from "axios";
import { Message } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useUser } from "../../context/UserProvider.tsx";
import { useChatContext } from "../../context/ChatContext.tsx";
import { Plus } from "lucide-react";
import { Messaggio } from "../common/Messaggio.tsx";


function Chat() {
  const [selectedMsgIndex, setSelectedMsgIndex] = useState<number | null>(null);

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
    let message = new Message(-1, nuovoTesto, user);
    let type = "chat";
    socket.emit("sendMessage", { id, type, message });

    setNuovoTesto("");
  };

  useEffect(() => {
    if (!socket) return;
    const handleNewMessage = (data: any) => {
      setMessages((prevMessages) => [...prevMessages, Message.fromJSON(data)]);
    };
    socket.on("newMessage", handleNewMessage);

    const handleDeletedMessage = (data: any) => {
      console.log("messaggio eliminato", data)
      removeMessageById(data.messageId);
    }

    const removeMessageById = (id: number) => {
      setMessages(prevMessages => prevMessages.filter(msg => msg.id !== id));
    }
    socket.on("deletedMessage", handleDeletedMessage);

    return () => {
      socket.off("newMessage", handleNewMessage);
      socket.off("deletedMessage", handleDeletedMessage);
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

  useEffect(() => {
    if (!activeChat) return;
    if (!socket) return;
    const chatId = activeChat.chatId;
    joinChat(activeChat.chatId, activeChat.otherUser.username);

    return () => { socket.emit("leave_chat", { chatId }); };
  }, [activeChat, socket]);


  if (!activeChat) return <div className="chat-box"></div>;

  const render = () => {
    return (
      <>

        <div className="flex flex-col flex-1 min-h-0">

          <div className="border-white/10 border-b w-full pl-3 p-4 text-center">
            {header}
          </div>
        
          {/* MESSAGGI */}
          <div
            className="flex-1 mb-3 overflow-y-auto flex flex-col justify-end"
            ref={messagesRef}
          >
            {[...messages].map((msg: Message, index: number) => (
              <Messaggio messageType={"chat"} id={activeChat.chatId} msg={msg} key={index} style={selectedMsgIndex === index ? "bg-white/10" : ""} onCloseMenu={() => { setSelectedMsgIndex(null) }} onTrigger={() => { console.log(index); setSelectedMsgIndex(index) }}></Messaggio>
            ))}
          </div>

         {/* INPUT */}
          <div className="mt-1/2 p-1.5">
            <div className="flex items-center gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
              <Plus className="w-6 h-6 text-white" />
              <input
                value={nuovoTesto || ""}
                className="flex-1 bg-transparent text-[14px] focus:outline-none"
                onChange={(e) => setNuovoTesto(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") aggiungiMessaggio();
                }}
                placeholder="Scrivi qui..."
              />
            </div>
        

          </div>
         
        </div>
      </>

    );
  };

  const joinChat = (chatId: number, otherUser: string) => {
     if(!socket) return;
    setHeader("Chat con " + otherUser);
    fetchMessages(chatId);
    socket.emit("join_chat", { chatId });
  }


  return render();
}


export default Chat