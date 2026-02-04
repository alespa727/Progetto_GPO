import Chats from "./chat/Chats.tsx";
import Chat from "./chat/Chat.tsx";
import Server from "./server/Server.tsx"
import "../styles/Content.css";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import ChannelChat from "./server/ChannelChat.tsx";
import { useActiveServerContext } from "../context/ActiveServerProvider.tsx";
import { ServerContext } from "../context/ServerContext.tsx";
import { useActiveChatContext } from "../context/ActiveChatProvider.tsx";
import { ChatContext } from "../context/ChatContext.tsx";
import { useSocketStatus } from "@/context/SocketProvider.tsx";
import { Loader, Loader2Icon } from "lucide-react";
import { div } from "motion/react-client";

function Content() {

  const mode = useMode().mode;
  const activeServer = useActiveServerContext().activeServer;
  const activeChat = useActiveChatContext().activeChat;
  const socketStatus = useSocketStatus();

  if (!socketStatus) {
    return (
      <>
        <div className="z-1000 absolute flex items-center justify-center bg-[#292938ff] w-full h-full">
          <Loader2Icon className="w-20 h-20 text-white animate-spin"></Loader2Icon>
        </div>
      </>
    )
  }

  switch (mode) {
    case ClientMode.Server:
      return (
        <div className="content">
          <ServerContext value={activeServer}>
            <div className="flex box-border h-full border-solid border border-[#313244]  bg-[#292938ff]">
              <Server></Server>
              <ChannelChat></ChannelChat>
            </div>
          </ServerContext>

        </div>
      );
    case ClientMode.Chats:
      return (
        <div className="content">
          <ChatContext value={activeChat}>
            <div className="flex box-border w-full h-full border-solid border border-[#313244]  bg-[#292938ff]">
              <Chats></Chats>
              <Chat></Chat>
            </div>
          </ChatContext>



        </div>
      );
  }

}

export default Content;
