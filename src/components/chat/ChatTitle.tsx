import { motion } from "framer-motion";
import { useActiveChatContext } from "../../context/ActiveChatProvider";
import { useChatContext } from "../../context/ChatContext";

export interface ChatTitleProps {
  style: string;
  children: React.ReactNode;
}

export function ChatTitle({ style, ...props }: ChatTitleProps) {
  const chat = useChatContext();
  const setActiveChat = useActiveChatContext().setActiveChat;

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: 8, scale: 0.97 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, y: -8, scale: 0.97 }}
      transition={{
        type: "spring",
        stiffness: 260,
        damping: 20,
        mass: 0.8,
      }}
      whileHover={{ scale: 1 }}
      whileTap={{ scale: 0.98 }}
      onClick={() => setActiveChat(chat)}
      className={`
        bg-white/5
        hover:bg-white/10
        transition-colors
        duration-200
        p-4
        mb-1
        flex
        cursor-pointer
        items-center
        rounded-[var(--radius)]
        gap-[9px]
        text-[20px]
        active:brightness-95
      `}
    >
      {props.children}
    </motion.div>
  );
}