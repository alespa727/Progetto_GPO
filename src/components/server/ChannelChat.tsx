import { useState, useRef, useEffect } from "react";
import "../../styles/Chat.css";
import axios from "axios";
import { Message, ChannelType } from "../../types.tsx";
import { useSocket } from "../../context/SocketProvider.tsx";
import { useActiveServerContext } from "../../context/ActiveServerProvider.tsx";
import { useUser } from "../../context/UserProvider.tsx";
import { ProfilePicture } from "../chat/ProfilePicture.tsx";
import { Plus } from "lucide-react";

function ChannelChat() {
  const user = useUser();
  const socket = useSocket();
  const [nuovoTesto, setNuovoTesto] = useState<string>("");
  const messagesRef = useRef<null | HTMLDivElement>(null);
  const activeServer = useActiveServerContext().activeServer;
  const activeChannel = useActiveServerContext().activeChannel;
  const setActiveChannel = useActiveServerContext().setActiveChannel;
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
    if (!socket) return;
    if (!nuovoTesto) return;
    if (nuovoTesto.trim() === "") return;
    if (!user) return;
    let id = activeChannel ? activeChannel.id : null;
    let message = new Message(nuovoTesto, user, new Date());
    let type = "channel";
    socket.emit("sendMessage", { id, type, message });

    setNuovoTesto("");
  };

  useEffect(() => {
    setCallActive(activeChannel?.type === ChannelType.VOICE);
  }, [activeChannel]);

  useEffect(() => {
    if (!socket) return;
    const handleNewMessage = (data: any) => {
      setMessages((prevMessages) => [...prevMessages, Message.fromJSON(data)]);
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

  const render = () => {
    return (
      <>
       <div className="flex flex-col w-full">
  <div className="bg-[#1e1e2e] w-full pl-3 p-4 text-center">
    {header}
  </div>

  <div className="flex relative">
    {/* CHAT */}
    <div className="flex flex-col flex-1 min-h-0 overflow-hidden box-border relative">
      {/* MESSAGGI */}
      <div className="flex-1 overflow-y-auto" ref={messagesRef}>
        {[...messages].reverse().map((msg: Message, index: number) => (
          <div
            key={index}
            className="rounded-r-md px-4 mr-4 hover:bg-white/10 flex items-center"
          >
            <div className="gap-2 flex p-1 text-[15px]">
              <p className="text-[10px] m-auto font-extralight">
                {(msg.time.getHours() <= 9 ? "0" : "") +
                  msg.time.getHours() +
                  ":" +
                  (msg.time.getMinutes() <= 9 ? "0" : "") +
                  msg.time.getMinutes()}
              </p>
              <b className="text-red-400">{msg.sender.username}</b>
              <p>{msg.text}</p>
            </div>
          </div>
        ))}
      </div>

      {/* INPUT */}
      <div className="p-2 bg-[#1e1e2e]">
        <div className="flex items-center gap-3 bg-[#313244] rounded-md px-3 py-2 text-white shadow-lg">
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

    {/* SPAZIO DESTRO */}
    <div className="w-20"></div>
  </div>
</div>

      </>
    );
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

  return render();
}

export default ChannelChat;
