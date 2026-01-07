import { createContext, useContext, useState, ReactNode } from "react";

export enum ClientMode {
  Server,
  Chats
}

interface ModeContextType {
  mode: ClientMode;
  setMode: (mode: ClientMode) => void;
}

const ModeContext = createContext<ModeContextType | null>(null);

export const ModeProvider = ({ children }: { children: ReactNode }) => {
  const [mode, setMode] = useState<ClientMode>(ClientMode.Chats);

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
