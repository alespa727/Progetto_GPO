import { useEffect, useState } from "react";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { Server } from "../../types";
import { ChevronDown, Wrench } from "lucide-react";
import * as ContextMenu from "@radix-ui/react-context-menu"
import { ManageServer } from "./ManageServer";
import { useAccount } from "@/context/UserProvider";

export function ServerName() {
    const { activeServer } = useActiveServerContext();
    const account = useAccount();
    const channel = useActiveServerContext().activeChannel;
    const [server, setServer] = useState<Server | null>(null);
    const [active, setActive] = useState(false)

    useEffect(() => {
        if (activeServer) setServer(activeServer);
    }, [activeServer]);

    return (
        <>
            <ManageServer active={active} setActive={setActive}></ManageServer>

            <ContextMenu.Root>
                <ContextMenu.Trigger asChild>
                    <div className="border-b border-b-[#313244] text-gray-300 hover:bg-black/20 hover:text-white rounded-l-md rounded-r-none cursor-pointer p-4 flex items-center">
                        <ChevronDown className="w-5 mr-1" />
                        {server?.name}
                    </div>
                </ContextMenu.Trigger>
                {activeServer?.owner===account?.username && <ContextMenu.Content className="bg-[#1e1e2e] text-white rounded-xl shadow-2xl border border-white/10 p-1.5 z-100 min-w-[180px]">
                    <ContextMenu.Item
                        onSelect={() => {
                            if(activeServer?.owner===account?.username) 
                                setActive(true)

                        }}
                        className="px-3 py-2 rounded-lg flex items-center gap-2.5 text-sm cursor-pointer transition-colors hover:bg-white/10 text-white/90"
                    >
                        <Wrench className="w-4 h-4" />
                        <span>Modifica impostazioni</span>
                    </ContextMenu.Item>
                </ContextMenu.Content>}
            </ContextMenu.Root>
        </>
    );
}