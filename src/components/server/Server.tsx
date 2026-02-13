import "../../styles/Canali.css";
import { SectionLoop } from "./SezioneLoop.tsx";
import { SectionName } from "./SectionName.tsx";
import { ChannelLoop } from "./ChannelLoop.tsx";
import { ChannelName } from "./ChannelName.tsx";
import { ServerName } from "./ServerName.tsx";
import { useServerContext } from "../../context/ServerContext.tsx";
import { useActiveChatContext } from "@/context/ActiveChatProvider.tsx";
import { useRef } from "react";
import { useActiveServerContext } from "@/context/ActiveServerProvider.tsx";

function Server() {

  const activeServer = useServerContext();

  const channel = useActiveServerContext().activeChannel;

  const containerRef = useRef<HTMLDivElement>(null);

  const startResizing = (mouseDownEvent: React.MouseEvent) => {
    const startX = mouseDownEvent.pageX;
    const startWidth = containerRef.current?.offsetWidth || 0;

    const onMouseMove = (mouseMoveEvent: MouseEvent) => {
      if (containerRef.current) {
        const newWidth = startWidth + (mouseMoveEvent.pageX - startX);


        if (newWidth > 200 && newWidth < 400) {
          containerRef.current.style.width = `${newWidth}px`;
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


  if (!activeServer) return;

  return (

    <div ref={containerRef} 
    className={ (channel ? "hidden md:flex flex-col" : "flex flex-col") +
    " bg-[#1e1e2e] font-medium border-r border-slate-700 relative " +
    "w-full md:w-(--chat-width,300px) md:min-w-[200px] md:max-w-[400px]"}
    >
      <ServerName></ServerName>

      <SectionLoop>
        <SectionName onClick={() => { }}></SectionName>
        <ChannelLoop>
          <ChannelName></ChannelName>
        </ChannelLoop>
      </SectionLoop>
      <div 
        onMouseDown={startResizing}
        className="absolute right-0  top-0 h-full w-1 cursor-ew-resize  transition-colors"
      />
    </div>
  );
}
export default Server;
