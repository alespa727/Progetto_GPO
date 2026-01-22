import { useUser } from "../context/UserProvider";
import { ProfilePicture } from "./chat/ProfilePicture";
import { Hash, Mic, Headphones, Volume2, PhoneCall, Settings, Cog, MicOff, PhoneOffIcon, WifiCog, Wifi } from "lucide-react";
import { useMode, ClientMode } from "../context/ModeProvider";
import { useSettingsStatusContext } from "../context/SettingsContext";
import { useActiveRoomContext } from "../context/CallContext";
import CallInterface from "./chiamata/CallInterface";
function UserProfile() {
  const user = useUser();
  const room = useActiveRoomContext();
  const { state, setState } = useSettingsStatusContext()
  const {setToken} = useActiveRoomContext();
  return (
    <div className="flex-col fixed bottom-0 p-1.5 box-border text-black flex items-center shadow-lg rounded-lg w-66">
      <div className="transition-all ease-in duration-200 flex gap-4 flex-col items-center w-full h-full rounded-[6px] bg-[#313244] text-white text-xl p-2">
        
        <CallInterface></CallInterface>
        <div className="m-2 px-2 flex w-full gap-3.5 items-center">
          <div className="flex flex-1 items-center gap-2">
            <ProfilePicture className="w-7 h-7" />

            <div className="text-[16px]">{user?.username}</div>

          </div>

          <Mic className="text-gray-300 hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Mic>
          <Headphones className="text-gray-300 hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Headphones>
          <Cog onClick={() => { setState(true) }} className="text-gray-300 hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0" />

        </div>


      </div>
    </div>

  );
}

export default UserProfile;
