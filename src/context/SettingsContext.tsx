import { createContext, useContext, useState, ReactNode } from "react";
import { PrivateChatResponse } from "../types";

interface SettingsContext {
  state: boolean;
  setState: (state: boolean) => void;
}

const SettingsContext = createContext<SettingsContext | null>(null);

export const SettingsStatusProvider = ({ children }: { children: ReactNode }) => {
  const [state, setState] = useState<boolean>(false);

  return (
    <SettingsContext.Provider value={{ state, setState }}>
      {children}
    </SettingsContext.Provider>
  );
};

export const useSettingsStatusContext = () => {
  const settings = useContext(SettingsContext);
  if (!settings) throw new Error("useSettingsStatusContext deve essere usato dentro SettingsContext");
  return settings;
};
