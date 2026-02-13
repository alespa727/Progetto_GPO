import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useServerContext } from "../../context/ServerContext";
import { ClientMode, useMode } from "../../context/ModeProvider";

export function ServerPicture({ style, alt=""}: { style: string, alt: string}) {
    const server = useServerContext();
    const mode = useMode();
    const setServer = useActiveServerContext().setActiveServer;
    return (
        <div className={`rounded-full overflow-hidden ${style}`}>
            <img
                onClick={()=>{console.log("ciao");mode.setMode(ClientMode.Server);setServer(server)}}
                src={"/placeholder.png"}
                alt={server!==null ? server?.name : alt}
                className="text-[12px] w-full h-full object-cover bg-black"
                title={server!==null ? server?.name : alt}
            />
        </div>
    );
}