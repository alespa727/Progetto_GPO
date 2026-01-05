import { useState } from "react";
import "../styles/Canali.css";
import { chat_type, chat, channel, server } from "../types.tsx";

function Canali({ server, onChannelClick }: { server: server, onChannelClick: (channel: chat) => void }) {
  var arr = getCanali(server);
  const [sezioni, setSezioni] = useState<channel[][]>(arr);

  return (
  <div className="canali flex flex-col h-full pb-[70px]">
  <div className="nome">
    <p>{server.name}</p>
  </div>

  <div className="sezioni flex-1 overflow-y-auto p-2">
    {sezioni.map((sezione: channel[], i: number) => (
      <div className="sezione mb-4" key={i}>
        <div className="nome font-bold mb-2">Titolo</div>
        {sezione.map((canale: channel, j: number) => (
          <div
            className="canale px-2 py-1 hover:bg-gray-200 cursor-pointer rounded"
            key={j}
            onClick={() => onChannelClick(canale)}
          >
            # {canale.name}
          </div>
        ))}
      </div>
    ))}
  </div>
</div>

  );
}

function getCanali(server: server): channel[][] {
  let nextId = 1;

  return [
    [server.default_channel],
    ["memes", "aa", "bb", "cc", "ciao", "chat"].map(name => ({
      id: nextId++,
      name,
      type: chat_type.CHANNEL
    })),
    ["gaming", "musica", "film"].map(name => ({
      id: nextId++,
      name,
      type: chat_type.CHANNEL
    })),
    ["annunci", "bot"].map(name => ({
      id: nextId++,
      name,
      type: chat_type.CHANNEL
    })),
  ];
}

export default Canali;
