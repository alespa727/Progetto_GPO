import * as ContextMenu from "@radix-ui/react-context-menu";
import { Trash, Pencil, Eye, Clipboard } from "lucide-react";
import { Message } from "../../types";
import { useSocket } from "../../context/SocketProvider";
import { useState } from "react";

export function Messaggio({ msg, messageType, id, style, onCloseMenu, onTrigger }: { msg: Message, messageType: string, id: number, style: string, onCloseMenu: () => void, onTrigger: () => void }) {
    const socket = useSocket();
    const [isBeingModified, setModifying] = useState<boolean>(false);
    const [draft, setDraft] = useState(msg.text);
    let username = msg.sender.username;
    if (username.length > 9) {
        username = username.slice(0, 9) + "...";
    }

    if (isBeingModified) return (
        <div
            className={"rounded-r-md px-4 mr-4 hover:bg-white/10 flex items-center"}
        >
            <div className="gap-2 flex flex-col p-1 text-[15px]">
                <div className="pt-1/2 gap-2 flex">
                    <span>
                        {msg.time
                            ? (() => {
                                const date = new Date(msg.time);
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

                {/*(<img src="https://placehold.co/600x400/png" alt="img" className="mb-2" />)*/}
            </div>
        </div>
    );

    return (
        <ContextMenu.Root >
            <ContextMenu.Trigger onContextMenu={onTrigger} className={style}>
                <div
                    className={"rounded-r-md px-4 mr-4 hover:bg-white/10 flex items-center"}
                >
                    <div className="gap-2 flex flex-col p-1 text-[15px]">
                        <div className="pt-1/2 gap-2 flex">
                            <span>
                                {msg.time
                                    ? (() => {
                                        const date = new Date(msg.time);
                                        return (date.getHours() <= 9 ? "0" : "") + date.getHours() + ":" +
                                            (date.getMinutes() <= 9 ? "0" : "") + date.getMinutes();
                                    })()
                                    : ""}
                            </span>
                            <span className="hover:font-bold hover:underline">
                                {username}
                            </span>
                            <span className="break-all">
                                {msg.text}
                            </span>
                        </div>

                        {/*(<img src="https://placehold.co/600x400/png" alt="img" className="mb-2" />)*/}
                    </div>
                </div>
            </ContextMenu.Trigger>

            <ContextMenu.Content className="bg-[#313244]  text-white rounded-md shadow-lg border border-white/10 p-1 z-100" onEscapeKeyDown={onCloseMenu}
                onPointerDownOutside={onCloseMenu}>
                <ContextMenu.Item
                    onSelect={() => {
                        let messageId = msg.id;

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
                        console.log("profilo")
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