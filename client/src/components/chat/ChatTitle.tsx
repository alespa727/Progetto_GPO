import { useActiveChatContext } from "../../context/ActiveChatProvider";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useChannelContext } from "../../context/ChannelContext";
import { useChatContext } from "../../context/ChatContext";
import { useSectionContext } from "../../context/SezioneContext";
import { ChannelType } from "../../types";


export interface ChatTitleProps {
  style: string;
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ChatTitle({style, ...props} : ChatTitleProps) {
    const chat = useChatContext();
    const setActiveChat= useActiveChatContext().setActiveChat;
    return (
      <>
      <div onClick={()=>setActiveChat(chat)} className={style}>
        {
          props.children
        }
      </div>
      </>
    );
}