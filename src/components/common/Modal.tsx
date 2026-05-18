import { motion, AnimatePresence } from "framer-motion"
import { createPortal } from "react-dom"
import { X } from "lucide-react"
import { ReactNode, useState } from "react"

export default function Modal({
    children,
    extraButtons,
    open,
    onOpenChange,
    trigger,
    className
}: {
    children: ReactNode
    extraButtons?: (close: () => void) => ReactNode
    open?: boolean
    onOpenChange?: (open: boolean) => void
    trigger?: (open: () => void) => ReactNode
    className?: string
}) {
    const [internalOpen, setInternalOpen] = useState(false)

    const isOpen = open ?? internalOpen
    const setIsOpen = (v: boolean) => {
        setInternalOpen(v)
        onOpenChange?.(v)
    }

    return (
        <>
            {trigger?.(() => setIsOpen(true))}

            {typeof window !== "undefined" &&
                createPortal(
                    <AnimatePresence>
                        {isOpen && (
                            <motion.div
                                className="fixed inset-0 backdrop-blur-md z-[999999] flex items-center justify-center bg-black/20"
                                initial={{ opacity: 0 }}
                                animate={{ opacity: 1 }}
                                exit={{ opacity: 0 }}
                                onClick={() => setIsOpen(false)}
                            >
                                <div className="flex absolute gap-2 top-5 right-5">
                                    {extraButtons?.(() => setIsOpen(false))}
                                    <motion.button
                                        className="flex items-center justify-center p-2 w-[52px] h-[52px] border-4 border-white/10 rounded-[var(--radius)] bg-[var(--background)]"
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        exit={{ scale: 0.9, opacity: 0 }}
                                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                        whileTap={{ scale: 0.8, rotate: 10 }}
                                        onClick={() => setIsOpen(false)}
                                    >
                                        <X />
                                    </motion.button>
                                </div>

                                <motion.div
                                    className={`min-h-[180px] min-w-[180px] rounded-(--radius) overflow-hidden ${className ?? ""}`}
                                    initial={{ scale: 0.8 }}
                                    animate={{ scale: 1 }}
                                    exit={{ scale: 0.9, rotate: 10 }}
                                    transition={{ type: "spring", stiffness: 260, damping: 25 }}
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    {children}
                                </motion.div>
                            </motion.div>
                        )}
                    </AnimatePresence>,
                    document.body
                )}
        </>
    )
}