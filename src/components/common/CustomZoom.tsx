import { useState } from "react"
import { motion, AnimatePresence } from "framer-motion"
import { createPortal } from "react-dom"
import { Download, X } from "lucide-react"
import axios from "axios"
import { Attachment } from "@/types"

export default function MyCustomZoom({
    src,
    alt,
    className,
    attachment
}: {
    src: string
    alt: string
    className?: string
    attachment?: Attachment
}) {
    const [isZoomed, setIsZoomed] = useState(false)

    return (
        <>
            <motion.img
                src={src}
                alt={alt}
                onClick={() => setIsZoomed(true)}
                className={"cursor-pointer object-contain max-w-full max-h-[40vh] min-h-[60px] min-w-[60px] md:min-h-[100px] md:min-w-[100px] md:max-h-[50vh] md:max-w-[70%] "+className}

            />
            {typeof window !== "undefined" &&
                createPortal(
                    <AnimatePresence>
                        {isZoomed && (
                            <motion.div
                                className="fixed inset-0 backdrop-blur-md z-[999999] flex items-center justify-center bg-black/20"
                                initial={{ opacity: 0 }}
                                animate={{ opacity: 1 }}
                                exit={{ opacity: 0 }}
                                onClick={() => setIsZoomed(false)}
                            >
                                <div className="flex absolute gap-2 top-5 right-5">
                                    {attachment && (<motion.button
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
                                            const downloadFile = async (url: string, filename: string) => {
                                                const response = await axios.get(url, { responseType: "blob" });
                                                const blobUrl = URL.createObjectURL(response.data);

                                                const a = document.createElement("a");
                                                a.href = blobUrl;
                                                a.download = filename;
                                                a.click();

                                                URL.revokeObjectURL(blobUrl);
                                            };
                                            downloadFile(`/files/${attachment.filename}${attachment.extension}`, `${attachment.originalname}${attachment.extension}`);

                                            setIsZoomed(false);
                                        }}
                                    >
                                        <Download />
                                    </motion.button>)}
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
                                            setIsZoomed(false);
                                        }}
                                    >
                                        <X />
                                    </motion.button>
                                </div>




                                <motion.img
                                    src={src}
                                    alt={alt}
                                    className=" min-h-[180px] min-w-[180px] max-h-[70vh] max-w-[90vw] md:max-h-[vh] md:max-w-[70vw] rounded-(--radius)"
                                    initial={{ scale: 0.8 }}
                                    animate={{ scale: 1 }}
                                    exit={{ scale: 0.9, rotate: 10 }}
                                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                    onClick={(e) => e.stopPropagation()}
                                />
                            </motion.div>
                        )}
                    </AnimatePresence>
                    ,
                    document.body
                )}
        </>
    )
}
