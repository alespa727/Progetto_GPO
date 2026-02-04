import { useState, useEffect, useRef } from "react";
import "../../styles/Chat.css";
import { Channel, endpoint2, Message } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useChatContext } from "../../context/ChatContext.tsx";
import { ListaMessaggi } from "../common/ListaMessaggi.tsx";
import ChatCall from "../chiamata/ChatCall.tsx";
import axios from "axios";
import { useAccount } from "@/context/UserProvider.tsx";
import { useServerContext } from "@/context/ServerContext.tsx";
import { Header } from "../chat/ChatHeader.tsx";
import { MessageInput } from "../chat/MessageInput.tsx";
import { useActiveServerContext } from "@/context/ActiveServerProvider.tsx";
import { head } from "motion/react-client";
import { Plus } from "lucide-react";

function ChannelChat() {
  const socket = useSocket();
  const messagesRef = useRef<Message[]>([]);
  const [, forceUpdate] = useState(0);
  const serverContext = useActiveServerContext();

  const account = useAccount();
  const messages = messagesRef.current;
  const [header, setHeader] = useState<string>("");
  const [channel, setChannel] = useState<Channel | null>(null);
  const [nuovoTesto, setNuovoTesto] = useState<string>("");
  useEffect(() => {
    setChannel(serverContext.activeChannel)
    console.log("Canale attivo", serverContext.activeChannel)
    joinChannel();
  }, [serverContext.activeChannel])

  function addMessage(msg: Message) {
    messagesRef.current.push(msg);
    forceUpdate(prev => prev + 1);
  }
  /*
  useEffect(() => {
    if (!channel) return;
    if (!socket) return;
    const id = channel.id;
    joinServer(channel?.id);

    return () => { socket.emit("leave_server", { id }); };
  }, [channel, socket]);*/

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
    /*
   if (!channel) return
  
   let endpointUrl = `${endpoint2}/services/chats/${id}/messages`;
   console.log(endpointUrl)
   try {
     const res = await axios.get(endpointUrl, {
       withCredentials: true
     });

     const newMessages = res.data.messages.map((msg: any) => Message.fromJSON(msg));
     messagesRef.current = newMessages;
     forceUpdate(prev => prev + 1);
   } catch (err) {
     console.error("Errore fetch messages:", err);
   }*/
  };


  const render = () => {
    return (
      <>
        <div className=" bg-red flex flex-col flex-1 min-h-0">
          <div className={"border-white/10 justify-center  transition-colors duration-300 ease-in border-b items-center flex w-full pl-3 p-4 text-center"}>
         
            <p className="w-full h-full ">{channel?.name}</p>

          </div>
          <ListaMessaggi messages={messages} />
            <div
            className="flex-1 mb-3 overflow-y-auto flex flex-col-reverse messages-scrollbar"
          ></div>
          <div className="pb-1.5 pr-1.5 pl-1.5">
                <div className="flex items-center gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
                    <Plus className="w-6 h-6 text-white" />
                    <input
                        value={nuovoTesto || ""}
                        className="flex-1 bg-transparent text-[14px] focus:outline-none"
                        onChange={(e) => setNuovoTesto(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") console.log("invia");
                        }}
                        placeholder="Scrivi qui..."
                    />
                </div>
            </div>
        </div>
      </>

    );
  };

  const joinChannel = () => {
    if (!socket || !channel) return;
    let id = channel.id;
    setHeader(channel?.name);
    //fetchMessages(serverId);
    //socket.emit("join_channel", { id });
  }

  return render();
}


export default ChannelChat