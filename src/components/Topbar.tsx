import { useRef, useState } from "react";
import "../styles/Topbar.css";

function Topbar() {
  const topbar = useRef<HTMLDivElement>(null);

  return (
    <div className="topbar q-electron-drag">
      <div className="drag-layer" ref={topbar}></div>
      <div className="title">Discord</div>
    </div>
  );
}

export default Topbar;
