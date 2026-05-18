import * as ContextMenu from "@radix-ui/react-context-menu";
import { Trash, Pencil, Eye, Clipboard, DownloadIcon, FileIcon, X, ArrowBigLeft } from "lucide-react";
import { Channel, Chat, ClientHttp, Message, Server } from "../../types";
import { useSocket } from "../../context/SocketProvider";
import { useEffect, useState } from "react";
import axios from "axios";
import "../../styles/zoom.css";
import MyCustomZoom from "./CustomZoom";
import { motion } from "framer-motion";
import { useAccount } from "@/context/UserProvider";
import { useMessageContext } from "@/context/MessageContext";
import Modal from "./Modal";
import { useFriends } from "@/context/FriendContext";

type MessaggioProps = {
    messages: React.RefObject<Message[]>;
    msg: Message;
    obj: Chat | Channel;
    style: string;
    onCloseMenu: () => void;
    onTrigger: () => void;
};

export function Messaggio({ messages, msg, obj, style, onCloseMenu, onTrigger }: MessaggioProps) {
    const socket = useSocket();
    const account = useAccount();
    const friends = useFriends();

    const [isBeingModified, setModifying] = useState<boolean>(false);
    const [draft, setDraft] = useState(msg.message);
    const [hoverButton, setHoverButton] = useState(false);
    const [isDownloading, setDownloading] = useState(false);
    const [beingDeleted, setBeingDeleted] = useState(false);
    const [deleted, setDeleted] = useState(false);
    const [profileOpen, setProfileOpen] = useState(false);

    const [copied, setCopied] = useState(false);

    const copy = async () => {

        await navigator.clipboard.writeText(msg.message);

        setCopied(true);
        setTimeout(() => setCopied(false), 2000);
    };
    if (!friends) return;
    const otherUserAccount = friends.find(f => f.username === msg.username);

    const displayAccount = otherUserAccount
        ?? (msg.username === account?.username ? account : null);

    const resolvedAccount = displayAccount ?? {
        username: msg.username,
        path: null,
        description: null,
    };

    let username = msg.username;
    let message = msg;

    if (!message) return;

    if (username.length > 13) {
        username = username.slice(0, 13) + "...";
    }


    const timeString = (sentAt: any) => {
        const date = new Date(sentAt);
        if (isNaN(date.getTime())) return "";
        return (date.getHours() <= 9 ? "0" : "") + date.getHours() + ":" +
            (date.getMinutes() <= 9 ? "0" : "") + date.getMinutes();
    };

    if (isBeingModified) return (
        <div className="px-4 mr-4 py-1 hover:bg-white/5 flex items-center rounded-lg">
            <div className="flex gap-3 items-center text-sm w-full">
                <span className="text-white/30 text-xs font-mono">{msg.sentAt ? timeString(msg.sentAt) : ""}</span>
                <span className="text-white/70 font-medium">{username}</span>
                <input
                    className="flex-1 bg-white/10 border border-white/20 rounded-lg px-3 py-1.5 text-white outline-none focus:ring-2 focus:ring-white/30 transition-all"
                    value={draft}
                    onChange={(e) => setDraft(e.target.value)}
                    onKeyDown={async (e) => {

                        if (e.key === 'Enter') {
                            if (obj instanceof Chat) {
                                const res = await ClientHttp.modifyMessage(obj.id, message.messageId, draft);
                                if (res) {
                                    const target = messages.current.find(m => m.messageId === msg.messageId);
                                    if (target) target.message = draft;
                                    setModifying(false);
                                    socket?.emit("modifiedMessageChat", { chatId: obj.id, messageId: msg.messageId, message: draft });
                                }
                            }else if(obj instanceof Server){
                                
                            }


                        }
                    }}
                />
                <ArrowBigLeft></ArrowBigLeft>
            </div>
        </div>
    );

    if (deleted) {
        return <></>;
    }


    return (

        <ContextMenu.Root>

            <ContextMenu.Trigger onContextMenu={onTrigger} className={style}>
                <motion.div
                    className={"px-4 mr-2 py-1 rounded-lg flex items-start group " + (!hoverButton ? "hover:bg-white/5" : "")}
                    initial={{ scale: 0.98, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    exit={{ scale: 0.95, opacity: 0 }}
                    transition={{ type: "spring", stiffness: 300, damping: 25 }}
                    whileTap={{ scale: 0.99 }}
                >
                    <div className="flex flex-col gap-1 w-full text-[14px]">
                        <div className={`flex items-baseline gap-2 ${!message.sent || beingDeleted ? "opacity-50" : ""}`}>
                            <span className="text-white/30 text-xs font-mono shrink-0">
                                {msg.sentAt ? timeString(msg.sentAt) : ""}
                            </span>
                            <span className="font-semibold text-indigo-300 hover:underline cursor-pointer shrink-0">
                                {username}
                            </span>
                            <span className="text-white/90 break-all leading-relaxed">
                                {msg.message}
                            </span>
                            <Modal open={profileOpen} onOpenChange={setProfileOpen} className="w-[500px]">

                                <div className="bg-(--mantle) w-full h-24 relative">
                                    <div className="absolute -bottom-10 left-6">
                                        <img
                                            src={
                                                resolvedAccount.path ? resolvedAccount.path.replace("http://localhost:8080", "") : "/placeholder.png"
                                            }
                                            className="rounded-full w-20 h-20 object-cover border-4 border-(--base)"
                                        />
                                    </div>
                                </div>

                                <div className="bg-(--base) pt-14 pb-6 px-6 w-full flex flex-col gap-3">
                                    <h2 className="text-white text-xl font-bold">{resolvedAccount.username}</h2>

                                    <div className="bg-(--mantle) rounded-(--radius) p-4 flex flex-col gap-1">
                                        <span className="text-white/40 text-xs uppercase font-semibold tracking-wider">Chi sono</span>
                                        <p className="text-white/80 text-sm">{resolvedAccount.description ?? "Nessuna descrizione."}</p>
                                    </div>
                                </div>
                            </Modal>
                            {message.message !== "" && message.fail && (
                                <span className="bg-red-500/30 border border-red-500/50 text-red-300 text-xs px-2 py-0.5 rounded-full shrink-0">
                                    Non inviato
                                </span>
                            )}
                            {copied && (
                                <span className="bg-green-500/30 border border-green-500/50 text-green-300 text-xs px-2 py-0.5 rounded-full shrink-0">
                                    Messaggio copiato
                                </span>
                            )}
                        </div>
                        {message.attachments.map(a => {

                            const isImage =
                                a.extension === ".jpeg" ||
                                a.extension === ".png" ||
                                a.extension === ".jpg";

                            const isUploading = !message.sent;

                            if (isImage) {
                                return (
                                    <div key={a.id} className="relative">
                                        <MyCustomZoom
                                            src={`/files/${a.filename}${a.extension}`}
                                            attachment={a}
                                            className="rounded-lg"
                                            alt="img"
                                        />

                                        {!message.sent && (
                                            <div className="absolute inset-0 bg-black/50 flex items-center justify-center rounded-xl">
                                                <div className="w-8 h-8 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                                            </div>
                                        )}
                                    </div>
                                );
                            }

                            return (
                                <motion.div
                                    key={a.id}
                                    className={`flex items-center gap-3 border rounded-xl p-3 w-[50%] max-w-xs transition-all duration-150
                ${isUploading
                                            ? "bg-white/5 border-white/10 opacity-70"
                                            : "bg-white/5 hover:bg-white/10 border-white/10"
                                        }`}
                                    initial={{ scale: 0.95, opacity: 0 }}
                                    animate={{ scale: 1, opacity: 1 }}
                                    whileTap={{ scale: 0.98 }}
                                >
                                    {/* ICON */}
                                    <div className="w-9 h-9 flex items-center justify-center">
                                        {message.fail ?
                                            (<><X className="w-5 h-5 text-red/60"></X></>)
                                            :
                                            (isUploading ?
                                                (
                                                    <div className="w-5 h-5 border-2 border-white/20 border-t-white rounded-full animate-spin" />
                                                ) :
                                                (
                                                    <FileIcon className="w-5 h-5 text-white/60" />
                                                )
                                            )}

                                    </div>

                                    {/* TEXT */}
                                    <div className="flex flex-col flex-1 min-w-0">
                                        <span className="text-white/90 text-sm font-medium truncate">
                                            {a.originalname + a.extension}
                                        </span>

                                        <span className="text-white/40 text-xs">
                                            {isUploading ? "Invio in corso..." : "File"}
                                        </span>
                                    </div>

                                    {/* DOWNLOAD */}
                                    {!isUploading && (
                                        <button
                                            className="p-2 rounded-lg hover:bg-white/10 active:scale-90 transition-all duration-150 shrink-0"
                                        >
                                            {
                                                !isDownloading ? (<DownloadIcon className="w-4 h-4 text-white/60" onClick={() => {
                                                    const downloadFile = async (url: string, filename: string) => {
                                                        setDownloading(true);
                                                        const response = await axios.get(url, { responseType: "blob" });
                                                        const blobUrl = URL.createObjectURL(response.data);

                                                        const a = document.createElement("a");
                                                        a.href = blobUrl;
                                                        a.download = filename;
                                                        a.click();

                                                        URL.revokeObjectURL(blobUrl);
                                                        setDownloading(false);
                                                    };
                                                    downloadFile(`/files/${a.filename}${a.extension}`, `${a.originalname}${a.extension}`);

                                                }} />) : (
                                                    <div className="w-5 h-5 border-2 border-white/20 border-t-white rounded-full animate-spin" />
                                                )
                                            }

                                        </button>
                                    )}

                                    {message.fail && (
                                        <span className="bg-red-500/30 border border-red-500/50 text-red-300 text-xs px-2 py-0.5 rounded-full shrink-0">
                                            Non inviato
                                        </span>
                                    )}
                                </motion.div>
                            );
                        })}
                    </div>
                </motion.div>
            </ContextMenu.Trigger>

            <ContextMenu.Content className="bg-[#1e1e2e] text-white rounded-xl shadow-2xl border border-white/10 p-1.5 z-100 min-w-[180px]">
                {[
                    {
                        icon: <Trash className="w-4 h-4" />, label: "Elimina", action: async () => {

                            setBeingDeleted(true);

                            if(obj instanceof Chat){
                                const res = await ClientHttp.deleteMessage(obj.id, message.messageId);
                                if (res) {
                                    messages.current = messages.current.filter(m => m.messageId !== msg.messageId);
                                    setBeingDeleted(false);
                                    setDeleted(true);
                                    socket?.emit("deletedMessageChat", { chatId: obj.id, messageId: msg.messageId });
                                }
                            }
                            

                            onCloseMenu();
                        }, danger: true, ownerOnly: true
                    },
                    {
                        icon: <Pencil className="w-4 h-4" />, label: "Modifica", action: async () => {
                            setModifying(true);
                            onCloseMenu();
                        }, ownerOnly: true
                    },
                    {
                        icon: <Eye className="w-4 h-4" />, label: "Visualizza profilo", action: () => {
                            setProfileOpen(true);
                            onCloseMenu()
                        }
                    },
                    {
                        icon: <Clipboard className="w-4 h-4" />, label: "Copia messaggio", action: () => {
                            copy()
                            onCloseMenu()
                        }
                    },
                ]
                    .filter(({ ownerOnly }) => !ownerOnly || message.username === account?.username)
                    .map(({ icon, label, action, danger }) => (
                        <ContextMenu.Item
                            key={label}
                            onSelect={action}
                            className={`px-3 py-2 rounded-lg flex items-center gap-2.5 text-sm cursor-pointer transition-colors ${danger ? "hover:bg-red-500/20 text-red-400" : "hover:bg-white/10 text-white/90"}`}
                        >
                            {icon}
                            <span>{label}</span>
                        </ContextMenu.Item>
                    ))}
            </ContextMenu.Content>
        </ContextMenu.Root>
    );
}