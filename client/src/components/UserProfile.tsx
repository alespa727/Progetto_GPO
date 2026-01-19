import { useUser } from "../context/UserProvider";
import { ProfilePicture } from "./chat/ProfilePicture";
import { Hash, Mic, Headphones, Volume2, PhoneCall, Settings, Cog, MicOff, PhoneOffIcon, WifiCog, Wifi } from "lucide-react";
import { useMode, ClientMode } from "../context/ModeProvider";
import { useSettingsStatusContext } from "../context/SettingsContext";
import { useActiveRoomContext } from "../context/CallContext";
function UserProfile() {
  const user = useUser();
  const room = useActiveRoomContext();
  const { state, setState } = useSettingsStatusContext()
  return (
    <div className="flex-col fixed bottom-0 p-1.5 box-border w-[26.149%] text-black flex items-center shadow-lg rounded-lg min-w-66.5 max-w-76.5">
      <div className="transition-all ease-in duration-200 flex gap-4 flex-col items-center w-full h-full rounded-[6px] bg-[#313244] text-white text-xl p-2">
        
        <div className="mx-2 mt-1 p-2 flex flex-1 w-full gap-2">
            <div className="flex flex-1 items-center gap-2">
              <div className="bg-green-400 p-2">
                <Wifi className="text-green-100 rotate-45"></Wifi>
              </div>
              
              <div className="text-[16px]">Chiamata</div>

            </div>
            <PhoneOffIcon className="text-gray-300 hover:bg-white/10 hover:text-white w-5 h-5 right-4"></PhoneOffIcon>
        </div>

        <div className="mb-2 px-2 grid grid-cols-4 gap-1 w-full h-5">
          <div className="rounded-md bg-red-500 flex items-center justify-center text-white">1</div>
          <div className="rounded-md bg-blue-500 flex items-center justify-center text-white">2</div>
          <div className="rounded-md bg-green-500 flex items-center justify-center text-white">3</div>
          <div className="rounded-md bg-yellow-500 flex items-center justify-center text-black">4</div>
        </div>

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
