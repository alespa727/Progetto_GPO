import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useSocket } from "@/context/SocketProvider";
import { useAccount } from "@/context/UserProvider";
import { Attachment, endpoint2, Message } from "@/types";
import axios, { HttpStatusCode } from "axios";
import { Plus, Send } from "lucide-react";
import { useRef, useState } from "react";

export function MessageInput({ addMessage, forceUpdate }: { addMessage: (message: Message) => void, forceUpdate: React.Dispatch<React.SetStateAction<number>> }) {
    const socket = useSocket();
    const account = useAccount();
    const [nuovoTesto, setNuovoTesto] = useState<string>("");
    const channel = useActiveServerContext().activeChannel;
    const sectionId = channel?.sectionId;
    const community = useActiveServerContext().activeServer;
    const setActiveChannel = useActiveServerContext().setActiveChannel;
    const [isFile, setFile] = useState(false);
    const [files, setFiles] = useState<File[]>();
    const addFiles = (files: File[]) => {
        setFiles((prev: File[] = []) => [
            ...prev,
            ...files
        ]);
    }
    const removeFile = (index: number) => {
        setFiles((prev: File[] = []) => prev.filter((_, i) => i !== index));
    }

    if (!setActiveChannel || !channel || !(account && account !== undefined)) return;

    useEffect(() => {
        setFiles([]);
        setFile(false);
        setNuovoTesto("");
    }, [channel.id])

    let id = channel ? channel.id : null;
    let type = "channel";


    const sendMessage = async (nuovoTesto: string) => {



        if (!socket) return;
        if(!community) return;
        const tempId = Date.now();
        let newMessage = new Message(tempId, account.username, nuovoTesto, [], new Date(), false);
        try {

            addMessage(newMessage)
            const res = await axios.post(
                endpoint2 + "/services/communities/" + community.id + "/sections/"+sectionId+"/channels/"+id+"/messages",
                {
                    message: nuovoTesto,
                },
                {
                    withCredentials: true,
                }

            );

            if (res.status === HttpStatusCode.Created) {
                newMessage.messageId = res.data.messageId;
                newMessage.sent = true;
                socket.emit("sendMessage", { id, type, message: newMessage });
                
            } else {
                newMessage.fail = true;
            }
            
        } catch (error) {
            console.log(error)
            if (newMessage) newMessage.fail = true;

            addMessage(new Message(-1, account.username, nuovoTesto, [], new Date(), false))
            
        } finally {
            setNuovoTesto("");
            forceUpdate(prev => prev + 1);
        }

    }

    const sendMessageAndAttachments = async (nuovoTesto: string, files: File[]) => {
        if (!files) return;
        setFile(false);
        setFiles([]);
        const tempId = Date.now();
        const atts: Attachment[] = files?.map(f => {
            const filename = f.name.split(".")[0];
            const extension = f.name.split(".")[1]
            return new Attachment(tempId - 1, f.webkitRelativePath, filename, filename, extension)
        });
        let newMessage = new Message(tempId, account.username, nuovoTesto, atts, new Date(), false);
        
        try {

            if(!community) return;
            addMessage(newMessage);
            const formData = new FormData();

            files.forEach(file => {
                formData.append("files", file);
            });
            formData.append("message", nuovoTesto);

            const res = await axios.post<Message>( endpoint2 + "/services/communities/" + community.id + "/sections/"+sectionId+"/channels/"+id+"/attachments", formData, {
                withCredentials: true,
            });


            if (socket && res.status === HttpStatusCode.Created) {
                newMessage.messageId = res.data.messageId;
                newMessage.sent = true;
                newMessage.attachments = res.data.attachments;

                socket.emit("sendMessage", { id, type, message: newMessage });
                
            } else {
                newMessage.fail = true;
            }
        } catch (error) {
            if (newMessage) newMessage.fail = true;
            console.error("errore mandando il messaggio", error)
        } finally {
            setNuovoTesto("");
            forceUpdate(prev => prev + 1);
            setFile(!isFile);
            setFiles([])
        }
    };

    const send = async () => {
        if (!socket) return;

        if (files && files?.length > 0) {
            await sendMessageAndAttachments(nuovoTesto, files);
        } else if (nuovoTesto && nuovoTesto.trim() !== "") {
            await sendMessage(nuovoTesto)
        }

    }

    return (
        <div className="pb-1.5 pr-1.5 pl-1.5 min-w-0" onKeyDown={()=>{}
        }>
            <div className="flex flex-col gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg min-w-0 overflow-hidden">
                {isFile && (
                    <MyDropzone files={files} addFiles={addFiles} removeFile={removeFile} />
                )}
                <div className="relative flex gap-3">
                    <Plus className="w-6 h-6 text-white cursor-pointer" onClick={() => setFile(!isFile)} />
                    <input
                        value={nuovoTesto || ""}
                        className="flex-1 bg-transparent text-[14px] focus:outline-none min-w-0"
                        onChange={(e) => setNuovoTesto(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") send();
                        }}
                        placeholder="Scrivi qui..."
                    />
                    <Send className="absolute right-1 cursor-pointer" onClick={send} />
                </div>
            </div>
        </div>
    )
}
import { useDropzone } from "react-dropzone";
import { useEffect, } from "react";
import { s } from "framer-motion/client";
import { useChats } from "@/context/ChatListContext";
import { useActiveServerContext } from "@/context/ActiveServerProvider";

