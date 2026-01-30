import { useState, useRef, useEffect } from "react";
import "../../styles/Chat.css";
import axios from "axios";
import { Message, ChannelType } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useActiveServerContext } from "../../context/ActiveServerProvider.tsx";
import { Plus } from "lucide-react";
import { Messaggio } from "../common/Messaggio.tsx";
import { useAccount } from "@/context/UserProvider.tsx";

function ChannelChat() {
  const user = useAccount();
  const socket = useSocket();
  const [nuovoTesto, setNuovoTesto] = useState<string>("");
  const messagesRef = useRef<null | HTMLDivElement>(null);
  const activeServer = useActiveServerContext().activeServer;
  const activeChannel = useActiveServerContext().activeChannel;
  const setActiveChannel = useActiveServerContext().setActiveChannel;
  const [messages, setMessages] = useState<Message[]>([]);
  const [header, setHeader] = useState<string>("");
  const [isCallActive, setCallActive] = useState<boolean>(false);
  const [selectedMsgIndex, setSelectedMsgIndex] = useState<number | null>(null);

  const scrollToBottom = () => {
    if (messagesRef.current) {
      messagesRef.current.scrollTo({
        top: messagesRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  };

  const aggiungiMessaggio = () => {
    if (!socket) return;
    if (!nuovoTesto) return;
    if (nuovoTesto.trim() === "") return;
    if (!user) return;
    let id = activeChannel ? activeChannel.id : null;
    let message = new Message(-1, nuovoTesto, user.username, new Date());
    let type = "channel";
    socket.emit("sendMessage", { id, type, message });

    setNuovoTesto("");
  };

  const removeMessageById = (id: number) => {
    setMessages(prevMessages => prevMessages.filter(msg => msg.messageId !== id));
  }

  useEffect(() => {
    setCallActive(activeChannel?.type === ChannelType.VOICE);
  }, [activeChannel]);

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

    endpoint = `http://localhost:4000/channel/${id}/messages`;

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
    if (!activeChannel) return;
    if (!socket) return;

    const channelId = activeChannel.id;
    console.log("emit join_channel", channelId);
    socket.emit("join_channel", { channelId });
    setHeader("# " + activeChannel.title);
    fetchMessages(activeChannel.id);
    console.log(messages);

    return () => {
      socket.emit("leave_channel", { channelId });
    };
  }, [activeChannel, socket]);

  useEffect(() => {
    if (!activeServer) return;
    setActiveChannel(activeServer.sections[0].channels[0]);
    return () => setActiveChannel(null);
  }, []);



  if(!activeChannel) return;


  const render = () => {
    return (
      <>

        <div
          className="flex relative w-full flex-col min-h-0">

          <div className="border-white/10 border-b w-full pl-3 p-4 text-center">
            {header}
          </div>
        
          <div
            className="flex-1  mb-3 overflow-y-auto flex flex-col justify-end"
            ref={messagesRef}
          >
            {[...messages].map((msg: Message, index: number) => (
              <Messaggio messageType={"channel"} id={activeChannel.id} msg={msg} key={index} style={selectedMsgIndex === index ? "bg-white/10" : ""} onCloseMenu={() => { setSelectedMsgIndex(null) }} onTrigger={() => { console.log(index); setSelectedMsgIndex(index) }}></Messaggio>
            ))}
          </div>

          <div className="mt-1/2 p-1.5">
            <div className="flex items-center gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
              <Plus className="hover:bg-white/10 rounded-md transition-all duration-300 p-1/2 w-6 h-6 text-white" />
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

  return render();
}

export default ChannelChat;
