import { motion } from "framer-motion";
import { useActiveChatContext } from "../../context/ActiveChatProvider";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useChannelContext } from "../../context/ChannelContext";
import { useChatContext } from "../../context/ChatContext";
import { useSectionContext } from "../../context/SezioneContext";
import { ChannelType } from "../../types";
import { div } from "framer-motion/client";
import { Search } from "lucide-react";


export interface SearchProps {
    filter: string;
    setFilter: (filter: string) => void;
    onClick?: ()=>void
}

export function SearchBar({ onClick, filter, setFilter }: SearchProps) {
    return (
        <motion.div
            whileHover={{ background: "#FFFFFF1F" }}
            whileTap={{ scale: 0.99, background: "#FFFFFF2F" }}
            transition={{
                type: "spring",
                stiffness: 600,
                damping: 20,
                mass: 0.5
            }}
            onClick={onClick}
            className="
                w-full
                m-2.5
                mr-0
                mb-0
                p-1.5 px-4
                flex 
                justify-center  
                cursor-pointer
                items-center
                rounded-(--radius)
                gap-[9px]
                text-[20px]
            ">
            <Search></Search>
            <input
                type="text"
                placeholder="Cerca..."
                className="
                    w-full 
                    p-2 
                    rounded 
                    appearance-none 
                    bg-transparent
                    outline-none 
                    focus:outline-none 
                    focus:ring-0
                "
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
            />
        </motion.div>

    );
}