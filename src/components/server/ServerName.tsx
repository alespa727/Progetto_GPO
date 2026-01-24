import { useEffect, useState } from "react";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { Server } from "../../types";
import { ChevronDown, Wrench } from "lucide-react";
import * as ContextMenu from "@radix-ui/react-context-menu"

export function ServerName() {
    const { activeServer } = useActiveServerContext();
    const [server, setServer] = useState<Server | null>(null);

    useEffect(() => {
        if (activeServer) setServer(activeServer);
    }, [activeServer]);

    return (
        <>
         <ContextMenu.Root >
            <ContextMenu.Trigger>
               <div className="border-b border-b-[#313244] text-gray-300 hover:bg-black/20 hover:text-white rounded-l-md rounded-r-none cursor-pointer p-4 flex items-center">
                    <ChevronDown className="w-5 mr-1" /> 
                    {server?.name}
                </div>
            </ContextMenu.Trigger>

            <ContextMenu.Content className="bg-[#313244]  z-100  text-white rounded-md shadow-lg border border-white/10">
                <ContextMenu.Item
                    onSelect={() => {

                    }}
                    className="p-2 hover:bg-white/10 rounded flex items-center gap-2 text-white"
                >
                    <Wrench className="w-4 h-4 shrink-0" />
                    <span>Modifica impostazioni</span>
                </ContextMenu.Item>

            </ContextMenu.Content>
        </ContextMenu.Root>
        </>
        

    );
}
