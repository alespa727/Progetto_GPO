import { useEffect, useState } from "react";
import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { Server } from "../../types";
import { useServerContext } from "../../context/ServerContext";

export function SidebarServerName() {
    const sidebarServer = useServerContext();
    const [server, setServer] = useState<Server | null>(null);
    useEffect(()=>{
        if(sidebarServer){
            setServer(sidebarServer);
        }
    }, []);
    
  
    return (
        <>    
            {server?.name}
        </>
    );
}