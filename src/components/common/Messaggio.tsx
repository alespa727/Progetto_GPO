import * as ContextMenu from "@radix-ui/react-context-menu";
import { Trash, Pencil, Eye, Clipboard, DownloadIcon } from "lucide-react";
import { Message } from "../../types";
import { useSocket } from "../../context/SocketProvider";
import { useState } from "react";
import axios from "axios";
import "../../styles/zoom.css";
import MyCustomZoom from "./CustomZoom";
import { motion } from "framer-motion";

type MessaggioProps = {
    msg: Message,
    messageType: string,
    id: number,
    style: string,
    onCloseMenu: () => void,
    onTrigger: () => void
}

export function Messaggio({ msg, messageType, id, style, onCloseMenu, onTrigger }: MessaggioProps) {
    const socket = useSocket();
    const [isBeingModified, setModifying] = useState<boolean>(false);
    const [draft, setDraft] = useState(msg.message);
    const [hoverButton, setHoverButton] = useState(false);
    let username = msg.username;
    let message = msg;

    if (!message) return;

    if (username.length > 9) {
        username = username.slice(0, 9) + "...";
    }

    if (isBeingModified) return (
        <div
            className={"rounded-r-(--radius) px-4 mr-4 hover:bg-white/10 flex items-center"}
        >
            <div className="gap-2 flex flex-col p-1 text-[15px]">
                <div className="pt-1/2 gap-2 flex">
                    <span>
                        {msg.sentAt
                            ? (() => {
                                const date = new Date(msg.sentAt);
                                return (date.getHours() <= 9 ? "0" : "") + date.getHours() + ":" +
                                    (date.getMinutes() <= 9 ? "0" : "") + date.getMinutes();
                            })()
                            : ""}
                    </span>
                    <span className="hover:font-bold hover:underline">
                        {username}
                    </span>
                    <input
                        className="p-3 break-all"
                        value={draft}
                        onChange={(e) => setDraft(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                setModifying(false)
                            }
                        }}
                    />
                </div>

            </div>
        </div>
    );

    return (
        <ContextMenu.Root >
            <ContextMenu.Trigger onContextMenu={onTrigger} className={style}>
                <motion.div
                    className={
                        "rounded-r-[var(--radius)] px-6 mr-4 flex items-center justify-center " +
                        (!hoverButton ? "hover:bg-white/10" : "")
                    }
                    initial={{ scale: 0.98, opacity: 0.5 }}
                    animate={{ scale: 1, opacity: 1 }}
                    exit={{ scale: 0.9, opacity: 0 }}
                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                    whileTap={{ scale: 0.99 }}
                    onClick={() => {
                        console.log("Div clicked"); 
                    }}
                >
                    <div className="gap-3 w-full  flex flex-col p-1 text-[15px]">
                        <div className={`pt-1/2 gap-2 flex  ${!message.sent ? "text-gray-400" : "text-white"}`}>
                            <span>
                                {msg.sentAt
                                    ? (() => {
                                        const date = new Date(msg.sentAt);
                                        return (date.getHours() <= 9 ? "0" : "") + date.getHours() + ":" +
                                            (date.getMinutes() <= 9 ? "0" : "") + date.getMinutes();
                                    })()
                                    : ""}
                            </span>
                            <span className={"hover:font-bold hover:underline"}>
                                {username}
                            </span>
                            <span
                                className={`break-all`}
                            >
                                {msg.message}
                            </span>

                            {
                                message.fail && (
                                    <span className=" bg-red-500/50 p-1 text-white px-2 rounded">
                                        Messaggio non inviato
                                    </span>
                                )
                            }
                        </div>

                        {message.attachments.map(a => {
                            if (a.extension === ".jpeg" || a.extension === ".png" || a.extension === ".jpg") {
                                return (
                                    <MyCustomZoom key={a.id} src={`/files/${a.filename}${a.extension}`} attachment={a}
                                        alt="img">
                                    </MyCustomZoom>
                                );
                            } else {
                                return (
                                    <motion.div
                                        key={a.id}
                                        onMouseEnter={() => setHoverButton(true)}
                                        onMouseLeave={() => setHoverButton(false)}
                                        className="rounded-[var(--radius)] text-white transition-all duration-100 hover:scale-101 flex relative items-center border bg-white/5 border-transparent hover:bg-white/20 p-6"
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        exit={{ scale: 0.9, opacity: 0 }}
                                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                        whileTap={{ scale: 0.98}}
                                        onClick={() => {
                                            console.log("Card clicked:", a.id);
                                            
                                        }}
                                    >

                                        <span className="text-xl w-full h-full justify-center">
                                            {a.filename + a.extension}
                                        </span>

                                        <button
                                            onClick={async () => {
                                                const isElectron = !!window.electronAPI?.getSources;
                                                console.log("file", a.path)
                                                if (isElectron) {

                                                    const result = await window.electronAPI.downloadRemote(a.path);
                                                    if (result.success) {
                                                        console.log("Download completato:", result.path);
                                                    } else {
                                                        console.log("Download annullato o fallito");
                                                    }
                                                } else {
                                                    const downloadFile = async (url: string, filename: string) => {
                                                        const response = await axios.get(url, { responseType: "blob" });
                                                        const blobUrl = URL.createObjectURL(response.data);

                                                        const a = document.createElement("a");
                                                        a.href = blobUrl;
                                                        a.download = filename;
                                                        a.click();

                                                        URL.revokeObjectURL(blobUrl);
                                                    };
                                                    downloadFile(`/files/${a.filename}.${a.extension}`, `${a.filename}.${a.extension}`);

                                                }

                                            }}
                                            className="active:scale-90 transition-all duration-200"
                                        >
                                            <div className="rounded-[var(--radius)] p-2 hover:bg-black/20">
                                                <DownloadIcon className="w-6 h-6" />
                                            </div>
                                        </button>

                                    </motion.div>
                                );
                            }
                        })}

                    </div>
                </motion.div>
            </ContextMenu.Trigger>

            <ContextMenu.Content className="bg-[#313244]  text-white rounded-md shadow-lg border border-white/10 p-1 z-100" onEscapeKeyDown={onCloseMenu}
                onPointerDownOutside={onCloseMenu}>
                <ContextMenu.Item
                    onSelect={() => {
                        let messageId = msg.messageId;

                        let type: string = messageType;
                        socket?.emit("deleteMessage", { id: id, type, messageId })
                        onCloseMenu();
                    }}
                    className="p-2 hover:bg-white/10 rounded flex items-center gap-2 text-white"
                >
                    <Trash className="w-4 h-4 shrink-0" />
                    <span>Elimina</span>
                </ContextMenu.Item>

                <ContextMenu.Item
                    onSelect={() => {
                        setModifying(true);
                        onCloseMenu();
                    }}
                    className="p-2 hover:bg-white/10 rounded flex items-center gap-2 text-white"
                >
                    <Pencil className="w-4 h-4 shrink-0" />
                    <span>Modifica</span>
                </ContextMenu.Item>

                <ContextMenu.Item
                    onSelect={() => {
                        onCloseMenu();
                    }}
                    className="p-2 hover:bg-white/10 rounded flex items-center gap-2 text-white"
                >
                    <Eye className="w-4 h-4 shrink-0" />
                    <span>Visualizza profilo utente</span>
                </ContextMenu.Item>

                <ContextMenu.Item
                    onSelect={() => {

                        onCloseMenu();
                    }}
                    className="p-2 hover:bg-white/10 rounded flex items-center gap-2 text-white"
                >
                    <Clipboard className="w-4 h-4 shrink-0" />
                    <span>Copia messaggio</span>
                </ContextMenu.Item>

            </ContextMenu.Content>
        </ContextMenu.Root>
    );
}