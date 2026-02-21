
import { Mic, Headphones, Cog } from "lucide-react";
import { useSettingsStatusContext } from "../context/SettingsContext";
import CallInterface from "./chiamata/CallInterface";
import { useAccount } from "@/context/UserProvider";
import { motion } from "framer-motion";
import { useState } from "react";

function UserProfile() {
  const account = useAccount();
  const { setState } = useSettingsStatusContext();

  const profileSrc = account?.path ? account.path.replace("http://localhost:8080/", "") : "";

  return (
    <>
      <motion.div
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{
          scale: 1,
          opacity: 1,
          height: "80px",
          width: `calc(var(--chat-width, 300px) + 64px)`,
          padding: `6px`,
        }}
        exit={{ scale: 0.9, opacity: 0 }}
        transition={{ type: "tween", ease: "easeInOut", duration: 0.4 }}
        className="hidden md:flex flex-col fixed bottom-0 box-border text-black items-center shadow-lg rounded-(--radius) overflow-hidden"
        style={{
          zIndex: 50,
          left:"auto",
        }}
      >
        <div className={" transition-all ease-in flex flex-col items-center w-full h-full rounded-(--radius) bg-(--surface0) text-white text-xl overflow-hidden"}>

          <div className="p-4 flex w-full gap-3.5 items-center">
            <div className="flex flex-1 items-center gap-2 min-w-0">
              <motion.img
                layout
                src={profileSrc === "" ? (profileSrc ? profileSrc : "/placeholder.png") : "/placeholder.png"}
                animate={{
                  height:  40,
                  width:  40
                }}
                className="rounded-full cursor-zoom-in object-cover"
             
              />
              <div className={("text-[16px]") + " truncate"}>{account?.username}</div>
            </div>


            <>
              <motion.div className="flex gap-3 items-center"
                animate={{ opacity: 1 }}>
                <Mic className="text-gray-300 hover:text-white w-5 h-5 cursor-pointer" />
                <Headphones className="text-gray-300 hover:text-white w-5 h-5 cursor-pointer" />
                <Cog
                  onClick={() => setState(true)}
                  className="text-gray-300 w-5 h-5 hover:rotate-360 duration-1000 transition-transform cursor-pointer"
                />
              </motion.div>
            </>


          </div>

        </div>

      </motion.div>
    </>
  );
}

export default UserProfile;
