import { useSocket } from "@/context/SocketProvider";
import { useEffect, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { createPortal } from "react-dom";
import { PlusCircle, X } from "lucide-react";
import axios from "axios";

import { ClientHttp } from "@/types";
import { updateServers } from "@/context/ServerListContext";

enum ResponseType {
    ERROR,
    SUCCESSFUL,
}

type Tab = "create" | "join";

export function CreateServerButton() {

    const forceUpdate = updateServers();
    const [activeTab, setActiveTab] = useState<Tab>("create");

    // Scheda "Crea"
    const [name, setName] = useState<string>("");
    const [description, setDescription] = useState<string>("");

    // Scheda "Entra"
    const [inviteCode, setInviteCode] = useState<string>("");

    const [isActive, setActive] = useState<boolean>(false);
    const [response, setResponse] = useState<string>("");
    const [type, setType] = useState<ResponseType>(ResponseType.SUCCESSFUL);

    const circleStyle = "w-[65%] rounded-full aspect-square bg-white/10 self-center ";

    useEffect(() => {
        setName("");
        setDescription("");
        setInviteCode("");
        setActive(false);
    }, [response]);

    const handleClose = () => {
        setActive(false);
        setResponse("");
    };

    const createServer = async () => {
        if (name === "") {
            setResponse("Inserire un nome valido");
            setType(ResponseType.ERROR);
            return;
        }
        try {
            const res = await ClientHttp.postCommunities(name, description);
            forceUpdate();
            setResponse(res.message);
        } catch (error: unknown) {
            setType(ResponseType.ERROR);
            if (axios.isAxiosError(error)) {
                setResponse(error.response?.data.message);
            }
        }
    };

    const joinServer = async () => {
        if (inviteCode.trim() === "") {
            setResponse("Inserire un codice invito valido");
            setType(ResponseType.ERROR);
            return;
        }
        try {
            const res = await ClientHttp.joinByInviteCode(inviteCode.trim());
            forceUpdate();
            
        } catch (error: unknown) {
            setType(ResponseType.ERROR);
            if (axios.isAxiosError(error)) {
                setResponse(error.response?.data.message);
            }
        }
    };

    const tabBase = "pb-2 text-sm cursor-pointer border-b-2 transition-colors select-none mr-6";
    const tabActive = "border-[#7ED957] text-white";
    const tabInactive = "border-transparent text-white/40 hover:text-white/70";

    return (
        <>
            <motion.div
                whileHover={{ scale: 1.01, background: "#FFFFFF1F" }}
                whileTap={{ scale: 0.97, background: "#7ED957" }}
                transition={{ type: "spring", stiffness: 200, damping: 20, mass: 0.5 }}
                onClick={() => setActive(true)}
                className={circleStyle.concat("mb-1 flex items-center justify-center active:bg-green-50 text-white font-semibold shadow-md hover:shadow-lg cursor-pointer select-none")}
            >
                <PlusCircle />
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
                                onClick={handleClose}
                            >
                                {/* Pulsante chiudi */}
                                <div className="flex absolute gap-2 top-5 right-5">
                                    <motion.button
                                        className="flex items-center justify-center p-2 w-[52px] h-[52px] border-4 border-white/10 rounded-[var(--radius)] bg-[var(--background)]"
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        exit={{ scale: 0.9, opacity: 0 }}
                                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                        whileTap={{ scale: 0.8, rotate: 10 }}
                                        onClick={handleClose}
                                    >
                                        <X />
                                    </motion.button>
                                </div>

                                <div
                                    className="relative w-100 bg-(--surface0) rounded-(--radius) max-w-lg text-left"
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    <h2 className="m-7 mb-0 text-xl font-bold">Entra o crea un server</h2>

                                    {/* Tab switcher */}
                                    <div className="mx-7 mt-4 flex border-b border-white/10">
                                        <button
                                            className={`${tabBase} ${activeTab === "create" ? tabActive : tabInactive}`}
                                            onClick={() => { setActiveTab("create"); setResponse(""); }}
                                        >
                                            Crea server
                                        </button>
                                        <button
                                            className={`${tabBase} ${activeTab === "join" ? tabActive : tabInactive}`}
                                            onClick={() => { setActiveTab("join"); setResponse(""); }}
                                        >
                                            Entra tramite codice
                                        </button>
                                    </div>

                                    <div className="m-7 flex flex-col">

                                        {/* Scheda: Crea server */}
                                        {activeTab === "create" && (
                                            <>
                                                <input
                                                    type="text"
                                                    placeholder="Es. Nome del server di Tommy"
                                                    className="p-2 rounded mb-3 bg-(--surface1) border border-white/10"
                                                    value={name}
                                                    onChange={(e) => setName(e.target.value)}
                                                />
                                                <input
                                                    type="text"
                                                    placeholder="Es. Descrizione molto bella!"
                                                    className="p-2 rounded mb-3 bg-(--surface1) border border-white/10"
                                                    value={description}
                                                    onChange={(e) => setDescription(e.target.value)}
                                                />
                                                {response !== "" && (
                                                    <div className={`w-full p-2 rounded-(--radius) mb-3 ${type === ResponseType.ERROR ? "bg-red-500" : "bg-green-500"}`}>
                                                        <p className="text-white text-center">
                                                            {(type === ResponseType.ERROR ? "Errore: " : "") + response}
                                                        </p>
                                                    </div>
                                                )}
                                                <motion.button
                                                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                                    whileTap={{ scale: 0.9 }}
                                                    className="rounded-(--radius) text-white text-[16px] w-full bg-(--surface1) p-2"
                                                    onClick={createServer}
                                                >
                                                    Crea!
                                                </motion.button>
                                            </>
                                        )}

                                        {/* Scheda: Entra tramite codice */}
                                        {activeTab === "join" && (
                                            <>
                                                <p className="text-sm text-white/50 mb-3">
                                                    Inserisci il codice invito che hai ricevuto per unirti a un server esistente.
                                                </p>
                                                <input
                                                    type="text"
                                                    placeholder="Es. ABC-12345"
                                                    className="p-2 rounded mb-1 bg-(--surface1) border border-white/10 font-mono tracking-widest text-center text-lg"
                                                    value={inviteCode}
                                                    onChange={(e) => setInviteCode(e.target.value.toUpperCase())}
                                                    maxLength={9}
                                                />
                                                <p className="text-xs text-white/30 mb-3">
                                                    I codici invito sono composti da lettere e numeri.
                                                </p>
                                                {response !== "" && (
                                                    <div className={`w-full p-2 rounded-(--radius) mb-3 ${type === ResponseType.ERROR ? "bg-red-500" : "bg-green-500"}`}>
                                                        <p className="text-white text-center">
                                                            {(type === ResponseType.ERROR ? "Errore: " : "") + response}
                                                        </p>
                                                    </div>
                                                )}
                                                <motion.button
                                                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                                    whileTap={{ scale: 0.9 }}
                                                    className="rounded-(--radius) text-white text-[16px] w-full bg-(--surface1) p-2"
                                                    onClick={joinServer}
                                                >
                                                    Unisciti!
                                                </motion.button>
                                            </>
                                        )}
                                    </div>
                                </div>
                            </motion.div>
                        )}
                    </AnimatePresence>,
                    document.body
                )}
        </>
    );
}