import "../../styles/Canali.css";
import { SectionLoop } from "./SezioneLoop.tsx";
import { SectionName } from "./SectionName.tsx";
import { ChannelLoop } from "./ChannelLoop.tsx";
import { ChannelName } from "./ChannelName.tsx";
import { ServerName } from "./ServerName.tsx";
import { useServerContext } from "../../context/ServerContext.tsx";

function Server() {

  const activeServer = useServerContext();
  if (!activeServer) return;

  return (
      <div className="w-[21.27%] bg-[#1e1e2e] min-w-50 max-w-60 font-medium flex flex-col h-full pb-16">
        <ServerName></ServerName>

        <SectionLoop>
          <SectionName onClick={()=>{}}></SectionName>
          <ChannelLoop>
            <ChannelName></ChannelName>
          </ChannelLoop>
        </SectionLoop>
      </div>
  );
}
export default Server;
