
import { PhoneOffIcon, Wifi, VideoIcon, ScreenShare } from "lucide-react";
import { useActiveRoomContext } from "../../context/CallContext";
import { useEffect, useState } from "react";
import { RoomEvent } from "livekit-client";


function CallInterface() {
    const room = useActiveRoomContext().room;
    const title = useActiveRoomContext().title;
    const { setToken } = useActiveRoomContext();
    const [isActive, setIsActive] = useState<boolean>(false);

    useEffect(() => {
        room.on(RoomEvent.Disconnected, () => {
            setIsActive(false);
        })
        room.on(RoomEvent.Connected, () => {
            setIsActive(true);
        })

    }, []);

    if (!isActive) return;

    return (
        <>
            <div className="mx-2 mb-2 mt-1 flex flex-1 w-full gap-2 items-center">
                <div className="flex flex-1 items-center gap-3">
                    <div className="bg-green-400/20 rounded-md p-2 flex items-center justify-center">
                        <Wifi className="text-green-400 h-6 w-6" />
                    </div>

                    <div className="flex flex-col">
                        <span className="text-[14px] font-medium text-white">
                            Connesso a <span className="font-bold">{title}</span>
                        </span>
                        <span className="text-[12px] text-gray-400 flex items-center gap-1/2">
                            Stato:
                            <span
                                className={`p-2 py-0.5 rounded-full text-[12px] ${room.state === "connected" ? "text-green-400" : "bg-red-500/20 text-red-400"
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
        </>



    );
}

export default CallInterface;
