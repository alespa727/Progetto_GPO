import { useEffect, useState } from "react";
import "../styles/Canali.css";
import { Section, Server } from "../types.tsx";
import { useActiveServerContext } from "../context/ActiveServerProvider.tsx";

function Canali() {

  const activeServer = useActiveServerContext().activeServer;
  if(!activeServer) return;
  const setActiveChannel = useActiveServerContext().setActiveChannel;
  const [sezioni, setSezioni] = useState<Section[]>(activeServer.sections);

  useEffect(()=>{
    setSezioni(activeServer.sections);
    setActiveChannel(activeServer.sections[0].channels[0]);
  }, [activeServer]);
  
  return (
    <div className="canali flex flex-col h-full pb-16">
      <div className="nome">
        <p>{activeServer.name}</p>
      </div>

      <div className="sezioni flex-1 overflow-y-auto p-2">
        {
          sezioni.map((sezione: Section, i: number) => (
            <div className="sezione mb-4" key={i}>
              <div className="nome font-bold mb-2">{sezione.title}</div>
              {sezione.channels.map((canale, j) => (
                <div
                  className="canale px-2 py-1 hover:bg-gray-200 cursor-pointer rounded"
                  key={j}
                  onClick={() => setActiveChannel(canale)}
                >
                  # {canale.title}
                </div>
              ))}
            </div>
          ))
        }
      </div>
    </div>
  );
}
export default Canali;
