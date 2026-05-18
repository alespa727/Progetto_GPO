import "../styles/Sidebar.css";
import { Server } from "../types";
import { useServers } from "../context/ServerListContext";
import { ClientMode, useMode } from "../context/ModeProvider";
import { useActiveServerContext } from "../context/ActiveServerProvider";
import { ServerLoop } from "./server/ServerLoop";
import { ServerPicture } from "./server/ServerPicture";
import { Cog, PersonStanding } from "lucide-react";
import { CreateServerButton } from "./server/CreaServer";
import { motion } from "motion/react";
import { useSettingsStatusContext } from "@/context/SettingsContext";
import { useActiveChatContext } from "@/context/ActiveChatProvider";
function Sidebar() {

  const setClientMode = useMode().setMode;
  const servers: Server[] | null = useServers() ?? null;
  const setActiveServer = useActiveServerContext().setActiveServer;
  const setActiveChat = useActiveChatContext().setActiveChat;
  const circleStyle = "w-[65%] rounded-full aspect-square self-center ";

  const settings = useSettingsStatusContext();
  if (servers === null) return;

  return (
    <div className="h-full shrink-0 w-16 items-center flex bg-(--background) content-center flex-col">

      <motion.div
        initial={{ background: "#FFFFFF10z"}}
        whileHover={{ scale: 1.01, background: "#FFFFFF1F" }}
        whileTap={{ scale: 0.97, background: "#7ED957" }}
        transition={{
          type: "spring",
          stiffness: 200,
          damping: 20,
          mass: 0.5
        }}
        onClick={() => { setActiveChat(null); setActiveServer(null); setClientMode(ClientMode.Chats) }}
        className={circleStyle.concat("mb-1 text-5xl flex items-center justify-center active:bg-green-50 text-white font-semibold shadow-md hover:shadow-lg cursor-pointer select-none")}
      >
        <PersonStanding></PersonStanding>
      </motion.div>

      <div className="m-1 border-white/20 border" />
      <ServerLoop servers={servers}>
        <ServerPicture alt="" style={circleStyle.concat("mt-2")}></ServerPicture>
      </ServerLoop>

      <CreateServerButton></CreateServerButton>

      <div className="grow"></div>
      <Cog
        onClick={() => { 
          settings.setState(true);
        }}
        className="text-gray-300 mb-6 w-7 h-7 hover:rotate-360 duration-1000 transition-transform cursor-pointer  md:invisible"
      />


    </div>
  );
}

export default Sidebar;