function MyDropzone({
    files,
    addFiles,
    removeFile,
}: {
    files: File[] | undefined;
    addFiles: (files: File[]) => void;
    removeFile: (index: number) => void;
}) {
    const dragCounter = useRef(0);
    const [previews, setPreviews] = useState<Record<string, string>>({});

    useEffect(() => {
        if (!files) return;
        files.forEach((f) => {
            if (previews[f.name]) return;
            const reader = new FileReader();
            reader.onload = (e) => {
                if (!e.target?.result) return;
                setPreviews(prev => ({
                    ...prev,
                    [f.name]: e.target!.result as string
                }));
            };
            reader.readAsDataURL(f);
        });
    }, [files]);

    const { getRootProps, getInputProps } = useDropzone({
        onDrop: (acceptedFiles: File[]) => {
            addFiles(acceptedFiles);
            dragCounter.current = 0;
        },
        noKeyboard: true
    });

    const hasFiles = files && files.length > 0;

    return (
        <div {...getRootProps()} className="relative cursor-pointer" style={{ width: 0, minWidth: '100%' }}>
            <input {...getInputProps()} />
            {!hasFiles ? (
                <div className="rounded-(--radius) bg-white/5 border-2 border-dashed border-white/20 hover:border-white/40 transition-colors flex flex-col items-center justify-center p-8 gap-2">
                    <p className="text-white/50 text-sm">Trascina i file qui o clicca per selezionarli</p>
                </div>
            ) : (
                <div className="rounded-(--radius) bg-white/5 border border-white/10" style={{ width: 0, minWidth: '100%' }}>
                    <div
                        className="flex flex-row gap-2 p-3 overflow-x-auto"
                        style={{ width: 0, minWidth: '100%' }}
                    >
                        {files.map((f, index) => (
                            <div
                                key={f.name}
                                className="flex-shrink-0 flex flex-col items-center gap-1 w-32 relative group"
                            >
                                <button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        removeFile(index);
                                    }}
                                    className="absolute -top-1 -right-1 z-10 bg-black/70 hover:bg-red-500 text-white rounded-full w-5 h-5 flex items-center justify-center transition-colors opacity-0 group-hover:opacity-100 text-xs"
                                >
                                    ×
                                </button>

                                <div className="w-28 h-28 rounded-(--radius) bg-white/10 flex items-center justify-center overflow-hidden">
                                    {previews[f.name] ? (
                                        <img
                                            className="w-full h-full object-cover rounded-(--radius)"
                                            alt={f.name}
                                            src={previews[f.name]}
                                        />
                                    ) : (
                                        <span className="text-white/30 text-xs text-center px-1">
                                            {f.name.split('.').pop()?.toUpperCase()}
                                        </span>
                                    )}
                                </div>

                                <span className="text-white/60 text-xs truncate w-full text-center px-1">
                                    {f.name}
                                </span>
                            </div>
                        ))}

                        <div className="flex-shrink-0 w-28 h-28 rounded-(--radius) border-2 border-dashed border-white/20 hover:border-white/40 transition-colors flex items-center justify-center">
                            <span className="text-white/30 text-2xl">+</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default MyDropzone;