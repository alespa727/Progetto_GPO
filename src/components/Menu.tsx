import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";
import UserProfile from "./UserProfile.tsx";
import { ClientMode, useMode } from "../context/ModeProvider.tsx";
import Settings from "./settings/Settings.tsx"
import { useSettingsStatusContext } from "../context/SettingsContext.tsx";
import Login from "./common/Login.tsx";
import { useFriends } from "@/context/FriendContext.tsx";

function Menu() {
  const modeContext = useMode();
  const { state } = useSettingsStatusContext()
  const friends = useFriends();

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
