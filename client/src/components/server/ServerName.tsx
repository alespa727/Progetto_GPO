import { act, useEffect, useState } from "react";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { Server } from "../../types";
import { useServerContext } from "../../context/ServerContext";
import { ChevronDown } from "lucide-react";

export function ServerName() {
    const { activeServer } = useActiveServerContext();
    const [server, setServer] = useState<Server | null>(null);
    useEffect(()=>{
        if(activeServer){
            setServer(activeServer);
        }
    }, [activeServer]);
  
    return (
        <>    
            <div className="border-r-0  border-b border-b-[#313244] text-gray-300 hover:bg-black/10 hover:text-white"> 
                <div className="box-border w-full hover:bg-black/10 cursor-pointer text-white p-4 flex">
                     <ChevronDown className="w-5 inline-block mr-1"/> {server?.name}
                </div>
            </div>
        </>
    );
}