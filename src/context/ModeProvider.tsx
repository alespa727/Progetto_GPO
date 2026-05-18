import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { useSetAccount } from "./UserProvider";
import axios from "axios";
import { Account, ClientHttp, endpoint2 } from "@/types";

export enum ClientMode {
  Server,
  Chats,
  Login,
  Registration
}

interface ModeContextType {
  mode: ClientMode;
  setMode: (mode: ClientMode) => void;
}

const ModeContext = createContext<ModeContextType | null>(null);

export const ModeProvider = ({ children }: { children: ReactNode }) => {
  const [mode, setMode] = useState<ClientMode>(ClientMode.Login);
  const setAccount = useSetAccount();
  useEffect(() => {
    const execute = async () => {
      try {
      const account = await ClientHttp.getProfile();
  
      if(!account){
          console.warn("Eseguire il login");
          return;
        }
      if (setAccount && account) {
        setAccount(account);
        setMode(ClientMode.Chats);
      }
      } catch (error) {
         console.error("Eseguire il login")
      }
     
    }
    execute()
  }, [])

  

  return (
    <ModeContext.Provider value={{ mode, setMode }}>
      {children}
    </ModeContext.Provider>
  );
};

export const useMode = () => {
  const context = useContext(ModeContext);
  if (!context) throw new Error("useMode deve essere usato dentro ClientModeProvider");
  return context;
};
