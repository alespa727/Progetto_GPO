import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import UserProfile from "./UserProfile.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import Settings from "./settings/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";
import Start from "./common/Start.tsx";
import { useFriends } from "@/context/FriendContext.tsx";
import { useSocket, useSocketStatus } from "@/context/SocketProvider.tsx";
import Modal from "./common/Modal.tsx";
import { WifiOff } from "lucide-react";

function Menu() {
  const modeContext = useMode();
  const socket = useSocketStatus();
  const { state } = useSettingsStatusContext()
  const friends = useFriends();

  if (modeContext.mode === ClientMode.Login) {
    return (
      <>
        <div className="app">
          <Start>

          </Start>
        </div>
      </>
    );
  }

  if (!socket) {
    return (
      <div className="flex flex-col items-center justify-center w-full h-full gap-4">
        <div className="w-12 h-12 rounded-full bg-white/5 border border-white/10 flex items-center justify-center">
          <WifiOff className="w-5 h-5 text-white/30" />
        </div>
        <div className="flex flex-col items-center gap-1">
          <span className="text-white/60 text-sm font-medium">Connessione assente</span>
          <span className="text-white/20 text-xs">Controlla la tua connessione e riprova</span>
        </div>
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
      <div className="app flex-1">
        <Sidebar></Sidebar>
        <Content></Content>
        <UserProfile></UserProfile>
      </div>
    );
  }

}

export default Menu;
