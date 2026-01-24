import { useChats } from "../../context/ChatListContext.tsx";
import "../../styles/Chats.css";
import { ChatLoop } from "./ChatLoop.tsx";
import { ChatName } from "./ChatName.tsx";
import { ChatTitle } from "./ChatTitle.tsx";
import { ProfilePicture } from "./ProfilePicture.tsx";

function Chats() {
  const chats = useChats();
  if(!chats) return;

  return (
    <div className="bg-[#1e1e2e] min-w-50 max-w-60 font-medium ">
          <ChatLoop chats={chats}>
            <ChatTitle style={"bg-[#181825] p-[9px] mb-1 flex cursor-pointer items-center rounded-[9px] gap-[9px] text-[20px]"}>
              <ProfilePicture className="bg-black w-10 h-10 object-cover"></ProfilePicture>
              <ChatName></ChatName>
            </ChatTitle>
          </ChatLoop>
    </div>
  );
}

export default Chats;
