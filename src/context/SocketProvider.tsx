import { createContext, useContext, useEffect, ReactNode, useState } from "react";
import { io, Socket } from "socket.io-client";
import { useSetAccount } from "./UserProvider";
import { ClientHttp, endpoint2 } from "@/types";
import axios from "axios";

const SocketContext = createContext<Socket | null>(null);
const SocketStatus = createContext<boolean>(false);

export const SocketProvider = ({ children }: { children: ReactNode }) => {
  const [socket, setSocket] = useState<Socket | null>(null);
  const [status, setStatus] = useState<boolean>(false);
  const [token, setToken] = useState<string | null>(localStorage.getItem("accessToken"));
  const setAccount = useSetAccount();

  useEffect(() => {
    if (!token) return;

    // Evita doppie connessioni
    if (socket?.connected) return;

    console.log("connecting..");
    const newSocket = io("", {
      path: "/server1/socket.io",
      auth: { token }
    });

    newSocket.on("connect_error", (err) => {
      console.log("connect_error:", err.message, err);
    });
    newSocket.on("connect", () => {
      console.log("Socket connesso con ID:", newSocket.id);
      setStatus(true);
    });

    newSocket.on("disconnect", async (reason) => {
      console.log("Disconnesso:", reason);
      setStatus(false);

      const res = await axios.post(
        endpoint2 + "/login",
        {},
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json"
          }
        }
      );

      setToken(res.data.accessToken)

    });

    setSocket(newSocket);

    return () => {
      newSocket.disconnect();
    };
  }, [token]);

  return (
    <SocketContext.Provider value={socket}>
      <SocketStatus.Provider value={status}>
        {children}
      </SocketStatus.Provider>
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);
export const useSocketStatus = () => useContext(SocketStatus);