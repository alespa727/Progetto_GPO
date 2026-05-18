
import { PhoneOffIcon, Wifi, VideoIcon, ScreenShare } from "lucide-react";
import { useActiveRoomContext } from "../../context/RoomContext";
import { useEffect, useState } from "react";
import { RoomEvent } from "livekit-client";
import * as motion from "motion/react-client"
import { useSocket } from "@/context/SocketProvider";
import { useChatContext } from "@/context/ChatContext";

function CallInterface() {
    const room = useActiveRoomContext().room;
    const chat = useChatContext();
    const socket = useSocket();
    const { setToken } = useActiveRoomContext();
    const [isActive, setIsActive] = useState<boolean>(false);
    if (!chat) return;
    useEffect(() => {
        
        const handleDisconnected = () => {
            socket?.emit("leave_call", { chatId: chat.id });
            setIsActive(false);
            console.log("ciaoaooaosadoa")
        };

        const handleConnected = () => {
           
            setIsActive(true);
            console.log("ciaoaooaosadoa")
        };

       
        room.on(RoomEvent.Disconnected, handleDisconnected);
        room.on(RoomEvent.Connected, handleConnected);

        return () => {
            room.off(RoomEvent.Disconnected, handleDisconnected);
            room.off(RoomEvent.Connected, handleConnected);
        };

    }, [room, socket]); 

    if (!isActive) return;

    return (
        <motion.div initial={{ opacity: 0, scale: 0.1 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{
                type: "spring",
                stiffness: 800,
                damping: 30,
                mass: 0.1
            }} className="border-b border-white/20 w-full  px-2 pt-2">
            <div className="mb-2 mt-1 flex flex-1 w-full gap-2 items-center">
                <div className="flex flex-1 items-stretch gap-3 h-12">
                    <div className="bg-green-400/20 rounded-md w-12 flex items-center justify-center">
                        <Wifi className="text-green-400 h-6 w-6" />
                    </div>

                    <div className="flex flex-col justify-center text-[12px]">
                        {/*<span className="font-medium text-white">
                            Connesso a <span className="font-bold">{title}</span>
                        </span>*/}

                        <span className="text-gray-400 flex items-center gap-1">
                            Stato:
                            <span
                                className={`px-1 py-0.5 rounded-full
          ${room.state === "connected"
                                        ? "text-green-400"
                                        : "bg-red-500/20 text-red-400"
                                    }`}
                            >
                                {room.state}
                            </span>
                        </span>
                    </div>
                </div>



                <PhoneOffIcon
                    className="text-gray-300 hover:bg-white/10 hover:text-white w-5 h-5 shrink-0 mr-3 cursor-pointer"
                    onClick={() => setToken("")}
                />
            </div>



            <div className="mb-3 p-1/2 grid grid-cols-4 gap-1 w-full h-8">
                <div className="rounded-md  cursor-pointer bg-black/20 flex items-center justify-center text-white hover:bg-white/10">
                    <VideoIcon className="w-5 h-5"></VideoIcon>
                </div>
                <div className="rounded-md  cursor-pointer bg-black/20 flex items-center justify-center text-white hover:bg-white/10">
                    <ScreenShare className="w-5 h-5"></ScreenShare>
                </div>
                <div className="rounded-md  cursor-pointer bg-black/20 flex items-center justify-center text-white hover:bg-white/10">
                    <VideoIcon className="w-5 h-5"></VideoIcon>
                </div>
                <div className="rounded-md  cursor-pointer bg-black/20 flex items-center justify-center text-white hover:bg-white/10">
                    <ScreenShare className="w-5 h-5"></ScreenShare>
                </div>
            </div>
        </motion.div>



    );
}

export default CallInterface;
