import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { useSocket } from "./SocketProvider";
import Modal from "@/components/common/Modal";
import axios from "axios";
import { endpoint } from "@/types";
import { useAccount } from "./UserProvider";
import { useChats } from "./ChatListContext";
import { useActiveRoomContext } from "./RoomContext";
import { useActiveChatContext } from "./ActiveChatProvider";

interface IncomingCallContextType {
    state: boolean;
    setState: (state: boolean) => void;
}

const IncomingCallContext = createContext<IncomingCallContextType | null>(null);

export const IncomingCallStatusProvider = ({
    children,
}: {
    children: ReactNode;
}) => {
    const account = useAccount();
    const chats = useChats()?.chats;

    const url = "wss://progettogpo-dfna4rrr.livekit.cloud";
    const { setUrl, setToken, setTitle } = useActiveRoomContext()

    const [state, setState] = useState(false);
    const setActiveChat = useActiveChatContext().setActiveChat;
    const [caller, setCaller] = useState("");
    const socket = useSocket();

    useEffect(() => {
        if (!socket) return;

        const handleIncomingCall = (data: { caller: string }) => {
            console.log("Chiamata in arrivo da:", data.caller);
            setCaller(data.caller)
            setState(true)
        }
        socket.on("incomingCall", handleIncomingCall);
        return () => {
            socket.off("incomingCall", handleIncomingCall);
        };
    }, [socket]);

    const handleAccept = async () => {
        const chat = chats?.find((c) => c.friend.username === caller);
        if (!chat) {
            setState(false)
            setCaller("");
            return;
        }
        const res = await axios.post(endpoint + "/token", {
            identity: account?.username,
            roomName: "chat_" + chat.id
        });
        setUrl(url);
        setToken(res.data.token);
        setTitle(chat.friend.username);
        setState(false);
        setActiveChat(chat);
        setCaller("");
    };

    const handleReject = ()=>{
        setState(false);
        setCaller("");
    }

    return (
        <IncomingCallContext.Provider value={{ state, setState }}>
            {state && (
                <Modal open={state} onOpenChange={setState} className="w-[360px]">
                    <div className="bg-(--base) flex flex-col overflow-hidden">

                        {/* Top */}
                        <div className="bg-(--mantle) px-6 py-8 flex items-center gap-4">
                            <img
                                src="/placeholder.png"
                                className="rounded-full w-14 h-14 object-cover"
                            />
                            <div className="flex flex-col gap-0.5">
                                <span className="text-white/40 text-xs tracking-wider uppercase">Chiamata in arrivo</span>
                                <h2 className="text-white text-lg font-semibold">{caller}</h2>
                            </div>
                        </div>

                        {/* Divider animato */}
                        <div className="h-[2px] bg-gradient-to-r from-transparent via-indigo-500/40 to-transparent" />

                        {/* Azioni */}
                        <div className="px-6 py-5 flex gap-3">
                            <button onClick={handleReject} className="flex-1 py-2.5 rounded-(--radius) bg-white/5 hover:bg-red-500/20 hover:text-red-400 text-white/50 text-sm transition-colors">
                                Rifiuta
                            </button>
                            <button  onClick={handleAccept} className="flex-1 py-2.5 rounded-(--radius) bg-indigo-500/20 hover:bg-indigo-500 border border-indigo-500/30 text-indigo-300 hover:text-white text-sm transition-colors">
                                Accetta
                            </button>
                        </div>

                    </div>
                </Modal>
            )}


            {children}
        </IncomingCallContext.Provider>
    );
};

export const useIncomingCallContext = () => {
    const context = useContext(IncomingCallContext);

    if (!context) {
        throw new Error(
            "useIncomingCallContext deve essere usato dentro IncomingCallStatusProvider"
        );
    }

    return context;
};