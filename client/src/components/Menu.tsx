import { useState } from "react";
import Sidebar from "./Sidebar.tsx";
import Content from "./Content.tsx"
import "../styles/Menu.css";


function Menu() {
  const [servers, setServers] = useState(["server di ale.", "server di Tommy"]);
  const [mode, setMode]= useState<string>("");
  return (
    <div className="app">
        <Sidebar servers={servers} setMode={setMode}></Sidebar>
        <Content mode={mode}></Content>
    </div>
  );
}

export default Menu;
