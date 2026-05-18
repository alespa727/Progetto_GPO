import { useEffect, useRef, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { createPortal } from "react-dom";
import { X } from "lucide-react";
import { useDropzone } from "react-dropzone";
import { ClientHttp } from "@/types";

export function ChangePfp() {
    const [file, setFile] = useState<File | null>(null);
    const [isActive, setActive] = useState<boolean>(false);

    const addFile = (file: File) => {
        setFile(file); // sempre uno solo
    };

    return (
        <>
            <button onClick={() => setActive(!isActive)} className="px-4 py-2 rounded-md border border-[#313244] hover:bg-white/10 text-sm text-white">
                Cambia foto
            </button>


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
                                    setActive(false);
                                }}
                            >
                                <div className="flex absolute gap-2 top-5 right-5">
                                    <motion.button
                                        className="flex items-center justify-center p-2 w-[52px] h-[52px] border-4 border-white/10 rounded-[var(--radius)] bg-[var(--background)]"
                                        whileTap={{ scale: 0.8, rotate: 10 }}
                                        onClick={() => setActive(false)}
                                    >
                                        <X />
                                    </motion.button>
                                </div>

                                <div
                                    className="relative p-5 w-100 bg-(--surface0) rounded-(--radius) max-w-lg"
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    <MyDropzone file={file} addFile={addFile} />

                                    <motion.button
                                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                        whileTap={{
                                            scale: 0.9,
                                        }}
                                        className={"rounded-(--radius) text-white text-[16px] mt-3 w-full h-full bg-(--surface1) p-2"}
                                        onClick={() => {
                                                if (file) ClientHttp.patchPfp(file)
                                                
                                                const reload = () => {
                                                    (async () => {
                                                
                                                        window.location.reload();
                                                    })();
                                                }

                                                reload();

                                            }
                                        }>
                                        Invia richiesta
                                    </motion.button>
                                </div>
                            </motion.div>
                        )}
                    </AnimatePresence>,
                    document.body
                )}
        </>
    );
}

function MyDropzone({
    file,
    addFile,
}: {
    file: File | null;
    addFile: (file: File) => void;
}) {
    const [preview, setPreview] = useState<string | null>(null);
    const dragCounter = useRef(0);

    useEffect(() => {
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            if (!e.target?.result) return;
            setPreview(e.target.result as string);
        };

        reader.readAsDataURL(file);
    }, [file]);

    const { getRootProps, getInputProps } = useDropzone({
        onDrop: (acceptedFiles: File[]) => {
            if (acceptedFiles.length > 0) {
                addFile(acceptedFiles[0]); // SOLO 1 FILE
            }

            dragCounter.current = 0;
        },
        maxFiles: 1,
        accept: {
            "image/*": [],
        },
    });

    return (
        <div {...getRootProps()} className="relative cursor-pointer">
            <input {...getInputProps()} />

            <div className="rounded-(--radius) bg-white/10 flex items-center justify-center p-4 min-h-[200px]">
                {file && preview ? (
                    <div className="flex flex-col items-center gap-2">
                        <img
                            className="rounded-(--radius)"
                            src={preview}
                            width={150}
                            alt="preview"
                        />
                        <b className="text-sm text-white">{file.name}</b>
                    </div>
                ) : (
                    <p className="text-center text-white/70">
                        Trascina un'immagine qui o clicca per selezionarla
                    </p>
                )}
            </div>
        </div>
    );
}