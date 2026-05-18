import { Message } from "@/types";
import { Messaggio } from "../common/Messaggio";
import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useEffect, useRef, useState, Fragment, useMemo } from "react";
import { MessaggioSkeleton } from "../common/SkeletonMessage";
import { ProfilePicture } from "./ProfilePicture";
import { useFriends } from "@/context/FriendContext";

export function ListaMessaggi({ messages, skeleton, empty }: { messages: React.RefObject<Message[]>, skeleton?: boolean, empty?: boolean }) {
    const chat = useActiveChatContext().activeChat;
    const friends = useFriends();
    const [selectedMsgIndex, setSelectedMsgIndex] = useState<number | null>(null);
    const messagesRef = useRef<null | HTMLDivElement>(null);

    const scrollToBottom = () => {
        if (!messagesRef.current) return;

        const images = messagesRef.current.querySelectorAll("img");

        if (images.length === 0) {
            messagesRef.current.scrollTo({ top: messagesRef.current.scrollHeight, behavior: "smooth" });
            return;
        }

        const promises = Array.from(images).map(img =>
            img.complete ? Promise.resolve() : new Promise(res => {
                img.addEventListener("load", res, { once: true });
                img.addEventListener("error", res, { once: true });
            })
        );

        Promise.all(promises).then(() => {
            messagesRef.current?.scrollTo({ top: messagesRef.current.scrollHeight, behavior: "smooth" });
        });
    };

    const items = useMemo(() =>
        Array.from({ length: 20 }, (_, i) => ({
            showImage: Math.random() < 0.33,
        }))
        , []);

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    if (!chat) return null;
    if (!messages) return;

    return (
        <div
            className="flex-1 mb-3 overflow-y-auto flex flex-col-reverse messages-scrollbar"
            ref={messagesRef}
        >


            {skeleton ? (
                <div className="flex flex-col gap-1">
                    {items.map((item, i) => (
                        <MessaggioSkeleton key={i} showImage={item.showImage} />
                    ))}
                </div>
            ) : (
                [...messages.current].reverse().map((msg, index, reversedArray) => {
                    const currentDate = msg.sentAt ? new Date(msg.sentAt).toLocaleDateString() : null;
                    const prevMsg = reversedArray[index + 1];
                    const prevDate = prevMsg?.sentAt ? new Date(prevMsg.sentAt).toLocaleDateString() : null;
                    const showDateSeparator = currentDate && currentDate !== prevDate;
                    return (
                        <Fragment key={msg.messageId}>
                            <Messaggio
                                messages={messages}
                                obj={chat}
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
                })
            )}


            <div className="px-6 pr-4 py-8 flex flex-col items-center gap-4 border-b border-white/10">
                <ProfilePicture
                    className="w-24 aspect-square ring-4 ring-white/20 shadow-xl"
                    src={chat.friend.path}
                />
                <div className="flex flex-col items-center gap-1 text-center">
                    <h2 className="text-3xl font-bold text-white">{chat.friend.username}</h2>
                    <span>{chat.friend.description ? chat.friend.description : ""}</span>
                    <span className="text-white/50 text-sm max-w-xs">
                        Questo è l'inizio della tua cronologia con <span className="text-white/70 font-medium">{chat.friend.username}</span>
                    </span>
                </div>
            </div>


        </div>
    );
}