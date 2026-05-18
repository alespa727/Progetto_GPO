import { Mic, Headphones, Cog, MicOff, UserIcon, User2Icon, PhoneOff } from "lucide-react";
import { useSettingsStatusContext } from "../context/SettingsContext";
import { useAccount } from "@/context/UserProvider";
import { motion } from "framer-motion";
import { useActiveRoomContext } from "@/context/RoomContext";
import { useEffect, useState } from "react";
import { useAudioControls } from "@/context/AudioControlContext";
import { HeadphoneOff } from "lucide-react";
import { RoomEvent } from "livekit-client";
import { AnimatePresence } from "framer-motion";
import { useChats } from "@/context/ChatListContext";

function UserProfile() {
  const account = useAccount();
  const room = useActiveRoomContext().room;
  const { setState } = useSettingsStatusContext();
  const { muted, setMuted, deafened, setDeafened } = useAudioControls();

  const [roomStatus, setRoomStatus] = useState(false);

  const chats = useChats()?.chats;

  const otherCallUser = chats?.find(c => c.id.toString() === room.name.replace("chat_", ""));

  useEffect(() => {
    const updateStatus = () => {
      const isConnected = room.state === "connected" || room.state === "connecting";
      setRoomStatus(isConnected);
    };

    updateStatus();

    room.on(RoomEvent.Connected, updateStatus);
    room.on(RoomEvent.Reconnecting, updateStatus);
    room.on(RoomEvent.Disconnected, updateStatus);

    return () => {
      room.off(RoomEvent.Connected, updateStatus);
      room.off(RoomEvent.Reconnecting, updateStatus);
      room.off(RoomEvent.Disconnected, updateStatus);
    };
  }, [room]);

  const profileSrc = account?.path
    ? account.path.replace("http://localhost:8080/", "")
    : "";

  useEffect(() => {
    room.localParticipant.setMicrophoneEnabled(!muted);
  }, [muted]);

  useEffect(() => {
    if (deafened) setMuted(true);
    room.localParticipant.setMicrophoneEnabled(!deafened);
    room.remoteParticipants.forEach((participant) => {
      participant.audioTrackPublications.forEach((pub) => {
        if (pub.track) {
          pub.track.setMuted(deafened);
        }
      });
    });
  }, [deafened]);


  return (
    <motion.div
      initial={{ scale: 0.9, opacity: 0 }}
      animate={{
        scale: 1,
        opacity: 1,
        height: roomStatus ? "140px" : "80px",
        width: `calc(var(--chat-width, 300px) + 64px)`,
        padding: `6px`,
      }}
      exit={{ scale: 0.9, opacity: 0 }}
      transition={{ type: "tween", ease: "easeInOut", duration: 0.4 }}
      className="hidden md:flex flex-col fixed bottom-0 box-border text-black items-center shadow-lg rounded-(--radius) overflow-hidden"
      style={{ zIndex: 50, left: "auto" }}
    >
      <div className="flex flex-col-reverse w-full h-full rounded-(--radius) bg-(--surface0) text-white overflow-hidden">

        {/* Barra utente — sempre visibile */}
        <div className="p-4 flex w-full gap-3.5 items-center shrink-0">
          <div className="flex flex-1 items-center gap-2 min-w-0">
            <motion.img
              layout
              src={profileSrc || "/placeholder.png"}
              animate={{ height: 40, width: 40 }}
              className="rounded-full cursor-zoom-in object-cover"
            />
            <div className="text-[16px] truncate">{account?.username}</div>
          </div>

          <div className="flex gap-3 items-center">
            <button
              onClick={() => setMuted(m => !m)}
              className={`p-1.5 rounded-md transition-colors ${muted ? "text-red-400 bg-red-400/10 hover:bg-red-400/20" : "text-gray-400 hover:text-white hover:bg-white/10"}`}
            >
              {muted ? <MicOff className="w-4 h-4" /> : <Mic className="w-4 h-4" />}
            </button>
            <button
              onClick={() => setDeafened(d => !d)}
              className={`p-1.5 rounded-md transition-colors ${deafened ? "text-red-400 bg-red-400/10 hover:bg-red-400/20" : "text-gray-400 hover:text-white hover:bg-white/10"}`}
            >
              {deafened ? <HeadphoneOff className="w-4 h-4" /> : <Headphones className="w-4 h-4" />}
            </button>
            <button
              onClick={() => setState(true)}
              className="p-1.5 rounded-md text-gray-400 hover:text-white hover:bg-white/10 transition-colors duration-300"
            >
              <Cog className="w-4 h-4 transition-transform duration-300 hover:rotate-90" />
            </button>
          </div>
        </div>

        {/* Pannello chiamata — animato con AnimatePresence */}
        {/* Pannello chiamata */}
        <AnimatePresence>
          {roomStatus && (
            <motion.div
              key="call-bar"
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: 72 }}
              exit={{ opacity: 0, height: 0 }}
              transition={{ duration: 0.25 }}
              className="w-full overflow-hidden border-t border-white/5 bg-black/20 backdrop-blur-md"
            >
              <div className="flex items-center justify-between h-full px-4">

                {/* Info chiamata */}
                <div className="flex items-center gap-3 min-w-0">

                  <div className="flex items-center justify-center w-11 h-11 rounded-full bg-white/5 shrink-0">
                    <User2Icon className="w-5 h-5 text-gray-300" />
                  </div>

                  <div className="min-w-0">
                    <h2 className="text-sm font-medium truncate">
                      {otherCallUser
                        ? `Chiamata con ${otherCallUser.friend.username}`
                        : "Chiamata vocale"}
                    </h2>

                    <div className="flex items-center gap-1 text-xs text-green-400">
                      <span className="w-2 h-2 rounded-full bg-green-400 animate-pulse" />
                      Connesso
                    </div>
                  </div>
                </div>

                {/* Controlli */}
                <div className="flex items-center gap-2">
    

                  {/* Chiudi chiamata */}
                  <button
                    onClick={() => room.disconnect()}
                    className="
                      flex items-center justify-center
                      w-9 h-9 rounded-full
                      bg-(--crust) hover:bg-(--crust)
                      transition-all duration-200
                    "
                  >
                    <PhoneOff className="w-4 h-4 text-white" />
                  </button>

                </div>
              </div>
            </motion.div>
          )}
        </AnimatePresence>

      </div>
    </motion.div>
  );
}

export default UserProfile;