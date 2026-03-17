import { useSocket } from "@/context/SocketProvider";

import { useEffect, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { createPortal } from "react-dom";
import { PlusCircle, SendIcon, X } from "lucide-react";
import axios from "axios";

import { ClientHttp, endpoint2 } from "@/types";
import { useChats } from "@/context/ChatListContext";
import { updateServers } from "@/context/ServerListContext";

enum ResponseType {
    ERROR,
    SUCCESSFUL,
    ALREADY_EXISTS
}


export function CreateServerButton() {

    const forceUpdate = updateServers();
    const [name, setName] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [isActive, setActive] = useState<boolean>(false)
    const [response, setResponse] = useState<string>("")
    const [type, setType] = useState<ResponseType>(ResponseType.SUCCESSFUL)

    const circleStyle = "w-[65%] rounded-full aspect-square bg-white/10 self-center ";

    useEffect(() => {
        setName("")
        setDescription("")
        setActive(false)
    }, [response])


    const createServer = async () => {
    
        if (name === "") {
            setResponse("Inserire un nome valido")
            setType(ResponseType.ERROR);
            return;
        }

        try {
            const res = await ClientHttp.postCommunities(name, description)
            forceUpdate();
            setResponse(res.message);
        } catch (error: unknown) {
            setType(ResponseType.ERROR);
            if (axios.isAxiosError(error)) {
                console.log("STATUS:", error.response?.status);
                console.log("HEADERS:", error.response?.headers);
                console.log("DATA RAW:", error.response?.data);
                console.log("TYPE OF DATA:", typeof error.response?.data);

                setResponse(error.response?.data.message);
            }

        }
    }

    return (
        <>

            <motion.div
                whileHover={{ scale: 1.01, background: "#FFFFFF1F" }}
                whileTap={{ scale: 0.97, background: "#7ED957" }}
                transition={{
                type: "spring",
                stiffness: 200,
                damping: 20,
                mass: 0.5
                }}
                onClick={() => { setActive(true)}}
                className={circleStyle.concat("mb-1 flex items-center justify-center active:bg-green-50 text-white font-semibold shadow-md hover:shadow-lg cursor-pointer select-none")}
            >
                <PlusCircle></PlusCircle>
            </motion.div>

            {typeof window !== "undefined" &&
                createPortal(
                    <AnimatePresence>
                        {isActive && (
                            <motion.div
                                className="fixed inset-0 backdrop-blur-md z-[999999] flex items-center justify-center bg-black/20"
                                initial={{ opacity: 0 }}
                                animate={{ opacity: 1 }}
                                exit={{ opacity: 0 }}
                                onClick={() => {
                                    setActive(false)
                                    setResponse("")
                                }}
                            >
                                <div className="flex absolute gap-2 top-5 right-5">
                                    <motion.button
                                        className=" flex items-center justify-center p-2 
                                    w-[52px] h-[52px] border-4 border-white/10 
                                    rounded-[var(--radius)] bg-[var(--background)]"
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        exit={{ scale: 0.9, opacity: 0 }}
                                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                        whileTap={{
                                            scale: 0.8,
                                            rotate: 10
                                        }}
                                        onClick={() => {
                                            setActive(false);
                                        }}
                                    >
                                        <X />
                                    </motion.button>
                                </div>



                                <div
                                    className="relative w-100 bg-(--surface0) rounded-(--radius) max-w-lg text-left"
                                    onClick={(e) => e.stopPropagation()}
                                >

                                    <h2 className="m-7 text-xl font-bold">Crea il tuo nuovo server!</h2>

                                    <div className="m-7 flex flex-col">
                                        <input
                                            type="text"
                                            placeholder="Ex. Nome del server di tommy"
                                            className="p-2 rounded mb-3  bg-(--surface1) border border-white/10"
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                        />

                                         <input
                                            type="text"
                                            placeholder="Ex. Descrizione molto bella!"
                                            className="p-2 rounded mb-3  bg-(--surface1) border border-white/10"
                                            value={description}
                                            onChange={(e) => setDescription(e.target.value)}
                                        />

                                     
                                        {
                                            response !== "" && <div className={"w-full p-2 bg-(--base) rounded-(--radius)" + (type === ResponseType.ERROR ? " bg-red-500" : " bg-green-500")}>
                                                <p className={"text-white text-center"}>{(type === ResponseType.ERROR ? "Errore: " : "") + response}</p>
                                            </div>
                                        }


                                        <motion.button
                                            transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                            whileTap={{
                                                scale: 0.9,
                                            }}
                                            className={"rounded-(--radius) text-white text-[16px] mt-3 w-full h-full bg-(--surface1) p-2"}
                                            onClick={() => createServer()}>
                                            Crea!
                                        </motion.button>
                                    </div>
                                </div>

                            </motion.div>
                        )}
                    </AnimatePresence>
                    ,
                    document.body
                )}
        </>

    );
}
