import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useChannelContext } from "../../context/ChannelContext";
import { ChannelType } from "../../types";
import { Hash, Volume2, Eye } from "lucide-react";
import { ProfilePicture } from "../chat/ProfilePicture";
import { useEffect, useState } from "react";
import { useSocket } from "../../context/SocketProvider";
import { RoomAudioRenderer } from "@livekit/components-react";
import * as ContextMenu from "@radix-ui/react-context-menu";

export function ChannelName() {
  const channel = useChannelContext();
  console.log(channel)
  const { setActiveChannel, activeChannel } = useActiveServerContext();

  const isActive = activeChannel?.id === channel.id;
  const socket = useSocket();
  const [userList, setUsers] = useState<string[]>([]);
  let [count, setCount] = useState<number>(0);

  useEffect(() => {

    const handleUsers = ({
      channelId,
      users
    }:
      {
        channelId: number,
        users: string[]
      }) => {

      if (channelId !== channel.id) return;
      setCount(prev => prev + 1);
      setUsers(users)
    }

    socket?.emit("get_channel_users", { channelId: channel.id });
    socket?.on("voice_users_update", handleUsers);

  }, []);

  const handleClick = async () => {
  
    setActiveChannel(channel);
    console.log("Attivato", channel)
  };

  return (
    <>
      <ContextMenu.Root >
        <ContextMenu.ContextMenuTrigger>
          <button
            onClick={handleClick}
            className={`
        w-full text-left rounded-(--radius)
        transition-all duration-150
        ${isActive
                ? "bg-white/20 text-white"
                : "text-gray-300 hover:bg-white/10 hover:text-white"
              }
      `}
          >
            <div className="w-full text-left rounded-(--radius) px-3 py-2
        flex items-center gap-2
        transition-all duration-150">
              {channel.type === ChannelType.VOICE ? (
                <Volume2 className="w-4 h-4 inline-block" />
              ) : (
                <Hash className="w-4 h-4 inline-block" />
              )}
              <span className="truncate font-medium">
                {channel.name}
              </span>
            </div>

            {false ? (
              <div className="  w-full text-left rounded-(--radius) px-3 pl-8 mb-1
        flex items-center gap-1
        transition-all duration-150 pb-2">
                <ProfilePicture className="w-5 h-5 "></ProfilePicture>
                <ProfilePicture className="w-5 h-5"></ProfilePicture>
              </div>
            ) : ""}




          </button>


          {channel.type === ChannelType.VOICE ? (
            <div className="  w-full text-left rounded-(--radius) px-3 pl- mb-1
        flex flex-col gap-1/2 mt-1
        transition-all duration-150 pb-2">
              <RoomAudioRenderer />
              {

                userList.map((username) => {

                  return (
                    <div className="rounded-(--radius) p-2 flex gap-2 hover:bg-white/10 ">
                      <ProfilePicture className="w-5 h-5"></ProfilePicture>
                      <p>{username}</p>
                    </div>
                  )
                }

                )
              }


            </div>
          ) : ""}
        </ContextMenu.ContextMenuTrigger>
        <ContextMenu.ContextMenuContent className=" bg-[#313244] text-white rounded-md shadow-lg border border-white/10 p-1 z-100" >
          <ContextMenu.Item
            onSelect={() => {

            }}
            className="p-3 hover:bg-white/10 rounded-(--radius) flex items-center gap-2 text-white"
          >
            <Eye className="w-4 h-4 shrink-0" />
            <span>Visualizza dettagli</span>
          </ContextMenu.Item>
        </ContextMenu.ContextMenuContent>
      </ContextMenu.Root>

    </>

  );
}
