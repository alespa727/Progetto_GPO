import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import UserProfile from "./UserProfile.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import Settings from "./settings/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";
import Start from "./common/Start.tsx";
import { useFriends } from "@/context/FriendContext.tsx";

function Menu() {
  const modeContext = useMode();
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
