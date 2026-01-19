import { createContext, useContext, useEffect, ReactNode, useMemo, useState } from "react";
import { io, Socket } from "socket.io-client";

const SocketContext = createContext<Socket | null>(null);

export const SocketProvider = ({ children }: { children: ReactNode }) => {
  const [socket, setSocket] = useState<Socket | null>(null);

  useEffect(() => {
    const newSocket = io("http://localhost:4000", {
        transports: ["websocket"]
    });

    newSocket.on("connect", () => {
        console.log("Socket connesso con ID:", newSocket.id);
        newSocket.emit("login", { username: "ale", password: "password1" });
    });

    newSocket.on("message", (data) => {
        console.log("Messaggio dal server:", data);
    });

    setSocket(newSocket);
    return () => {
        newSocket.disconnect();
    };
}, []);

  return (
    <SocketContext.Provider value={socket}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => {
  return useContext(SocketContext);
};