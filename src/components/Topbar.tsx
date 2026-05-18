import { useEffect, useRef, useState } from "react";
import "../styles/Topbar.css";

function Topbar() {
  const topbar = useRef<HTMLDivElement>(null);
  const [header, setHeader] = useState<string>("Astro")
 

  return (
    <div className="topbar q-electron-drag">
      <div className="drag-layer" ref={topbar}></div>
      <div className="text-(--text) text-center w-full">{header}</div>
    </div>
  );
}

export default Topbar;
