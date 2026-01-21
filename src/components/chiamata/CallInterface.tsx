
import { Hash, Mic, Headphones, Volume2, PhoneCall, Settings, Cog, MicOff, PhoneOffIcon, WifiCog, Wifi } from "lucide-react";
import { useSettingsStatusContext } from "../../context/SettingsContext";
import { useActiveRoomContext } from "../../context/CallContext";
import { useEffect, useState } from "react";
import { RoomEvent } from "livekit-client";
import { useActiveServerContext } from "../../context/ActiveServerProvider";


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
            <div className="mx-2 mt-1 p-2 flex flex-1 w-full gap-2 items-start">
                {/* Icona + scritte */}
                <div className="flex flex-1 items-start gap-2">
                    <div className="bg-green-400/40 rounded-md p-2">
                        <Wifi className="text-green-100 h-6 w-6" />
                    </div>

                    {/* Contenitore verticale per scritte */}
                    <div className="flex flex-col">
                        <div className="text-[14px]">
                            {"Connesso a " + title}
                        </div>
                        <div className="text-[14px] text-gray-400">
                            {"Stato: " + room.state}
                        </div>
                    </div>
                </div>

                {/* Bottone per chiudere */}
                <PhoneOffIcon
                    className="text-gray-300 m-2 hover:bg-white/10 hover:text-white w-5 h-5 cursor-pointer"
                    onClick={() => { setToken(""); }}
                />
            </div>



            <div className="mb-2 px-2 grid grid-cols-4 gap-1 w-full h-5">
                <div className="rounded-md bg-red-500 flex items-center justify-center text-white">1</div>
                <div className="rounded-md bg-blue-500 flex items-center justify-center text-white">2</div>
                <div className="rounded-md bg-green-500 flex items-center justify-center text-white">3</div>
                <div className="rounded-md bg-yellow-500 flex items-center justify-center text-black">4</div>
            </div>
        </>



    );
}

export default CallInterface;
