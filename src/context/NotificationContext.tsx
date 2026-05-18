import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { useSocket } from "./SocketProvider";
import Modal from "@/components/common/Modal";
import axios from "axios";
import { Account, endpoint } from "@/types";
import { useAccount } from "./UserProvider";
import { useChats } from "./ChatListContext";
import { useActiveRoomContext } from "./RoomContext";
import { useActiveChatContext } from "./ActiveChatProvider";
import { data } from "framer-motion/client";


const NotificationContext = createContext<null>(null);

interface Notification {
  label: string;
  from: Account;
  text: string;
}
 
export const NotificationHandler = ({
    children,
}: {
    children: ReactNode;
}) => {
    const account = useAccount();
    const chats = useChats()?.chats;

    const [notification, setNotification] = useState<Notification | null>(null)

    const socket = useSocket();

    useEffect(()=>{

        const handleNotification = async (data: any)=>{
            setNotification(data);

            setTimeout(()=>{
                setNotification(null);
            }, 3000)
            console.log(data);
        };
        socket?.on("notification", handleNotification)

        return ()=>{
            socket?.off("notification", handleNotification)
        }
    })


    return (
        <>
            <Notification notification={notification}></Notification>
          {children}
        </>
    );
};


import { motion, AnimatePresence } from "framer-motion"
import { createPortal } from "react-dom"
import { X } from "lucide-react"

export default function Notification({
    notification,
}: {
    notification: Notification | null
  
}) {

    return (
        <>
            {typeof window !== "undefined" &&
                createPortal(
                    <AnimatePresence>
                        {notification && (
                            <motion.div
                                className="fixed inset-0  z-[999999] flex items-end justify-end bg-black/20"
                                initial={{ opacity: 0 }}
                                animate={{ opacity: 1 }}
                                exit={{ opacity: 0 }}
                            >

                                <motion.div
                                    className={`rounded-(--radius) mb-18 mr-5 overflow-hidden p-4 bg-black`}
                                    initial={{ scale: 0.8 }}
                                    animate={{ scale: 1 }}
                                    exit={{ scale: 0.9, rotate: 10 }}
                                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    {notification.label + " " + notification.text}
                                </motion.div>
                            </motion.div>
                        )}
                    </AnimatePresence>,
                    document.body
                )}
        </>
    )
}