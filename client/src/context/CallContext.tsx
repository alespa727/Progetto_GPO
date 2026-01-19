import { createContext, useContext, useState, ReactNode } from "react";
import { Room } from "livekit-client";

interface ActiveRoomContext {
  room: Room | null;
  setRoom: (room: Room | null) => void;
}

const ActiveRoomContext = createContext<ActiveRoomContext | null>(null);


export const ActiveRoomProvider = ({ children }: { children: ReactNode }) => {
  const [room, setRoom] = useState<Room | null>(null);

  return (
    <ActiveRoomContext.Provider value={{ room, setRoom}}>
    {children}
    </ActiveRoomContext.Provider>
  );
};
export const useActiveRoomContext = () => {
  const room = useContext(ActiveRoomContext);
  if (!room) throw new Error("useActiveRoomContext deve essere usato dentro ActiveChatProvider");
  return room;
};
