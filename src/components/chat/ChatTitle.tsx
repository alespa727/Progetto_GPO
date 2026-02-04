import { motion } from "framer-motion";
import { useActiveChatContext } from "../../context/ActiveChatProvider";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useChannelContext } from "../../context/ChannelContext";
import { useChatContext } from "../../context/ChatContext";
import { useSectionContext } from "../../context/SezioneContext";
import { ChannelType } from "../../types";


export interface ChatTitleProps {
  style: string;
  children: React.ReactNode;
}

export function ChatTitle({style, ...props} : ChatTitleProps) {
    const chat = useChatContext();
    const setActiveChat= useActiveChatContext().setActiveChat;
    return (
      <>
      <motion.div initial={{ scale: 0.9, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            exit={{ scale: 0.9, opacity: 0 }}
            transition={{ type: "spring", stiffness: 260, damping: 25 }}
            whileTap={{ scale: 0.98 }} onClick={()=>setActiveChat(chat)} className={style}>
        {
          props.children
        }
      </motion.div>
      </>
    );
}