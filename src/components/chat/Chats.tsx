import { useRef } from "react";
import { useChats } from "../../context/ChatListContext.tsx";
import "../../styles/Chats.css";
import { ChatLoop } from "./ChatLoop.tsx";
import { ChatName } from "./ChatName.tsx";
import { ChatTitle } from "./ChatTitle.tsx";
import { ProfilePicture } from "./ProfilePicture.tsx";

export let width = 300/4;

function Chats() {
  const chats = useChats();
  const containerRef = useRef<HTMLDivElement>(null);

  if (!chats) return null;

  const startResizing = (mouseDownEvent: React.MouseEvent) => {
    const startX = mouseDownEvent.pageX;
    const startWidth = containerRef.current?.offsetWidth || 0;

    const onMouseMove = (mouseMoveEvent: MouseEvent) => {
      if (containerRef.current) {
        const newWidth = startWidth + (mouseMoveEvent.pageX - startX);
        
        
        if (newWidth > 200 && newWidth < 300) {
          containerRef.current.style.width = `${newWidth}px`;
          width=newWidth/4;
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
      className="flex bg-[#1e1e2e] font-medium border-r border-slate-700 relative"
      style={{ width: "300px" }} 
    >
      <div className="flex w-full flex-col overflow-hidden">
         <ChatLoop chats={chats}>
            <ChatTitle style={"bg-[#181825] p-[9px] mb-1 flex cursor-pointer items-center rounded-[9px] gap-[9px] text-[20px]"}>
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