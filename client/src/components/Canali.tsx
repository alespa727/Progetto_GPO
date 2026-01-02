import { useState } from "react";
import "../styles/Canali.css";

function Canali({server}: {server: string}) {
 
  return (
    <div className="canali">
        <div className="nome">
            {server}
        </div>
    </div>
  );
}

export default Canali;
