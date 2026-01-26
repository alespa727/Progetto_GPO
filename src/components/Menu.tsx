import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import { useEffect } from "react";
import UserProfile from "./UserProfile.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import { useChats } from "../context/ChatListContext.tsx";
import Settings from "../components/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";
import Login from "./common/Login.tsx";

function Menu() {
  const modeContext = useMode();
  const {state} = useSettingsStatusContext()

  if(modeContext.mode === ClientMode.Login){
     return (
      <>
        <div className="app">
          <Login> 

          </Login>
        </div>
      </>
    );
  }

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
