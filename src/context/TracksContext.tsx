// TracksProvider.tsx
import { createContext, useContext } from "react";
import { TrackReference, useTracks } from "@livekit/components-react";

const TracksContext = createContext<TrackReference[] | null>(null);

export const TracksProvider = ({ children }: { children: React.ReactNode }) => {
  const tracks = useTracks([]);

  return (
    <TracksContext.Provider value={tracks}>
      {children}
    </TracksContext.Provider>
  );
};

export const useTracksContext = () => {
  const ctx = useContext(TracksContext);
  if (!ctx) {
    throw new Error("useTracksContext deve essere usato dentro TracksProvider");
  }
  return ctx;
};
