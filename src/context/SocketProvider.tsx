import { createContext, useContext, useEffect, ReactNode, useState } from "react";
import { io, Socket } from "socket.io-client";

const SocketContext = createContext<Socket | null>(null);
const SocketStatus = createContext<boolean>(false);

export const SocketProvider = ({ children }: { children: ReactNode }) => {
  const [socket, setSocket] = useState<Socket | null>(null);
  const [status, setStatus] = useState<boolean>(false);

  useEffect(() => {
    let newSocket: Socket;
    const connect = async () => {
      await new Promise(resolve => setTimeout(resolve, 1000));

     
      try {

        newSocket = io("", {
          path: "/server1/socket.io",
          transports: ["websocket"]
        });
        console.log("connecting..")

        newSocket.on("connect", () => {
          console.log("Socket connesso con ID:", newSocket.id);
          setStatus(true);
          newSocket.emit("login", { username: "ale", password: "password1" });
        });

        newSocket.on('disconnect', (reason) => {
          console.log('Disconnesso dal server:', reason);
          setStatus(false);
        });

        newSocket.on("message", (data) => {
          console.log("Messaggio dal server:", data);
        });

        setSocket(newSocket);
      } catch (e) {
        setStatus(false);
        console.log("error", e);
      }

    };
    connect()
    return () => {
      newSocket.disconnect();
    };
  }, []);

  return (
    <SocketContext.Provider value={socket}>
      <SocketStatus.Provider value={status}>
        {children}
      </SocketStatus.Provider>
    </SocketContext.Provider>
  );
};

export const useSocket = () => {
  return useContext(SocketContext);
};

export const useSocketStatus = () => {
  return useContext(SocketStatus);
};