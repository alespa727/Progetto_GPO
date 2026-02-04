import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useActiveRoomContext } from "@/context/CallContext";
import { useAccount } from "@/context/UserProvider";
import { endpoint } from "@/types";
import { useRoomContext } from "@livekit/components-react";
import axios from "axios";
import { RoomEvent } from "livekit-client";
import { ArrowLeft, PhoneCallIcon } from "lucide-react";
import { useEffect, useState } from "react";

export function Header({ value }: { value: string }) {
    const { setUrl,setToken, setTitle } = useActiveRoomContext()
    const account = useAccount();
    const chat = useActiveChatContext().activeChat;
    const setActiveChat = useActiveChatContext().setActiveChat;
    const url = "wss://progettogpo-dfna4rrr.livekit.cloud"; 
    const room = useRoomContext();
    const [roomStatus, setRoomStatus] = useState(false);

    useEffect(()=>{
        console.log(room.state)
        if(room.state === "connected" || room.state === 'connecting'){
            setRoomStatus(true);
        }
        const handleRoomDisconnection = ()=>{
            setRoomStatus(false)
        };
        room.on(RoomEvent.Disconnected, handleRoomDisconnection)

        return ()=>{
            room.off(RoomEvent.Disconnected, handleRoomDisconnection)
        }
    }, [room, room.state])

    if(!chat) return;
    

    const handleClick = async () => {
        const res = await axios.post(endpoint+"/token", {
            identity: account?.username,
            roomName: "chat_" + chat.id
        });
        
        setUrl(url)
        setToken(res.data.token);
        setTitle(chat.friend.username);
        setRoomStatus(true)
    
       
    };

   
    return (
        <div className={(roomStatus ? "bg-black ": "")+"border-white/10 justify-center  transition-colors duration-300 ease-in border-b items-center flex w-full pl-3 p-4 text-center"}>
            <ArrowLeft className="md:hidden block cursor-pointer text-white/60" onClick={()=>setActiveChat(null)}></ArrowLeft>
            <p className="w-full h-full ">{roomStatus ? "Chiamata con "+value : "Chat con "+value }</p>
           
            
            {
                !roomStatus ? 
                (<PhoneCallIcon className="cursor-pointer text-white/60" onClick={handleClick}></PhoneCallIcon>)
                :
                ""
            }
            
        </div>
    )
}