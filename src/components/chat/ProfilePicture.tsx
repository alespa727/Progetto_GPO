import React from "react";
import { useChatContext } from "@/context/ChatContext";

export const ProfilePicture = React.memo(function ProfilePicture({ 
  className, 
  src = "" 
}: { 
  className: string, 
  src?: string 
}) {
  const chat = useChatContext();
  
  const resolvedSrc = React.useMemo(() => {
    if (!chat) return src || "/placeholder.png";
    if (chat.friend.path === "default") return "/placeholder.png";
    if (chat.friend.path) return chat.friend.path.replace("http://localhost:8080/", "");
    return src || "/placeholder.png";
  }, [chat?.friend?.path, src]); // ← dipende SOLO dal path, non da lastMessage

  return (
    <div className={`rounded-full overflow-hidden ${className}`}>
      <img
        src={resolvedSrc}
        alt=""
        className="w-full h-full object-cover"
      />
    </div>
  );
});