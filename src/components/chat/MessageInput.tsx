import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useSocket } from "@/context/SocketProvider";
import { useAccount } from "@/context/UserProvider";
import { endpoint, Message } from "@/types";
import axios, { HttpStatusCode } from "axios";
import { Plus } from "lucide-react";
import { i } from "motion/react-client";
import { useState } from "react";

export function MessageInput({ addMessage, lastMessage }: { addMessage: (message: Message) => void, lastMessage: Message | undefined }) {
    const socket = useSocket();
    const account = useAccount();
    const [nuovoTesto, setNuovoTesto] = useState<string>("");
    const chat = useActiveChatContext().activeChat;

    if (!chat || !(account && account !== undefined)) return;

    const sendMessage = () => {
        if (!socket) return;
        if (!nuovoTesto) return;
        if (nuovoTesto.trim() === "") return;

        const username: string = account.username;
        let id = chat ? chat.id : null;
        
        let message = new Message(lastMessage ? lastMessage.messageId+1: 0, username, nuovoTesto);
        let type = "chat";
        const send = async () => {
            try {
                const res = await axios.post(
                    endpoint + "/services/chats/" + id + "/message",
                    {
                        message: nuovoTesto,
                    },
                    {
                        withCredentials: true,
                    }

                );
                
                if (res.status === HttpStatusCode.Created) {
                    socket.emit("sendMessage", { id, type, message });
                } else {
                    if(lastMessage?.messageId)
                        addMessage(new Message(lastMessage?.messageId, account.username, message.message, [], new Date(), false))
                }
                
            } catch (error) {
                console.log(error)
                addMessage(new Message(-1, account.username, message.message, [], new Date(), false))
               
            }finally{
                setNuovoTesto("");
            }

            
        };
        send();
    }

    return (
        <>
            <div className="mt-1/2 p-1.5">
                <div className="flex items-center gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
                    <Plus className="w-6 h-6 text-white" />
                    <input
                        value={nuovoTesto || ""}
                        className="flex-1 bg-transparent text-[14px] focus:outline-none"
                        onChange={(e) => setNuovoTesto(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") sendMessage();
                        }}
                        placeholder="Scrivi qui..."
                    />
                </div>
            </div>
        </>
    )
}