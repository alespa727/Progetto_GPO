
import { ProfilePicture } from "./chat/ProfilePicture";
import { Mic, Headphones, Cog } from "lucide-react";
import { useSettingsStatusContext } from "../context/SettingsContext";
import CallInterface from "./chiamata/CallInterface";
import { useAccount } from "@/context/UserProvider";
import * as motion from "motion/react-client"
import { width } from "./chat/Chats";


function UserProfile() {
  const account = useAccount();
  const { setState } = useSettingsStatusContext()
  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.1 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{
        type: "spring",
        stiffness: 500,
        damping: 30,
        mass: 0.1
      }}
      className="flex-col fixed bottom-0 p-1.5 box-border text-black flex items-center shadow-lg rounded-lg"
      style={{
        width: `calc(var(--chat-width, 300px) + 64px)`
      }}
    >
      <div className="transition-all ease-in flex gap-2 flex-col items-center w-full h-full rounded-[6px] bg-[#313244] text-white text-xl">
        <CallInterface></CallInterface>
        <div className="p-4 flex w-full gap-3.5 items-center ">
          <div className="flex flex-1 items-center gap-2">
            <ProfilePicture src={account?.path ? account.path : ""} className="w-9 h-9" />

            <div className="text-[16px]">{account?.username}</div>

          </div>

          <Mic className="text-gray-300 hover:animate-pulse hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Mic>
          <Headphones className="text-gray-300 hover:animate-pulse hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Headphones>
          <Cog
            onClick={() => setState(true)}
            className="text-gray-300 w-5 h-5 rounded-2xl hover:bg-white/10 hover:text-white 
                      transition-transform duration-300 ease-in-out hover:rotate-180"
          />

        </div>


      </div>
    </motion.div>

  );
}

export default UserProfile;
