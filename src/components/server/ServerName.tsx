import { act, useEffect, useState } from "react";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { Server } from "../../types";
import { useServerContext } from "../../context/ServerContext";
import { ChevronDown } from "lucide-react";

export function ServerName() {
    const { activeServer } = useActiveServerContext();
    const [server, setServer] = useState<Server | null>(null);

    useEffect(() => {
        if (activeServer) setServer(activeServer);
    }, [activeServer]);

    return (
        <div className="border-b border-b-[#313244] text-gray-300 hover:bg-black hover:text-white rounded-l-md rounded-r-none cursor-pointer p-4 flex items-center">
            <ChevronDown className="w-5 mr-1" /> 
            {server?.name}
        </div>
    );
}
