import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import { useEffect } from "react";
import UserProfile from "./UserProfile.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatListContext.tsx";
import Settings from "./settings/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";
import Login from "./common/Login.tsx";
import { useSocket, useSocketStatus } from "@/context/SocketProvider.tsx";

function Menu() {
  const modeContext = useMode();
  const isConnectedToSocket = useSocketStatus();
  const { state } = useSettingsStatusContext()

  if (modeContext.mode === ClientMode.Login) {
    return (
      <>
        <div className="app">
          <Login>

          </Login>
        </div>
      </>
    );
  }

  if (!isConnectedToSocket) {
    return (
     <div className="flex h-full w-full items-center justify-center">
      <h2>Disconnected</h2>
    </div>
    )
  }


  if (state) {
    return (
      <>
        <div className="app">
          <Settings>

          </Settings>
        </div>

      </>
    );
  } else {
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
