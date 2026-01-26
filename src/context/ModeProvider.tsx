import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { useAccount, useSetAccount } from "./UserProvider";
import axios from "axios";
import { Account } from "@/types";

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

      const account = await axios.get(
        "http://localhost:8080/api/services/account"
        ,
        {
          withCredentials: true
        });

      const accObj: Account = Account.fromJSON(account.data)
      if (setAccount && accObj) {
        setAccount(accObj);
        setMode(ClientMode.Chats);
      }
      else
        console.error(account)
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
