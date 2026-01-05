import { act, useState } from "react";
import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import { chat_type, chat, server } from "../types.tsx";
import { useEffect } from "react";
import UserProfile from "./UserProfile.tsx";

function Menu() {
  const [servers, setServers] = useState<server[]>([
    {
      id: 1, name: "server di ale.", description: "Il server di Alessio", createdAt: new Date(), default_channel: {
        id: 1,
        name: "Canale di ale.",
        type: chat_type.CHANNEL
      }
    },
    {
      id: 2, name: "server di Tommy", description: "Server di Tommy", createdAt: new Date(), default_channel: {
        id: 1,
        name: "Canale di tommy.",
        type: chat_type.CHANNEL
      }
    }
  ]);

  const [activeServer, setActiveServers] = useState<server>(servers[0]);
  const [mode, setMode] = useState<string>("chats");

  const initialChats: chat[] = [
    { type: chat_type.FRIEND, name: "ale.", id: 1 },
    { type: chat_type.FRIEND, name: "Tommy", id: 2 }
  ];

  const [chats, setChats] = useState<chat[]>(initialChats);
  const [activeChat, setActiveChat] = useState<chat>(initialChats[0]);

  useEffect(() => {
    if (mode === "chats") {
      setActiveChat(chats[0]);
    } else if (mode === "servers") {
      setActiveChat(activeServer.default_channel); // default per server
    }
  }, [mode, chats, activeServer]);

  return (
    <div className="app">
      <Sidebar servers={servers} setMode={setMode} setActiveServer={setActiveServers}></Sidebar>
      <Content setActiveChat={setActiveChat} activeChat={activeChat} chats={chats} setChats={setChats} mode={mode} server={activeServer}></Content>
      <UserProfile></UserProfile>
    </div>
  );
}

export default Menu;
