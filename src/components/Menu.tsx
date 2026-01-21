import { act, useState } from "react";
import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import { PrivateChat, Friendship, User, PrivateChatResponse, Server, Channel, Section } from "../types.tsx";
import { useEffect } from "react";
import UserProfile from "./UserProfile.tsx";
import axios from "axios";
import { ClientMode, ModeProvider, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatListContext.tsx";
import Settings from "../components/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";

function Menu() {
  const modeContext = useMode();
  const chats: PrivateChatResponse[] | null = useChats();
  const {state} = useSettingsStatusContext()
  useEffect(() => {
    if (modeContext.mode === ClientMode.Chats) {
      if(!chats) return;
    }
  }, [modeContext.mode, chats]);

  if(state){
    return (
      <>
        <div className="app">
          <Settings> 

          </Settings>
        </div>
        
      </>
    );
  }else{
    return (
      <div className="app">
        <Sidebar></Sidebar>
        <Content></Content>
        <UserProfile></UserProfile>
      </div>
    );
  }

}

export default Menu;
