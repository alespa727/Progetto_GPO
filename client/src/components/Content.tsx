import Chats from "./chat/Chats.tsx";
import Chat from "./chat/Chat.tsx";
import Server from "./server/Server.tsx"
import "../styles/Content.css";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatListContext.tsx";
import ChannelChat from "./server/ChannelChat.tsx";
import { useActiveServerContext } from "../context/ActiveServerProvider.tsx";
import { ServerContext } from "../context/ServerContext.tsx";
import { useActiveChatContext } from "../context/ActiveChatProvider.tsx";
import { ChatContext } from "../context/ChatContext.tsx";
import { useState } from "react";
import { Room } from "livekit-client";
import ChiamataChat from "./chat/ChiamataChat.tsx";

function Content() {

  const chats = useChats();
  const mode = useMode().mode;
  const activeServer = useActiveServerContext().activeServer;
  const activeChat = useActiveChatContext().activeChat;

  switch (mode) {
    case ClientMode.Server:
      return (
        <div className="content">
          <ServerContext value={activeServer}>
            <div className="server">
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
            <div className="server">
              <Chats></Chats>
              <Chat></Chat>
            </div>
          </ChatContext>
          
        </div>
      );
  }

}

export default Content;
