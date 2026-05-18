import "../../styles/Canali.css";
import { ServerName } from "./ServerName.tsx";
import { useServerContext } from "../../context/ServerContext.tsx";
import { useRef } from "react";
import { useActiveServerContext } from "@/context/ActiveServerProvider.tsx";
import { Hash, Volume } from "lucide-react";
import { ChannelType } from "@/types.tsx";

function Server() {
  const activeServer = useServerContext();
  const setActiveVC = useActiveServerContext().setActiveVC;
  const channel = useActiveServerContext().activeChannel;
  const setChannel = useActiveServerContext().setActiveChannel;

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

  if (!activeServer) return null;

  return (
    <div 
      ref={containerRef}
      className={`
        bg-[#1e1e2e] font-medium border-r border-slate-700 relative flex-col
        w-full md:w-(--chat-width,300px) md:min-w-[200px] md:max-w-[400px]
        ${channel ? "hidden md:flex" : "flex"}
      `}
    >
      <ServerName />

      <div className="p-3 flex flex-col gap-1 overflow-y-auto h-full">
        {activeServer?.sections.map(s => (
          <div key={s.id}>
            <p className="text-xs font-medium text-gray-500 uppercase tracking-widest px-2 py-1 mt-2">
              {s.name}
            </p>
            {s.channels.map(c => (
              <div
                key={c.id}
                onClick={() => {
                  if (c.type === ChannelType.TEXT) {
                    setChannel(c);
                  } else if (c.type === ChannelType.VOICE) {
                    setActiveVC(c);
                  }
                }}
                className={`flex flex-col px-2 py-1.5 rounded-md cursor-pointer ${
                  channel?.id === c.id ? "bg-white/10" : ""
                } hover:bg-white/10 group`}
              >
                {/* HEADER */}
                <div className="flex items-center gap-2">
                  {c.type === ChannelType.VOICE ? (
                    <Volume className="w-4 h-4" />
                  ) : (
                    <Hash className="w-4 h-4" />
                  )}

                  <span className="text-sm text-gray-400 group-hover:text-gray-300 flex-1">
                    {c.name}
                  </span>

                  {c.description && (
                    <span
                      className={`text-xs text-gray-600 truncate max-w-[140px] ${
                        channel?.id === c.id ? "text-white/60" : ""
                      }`}
                    >
                      {c.description}
                    </span>
                  )}

                  <span className="text-[11px] bg-blue-500/20 text-blue-300 px-1.5 py-0.5 rounded-full shrink-0">
                    {c.type}
                  </span>
                </div>

                {c.type === ChannelType.VOICE && c.users?.length > 0 && (
                  <div className="mt-1 ml-6 flex flex-col gap-0.5">
                    {c.users.map((u: any) => (
                      <div
                        key={u.username}
                        className="text-xs text-gray-500 flex items-center gap-1"
                      >
                        <div className="w-1.5 h-1.5 rounded-full bg-green-400" />
                        {u.username}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        ))}
      </div>
      
      {/* Resizer visibile solo su Desktop */}
      <div
        onMouseDown={startResizing}
        className="hidden md:block absolute right-0 top-0 h-full w-1 cursor-ew-resize transition-colors"
      />
    </div>
  );
}

export default Server;