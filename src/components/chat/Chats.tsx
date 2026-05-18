import { useEffect, useRef, useState } from "react";
import { useChats } from "../../context/ChatListContext.tsx";
import "../../styles/Chats.css";
import { ChatLoop } from "./ChatLoop.tsx";
import { ChatName } from "./ChatName.tsx";
import { ChatTitle } from "./ChatTitle.tsx";
import { ProfilePicture } from "./ProfilePicture.tsx";
import { useActiveChatContext } from "@/context/ActiveChatProvider.tsx";
import { AddFriendButton } from "./AggiungiAmicoButton.tsx";
import { SearchBar } from "./SearchBar.tsx";

export let width = 300 / 4;

function Chats() {
  const chats = useChats()?.chats;
  const containerRef = useRef<HTMLDivElement>(null);
  const {activeChat, setActiveChat} = useActiveChatContext();

  const [filter, setFilter] = useState<string>("");

  if (!chats) return null;

  useEffect(()=>{
    setFilter("");
  }, [activeChat])

  const startResizing = (mouseDownEvent: React.MouseEvent) => {
    const startX = mouseDownEvent.pageX;
    const startWidth = containerRef.current?.offsetWidth || 0;

    const onMouseMove = (mouseMoveEvent: MouseEvent) => {
      if (containerRef.current) {
        const newWidth = startWidth + (mouseMoveEvent.pageX - startX);


        if (newWidth > 200 && newWidth < 400) {
          containerRef.current.style.width = `${newWidth}px`;
          width = newWidth / 4;
          containerRef.current.style.flex = "none";
          document.documentElement.style.setProperty('--chat-width', `${newWidth}px`);
        }
      }
    };

    const onMouseUp = () => {
      document.removeEventListener("mousemove", onMouseMove);
      document.removeEventListener("mouseup", onMouseUp);
      document.body.style.cursor = "default";
    };

    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", onMouseUp);

    document.body.style.cursor = "ew-resize";
  };

  return (
    <div
      ref={containerRef}
      className={
        (activeChat ? "hidden md:flex" : "flex") +
        " bg-(--background) font-medium border-r border-slate-700 relative " +
        "w-full md:w-(--chat-width,300px) md:min-w-[200px] md:max-w-[400px]"
      }>


      <div className="flex w-full flex-col overflow-hidden">
        <div className="flex w-full">
          <SearchBar onClick={()=>{}} filter={filter} setFilter={setFilter}></SearchBar>
          <AddFriendButton></AddFriendButton>
        </div>
        
        <ChatLoop filter={filter} chats={chats}>
          <ChatTitle style={" bg-(--surface0) hover:bg-(--surface1) p-3 mb-1 flex cursor-pointer items-center rounded-(--radius) gap-[9px] text-[20px]"}>
            <ProfilePicture className="bg-black w-10 h-10 object-cover" />
            <ChatName />
          </ChatTitle>
        </ChatLoop>
      </div>
      <div
        onMouseDown={startResizing}
        className="absolute right-0 top-0 h-full w-1 cursor-ew-resize  transition-colors"
      />
    </div>
  );
}

export default Chats;