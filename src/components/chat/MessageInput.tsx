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
    const chat = useActiveChatContext().activeChat;
    const [isFile, setFile] = useState(false);
    const [files, setFiles] = useState<File[]>();
    const addFiles = (files: File[]) => {
        setFiles((prev: File[] = []) => [
            ...prev,
            ...files
        ]);
    }

    if (!chat || !(account && account !== undefined)) return;

    let id = chat ? chat.id : null;
    let type = "chat";

    const sendMessage = async (nuovoTesto: string) => {



        if (!socket) return;
        try {
            const tempId = Date.now();
            let newMessage = new Message(tempId, account.username, nuovoTesto, [], new Date(), false);
            addMessage(newMessage)
            const res = await axios.post(
                endpoint2 + "/services/chats/" + id + "/messages",
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
                console.log(res)

            } else {
                newMessage.fail = true;
            }
            forceUpdate(prev => prev + 1);
        } catch (error) {
            console.log(error)
            addMessage(new Message(-1, account.username, nuovoTesto, [], new Date(), false))

        } finally {
            setNuovoTesto("");
        }

    }

    const sendMessageAndAttachments = async (nuovoTesto: string, files: File[]) => {
        if (!files) return;
        try {
            const tempId = Date.now();
            const atts: Attachment[] = files?.map(f => {
                const filename = f.name.split(".")[0];
                const extension = f.name.split(".")[1]
                return new Attachment(tempId - 1, f.webkitRelativePath, filename, extension)
            });
            let newMessage = new Message(tempId, account.username, nuovoTesto, atts, new Date(), false);
            addMessage(newMessage);
            const formData = new FormData();

            files.forEach(file => {
                formData.append("files", file);
            });
            formData.append("message", nuovoTesto );

            const res = await axios.post(endpoint2 + "/services/chats/" + chat.id + "/attachments", formData, {
                withCredentials: true,
            });

            if (socket && res.status === HttpStatusCode.Created) {
                newMessage.messageId = res.data.messageId;
                newMessage.sent = true;
                console.log(res.data)
                console.log(res.data.attachments.map((a: any) => Attachment.fromJSON(a)))
                newMessage.attachments = res.data.attachments.map((a: any) => Attachment.fromJSON(a));
                socket.emit("sendMessage", { id, type, message: newMessage });
                console.log(res)

            } else {
                newMessage.fail = true;
            }
            forceUpdate(prev => prev + 1);
        } catch (error) {
            console.error("errore mandando il messaggio",error)
        } finally {
            setNuovoTesto("");
            setFile(!isFile);
            setFiles([])
        }
    };

    const send = async () => {
        if (!socket) return;
        if (!nuovoTesto) return;
        if (nuovoTesto.trim() === "") return;
        
        if (files && files?.length > 0) {
            await sendMessageAndAttachments(nuovoTesto, files);
        } else {
            await sendMessage(nuovoTesto)
        }
        
    }

    return (
        <>
            <div className="pb-1.5 pr-1.5 pl-1.5">
                <div className="flex flex-col gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
                    {
                        isFile ?
                            <>
                                <MyDropzone files={files} addFiles={addFiles}></MyDropzone>

                            </>

                            : <></>
                    }
                    <div className="relative flex gap-3">
                        <Plus className="w-6 h-6 text-white" onClick={() => setFile(!isFile)} />

                        <input
                            value={nuovoTesto || ""}
                            className="flex-1 bg-transparent text-[14px] focus:outline-none"
                            onChange={(e) => setNuovoTesto(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") send();
                            }}
                            placeholder="Scrivi qui..."
                        />
                        <Send className="absolute right-1" onClick={() => {
                            send();
                        }}></Send>
                    </div>



                </div>
            </div>
        </>
    )
}
import { useDropzone } from "react-dropzone";
import { useEffect, } from "react";

function MyDropzone({
    files,
    addFiles,
}: {
    files: File[] | undefined;
    addFiles: (files: File[]) => void;
}
) {
    const [isDragging, setIsDragging] = useState(false);
    const dragCounter = useRef(0);
    const [previews, setPreviews] = useState<Record<string, string>>({});

    useEffect(() => {
        if (!files) return;

        files.forEach((f) => {
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
            setIsDragging(false);
        }
    });

    useEffect(() => {

    }, [])

    return (
        <div {...getRootProps()} className="relative">
            <input {...getInputProps()} />


            <div className="flex items-center p-4 pointer-events-none">
                {files?.map(f => (
                    <div key={f.name}>
                        {previews[f.name] && (
                            <img src={previews[f.name]} alt={f.name} width={150} />
                        )}
                        <b>{f.name}</b>
                    </div>
                ))}
                {
                    files && files.length === 0 ? <p className="w-full h-full text-center justify-center">
                        Rilascia i file qui...
                    </p> : <></>
                }


            </div>

        </div>
    );
}

export default MyDropzone;
