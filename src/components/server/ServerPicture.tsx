import { useActiveServerContext } from "../../context/ActiveServerProvider";
import { useServerContext } from "../../context/ServerContext";
import { ClientMode, useMode } from "../../context/ModeProvider";

export function ServerPicture({ style, alt = "" }: { style: string, alt: string }) {
    const server = useServerContext();
    const mode = useMode();
    const setServer = useActiveServerContext().setActiveServer;
    return (
        <div className="w-10 aspect-square rounded-full overflow-hidden">
            <img
                onClick={() => {
                    console.log("ciao");
                    mode.setMode(ClientMode.Server);
                    setServer(server);
                }}
                src="/placeholder.png"
                alt={server !== null ? server?.name : alt}
                title={server !== null ? server?.name : alt}
                className="w-full h-full object-cover"
            />
        </div>
    );
}