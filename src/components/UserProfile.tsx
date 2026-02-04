
import { ProfilePicture } from "./chat/ProfilePicture";
import { Mic, Headphones, Cog } from "lucide-react";
import { useSettingsStatusContext } from "../context/SettingsContext";
import CallInterface from "./chiamata/CallInterface";
import { useAccount } from "@/context/UserProvider";
import * as motion from "motion/react-client"


function UserProfile() {
  const account = useAccount();
  const { setState } = useSettingsStatusContext()
  return (
    <motion.div
      initial={{ scale: 0.9, opacity: 0 }}
      animate={{ scale: 1, opacity: 1 }}
      exit={{ scale: 0.9, opacity: 0 }}
      transition={{ type: "spring", stiffness: 260, damping: 25 }}

      className="hidden md:flex flex-col fixed bottom-0 p-1.5 box-border text-black items-center shadow-lg rounded-(--radius)"
      style={{
        width: `calc(var(--chat-width, 300px) + 64px)`
      }}
    >
      <div className="transition-all ease-in flex gap-2 flex-col items-center w-full h-full rounded-(--radius) bg-[#313244] text-white text-xl">
        <CallInterface></CallInterface>
        <div className="p-4 flex w-full gap-3.5 items-center ">
          <div className="flex flex-1 items-center gap-2">
            <ProfilePicture src={account?.path ? account.path.replace("http://localhost:8080/", "") : ""} className="w-9 h-9" />

            <div className="text-[16px]">{account?.username}</div>

          </div>

          <Mic className="text-gray-300 hover:animate-pulse hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Mic>
          <Headphones className="text-gray-300 hover:animate-pulse hover:bg-white/10 hover:text-white rounded-2xl w-5 h-5 right-0"></Headphones>
          <Cog
            onClick={() => setState(true)}
            className="text-gray-300 w-5 h-5 rounded-2xl hover:bg-white/10 hover:text-white 
                      transition-transform duration-1200 ease-in-out hover:rotate-400"
          />

        </div>


      </div>
    </motion.div>

  );
}

export default UserProfile;
