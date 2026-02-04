import { useActiveChatContext } from "@/context/ActiveChatProvider";
import { useSocket } from "@/context/SocketProvider";
import { useAccount } from "@/context/UserProvider";
import { endpoint2, Message } from "@/types";
import axios, { HttpStatusCode } from "axios";
import { Plus } from "lucide-react";
import { div } from "motion/react-client";
import { useRef, useState } from "react";

export function MessageInput({ addMessage, forceUpdate }: { addMessage: (message: Message) => void, forceUpdate: React.Dispatch<React.SetStateAction<number>> }) {
    const socket = useSocket();
    const account = useAccount();
    const [nuovoTesto, setNuovoTesto] = useState<string>("");
    const chat = useActiveChatContext().activeChat;
    const [isFile, setFile] = useState(false);
    const [files, setFiles] = useState<File[]>([]);
    const fileInputRef = useRef<HTMLInputElement>(null);


    const handleFileChange = () => {
        const selectedFiles = fileInputRef.current?.files;
        if (selectedFiles) {
            setFiles(Array.from(selectedFiles));
        }
    };

    const handleClick = () => {
        fileInputRef.current?.click();
    }

    if (!chat || !(account && account !== undefined)) return;

    const sendMessage = () => {
        if (!socket) return;
        if (!nuovoTesto) return;
        if (nuovoTesto.trim() === "") return;

        let id = chat ? chat.id : null;

        let type = "chat";
        const send = async () => {
            try {
                const tempId = Date.now();
                let newMessage = new Message(tempId, account.username, nuovoTesto, [], new Date(), false);
                addMessage(newMessage)
                const res = await axios.post(
                    endpoint2 + "/services/chats/" + id + "/message",
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


        };
        send();
    }

    return (
        <>
            <div className="pb-1.5 pr-1.5 pl-1.5">
                <div className="flex gap-3 bg-[#313244] rounded-md px-3 p-3 text-white shadow-lg">
                    {
                        isFile ?
                            <>
                                <input ref={fileInputRef} multiple onChange={handleFileChange} className="hidden" value="" type="file"></input>
                                <div
                                    onClick={() => setFile(false)}
                                    className="z-100 top-0 left-0 backdrop-blur-sm absolute overflow-hidden w-full h-full bg-black/50"
                                >

                                </div>
                                <div
                                    onClick={handleClick}
                                    className="z-101 absolute w-[500px] h-auto border border-black bg-[#1e1e2e] top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"
                                >
                                    <p className="text-left p-6 w-full border-b border-black">Clicca per selezionare un file</p>
                                    {files.map((file, index) => (
                                        <div className={"p-3"} key={index}>
                                            <strong>Nome:</strong> {file.name} |{" "}
                                            <strong>Dimensione:</strong> {file.size} bytes |{" "}
                                            <strong>Tipo:</strong> {file.type || "Sconosciuto"}
                                        </div>
                                    ))}
                                    <button onClick={async()=>{
                                        const form = new FormData();
                                        form.append('message', nuovoTesto);
                                        files.forEach((file)=>{
                                            form.append("files", file);
                                        })
                                        const res = await axios.post(endpoint2+"/services/chats/"+chat.id+"/attachment", form, {
                                            withCredentials: true
                                        });
                                        console.log(res)

                                    }} className="p-4 bg-black">
                                        Invia
                                    </button>
                                </div>

                            </>

                            : <></>
                    }
                    <Plus className="w-6 h-6 text-white" onClick={() => setFile(!isFile)} />

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