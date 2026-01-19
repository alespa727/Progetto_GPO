import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useChannelContext } from "../../context/ChannelContext";
import { ChannelType } from "../../types";
import { Hash, Mic, Headphones, Volume2, PhoneCall } from "lucide-react";
import { ProfilePicture } from "../chat/ProfilePicture";
import { useActiveRoomContext } from "../../context/CallContext";
import { Room } from "livekit-client";
import { useEffect, useState } from "react";
import axios from "axios";
import { useUser } from "../../context/UserProvider";

export function ChannelName() {
  const channel = useChannelContext();
  const { setActiveChannel, activeChannel } = useActiveServerContext();

  const isActive = activeChannel?.id === channel.id;
  const setRoom = useActiveRoomContext().setRoom;

  const user = useUser();
  const url = "wss://progettogpo-dfna4rrr.livekit.cloud";
  const [room] = useState<Room>(new Room());
  const [token, setToken] = useState("");

  const connectRoom = async () => {
    if (token.length === 0) return;
    await room.connect(url, token, {
      rtcConfig: { iceTransportPolicy: "all" },
    });

    console.log('connected to room', room.name);
  }

  const handleClick = async() => {
    if (channel.type !== ChannelType.VOICE) {
      setActiveChannel(channel);
    } else {
      console.log(user);
      const res = await axios.post("http://localhost:4000/token", {
        identity: user?.username,
        roomName: "channel_"+channel.id
      });
      setToken(res.data.token);
      //setRoom();
    }
  };
  
  useEffect(()=>{
    if(token.length > 0)
    connectRoom();
  }, [token])

  return (
    <>
      <button
        onClick={handleClick}
        className={`
        w-full text-left rounded-md 
        transition-all duration-150
        ${isActive
            ? "bg-white/20 text-white"
            : "text-gray-300 hover:bg-white/10 hover:text-white"
          }
      `}
      >
        <div className="w-full text-left rounded-md px-3 py-2
        flex items-center gap-2
        transition-all duration-150">
          {channel.type === ChannelType.VOICE ? (
            <Volume2 className="w-4 h-4 inline-block" />
          ) : (
            <Hash className="w-4 h-4 inline-block" />
          )}
          <span className="truncate font-medium">
            {channel.title}
          </span>
        </div>

        {/*channel.type === ChannelType.VOICE*/false ? (
          <div className="  w-full text-left rounded-md px-3 pl-8 mb-1
        flex items-center gap-1
        transition-all duration-150 pb-2">
            <ProfilePicture className="w-5 h-5 "></ProfilePicture>
            <ProfilePicture className="w-5 h-5"></ProfilePicture>
          </div>
        ) : ""}




      </button>

      
        {channel.type === ChannelType.VOICE ? (
          <div className="  w-full text-left rounded-md px-3 pl- mb-1
        flex flex-col gap-1/2 mt-1
        transition-all duration-150 pb-2">
            <div className="rounded-md p-2 flex gap-2 hover:bg-white/10 ">
              <ProfilePicture className="w-5 h-5"></ProfilePicture>
              <p>user1</p>
            </div>
             <div className="rounded-md p-2 flex gap-2 hover:bg-white/10 ">
              <ProfilePicture className="w-5 h-5"></ProfilePicture>
              <p>user2</p>
            </div>
          </div>
        ) : ""}
    </>

  );
}
