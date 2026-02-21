import { Message } from "@/types";
import { Messaggio } from "../common/Messaggio";
import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useEffect, useRef, useState, Fragment } from "react";

export function ListaMessaggi({ messages }: { messages: Message[] }) {
    const chat = useActiveChatContext().activeChat;
    const [selectedMsgIndex, setSelectedMsgIndex] = useState<number | null>(null);
    const messagesRef = useRef<null | HTMLDivElement>(null);

    const scrollToBottom = () => {
        if (messagesRef.current) {
            messagesRef.current.scrollTo({
                top: messagesRef.current.scrollHeight,
                behavior: "smooth",
            });
        }
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    if (!chat) return null;
    if(!messages)  return;

    return (
        <div
            className="flex-1 mb-3 overflow-y-auto flex flex-col-reverse messages-scrollbar"
            ref={messagesRef}
        >
            {[...messages].reverse().map((msg, index, reversedArray) => {
                const currentDate = msg.sentAt ? new Date(msg.sentAt).toLocaleDateString() : null;
            
                const prevMsg = reversedArray[index + 1];
                const prevDate = prevMsg?.sentAt ? new Date(prevMsg.sentAt).toLocaleDateString() : null;

                const showDateSeparator = currentDate && currentDate !== prevDate;

                return (
                    <Fragment key={msg.messageId}>
                        <Messaggio
                            messageType={"chat"}
                            id={chat.id}
                            msg={msg}
                            style={selectedMsgIndex === index ? "bg-white/10 mr-4 rounded-md" : ""}
                            onCloseMenu={() => { setSelectedMsgIndex(null) }}
                            onTrigger={() => { setSelectedMsgIndex(index) }}
                        />
                        
                        {showDateSeparator && (
                            <div className="text-center my-6 flex items-center justify-center">
                                <span className="bg-zinc-800 text-zinc-400 text-xs px-3 py-1 rounded-full border border-white/5">
                                    {currentDate}
                                </span>
                            </div>
                        )}
                    </Fragment>
                );
            })}
        </div>
    );
}