import { createContext, useContext, useState, Dispatch, SetStateAction } from "react";

interface AudioControlsContextType {
  muted: boolean;
  setMuted: Dispatch<SetStateAction<boolean>>;
  deafened: boolean;
  setDeafened: Dispatch<SetStateAction<boolean>>;
}

const AudioControlsContext = createContext<AudioControlsContextType | null>(null);

export function AudioControlsProvider({ children }: { children: React.ReactNode }) {
  const [muted, setMuted] = useState(false);
  const [deafened, setDeafened] = useState(false);

  return (
    <AudioControlsContext.Provider value={{ muted, setMuted, deafened, setDeafened }}>
      {children}
    </AudioControlsContext.Provider>
  );
}

export function useAudioControls() {
  const ctx = useContext(AudioControlsContext);
  if (!ctx) throw new Error("useAudioControls must be used within AudioControlsProvider");
  return ctx;
}