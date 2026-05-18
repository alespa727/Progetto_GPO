import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useActiveRoomContext } from "@/context/RoomContext";
import { useSocket } from "@/context/SocketProvider";
import { useAccount } from "@/context/UserProvider";
import { endpoint } from "@/types";
import { useRoomContext } from "@livekit/components-react";
import axios from "axios";
import { RoomEvent } from "livekit-client";
import { ArrowLeft, PhoneCallIcon, PhoneMissed, PhoneOff } from "lucide-react";
import { useEffect, useState } from "react";
export function Header({ value }: { value: string }) {
  const { setUrl, setToken, setTitle } = useActiveRoomContext()
  const account = useAccount();
  const socket = useSocket();
  const chat = useActiveChatContext().activeChat;
  const setActiveChat = useActiveChatContext().setActiveChat;
  const url = "wss://progettogpo-dfna4rrr.livekit.cloud";
  const room = useRoomContext();
  const [roomStatus, setRoomStatus] = useState(false);

  // Derivato, non stato separato
  const isCallChat = roomStatus && chat != null && room.name === "chat_" + chat.id;

  useEffect(() => {
    const updateStatus = () => {
      const isConnected = room.state === "connected" || room.state === "connecting";
      setRoomStatus(isConnected);
    };

    updateStatus();

    room.on(RoomEvent.Connected, updateStatus);
    room.on(RoomEvent.Reconnecting, updateStatus);
    room.on(RoomEvent.Disconnected, updateStatus);

    return () => {
      room.off(RoomEvent.Connected, updateStatus);
      room.off(RoomEvent.Reconnecting, updateStatus);
      room.off(RoomEvent.Disconnected, updateStatus);
    };
  }, [room]);

  if (!chat) return;

  const handleClick = async () => {

    socket?.emit("get_token_call", {
      identity: account?.username,
      roomName: "chat_" + chat.id
    })

    const handleToken = (data: any) => {
      setUrl(url);
      setToken(data.token);
      setTitle(chat.friend.username);
      socket?.off("tokenCall", handleToken)
    }
    socket?.on("tokenCall", handleToken)

  };

  return (
    <div className={(isCallChat ? "bg-black" : "bg-(--background)") + " border-white/10 justify-center transition-colors duration-300 ease-in border-b items-center flex w-full pl-3 p-4 text-center"}>
      <ArrowLeft className="md:hidden block cursor-pointer text-white/60" onClick={() => setActiveChat(null)} />
      <p className="w-full h-full">
        {isCallChat ? "Chiamata con " + value : "Chat con " + value}
      </p>
      {!roomStatus && (
        <PhoneCallIcon className="cursor-pointer" onClick={handleClick} />
      )}
    </div>
  )
}