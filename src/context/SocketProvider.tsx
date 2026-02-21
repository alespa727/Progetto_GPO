import { createContext, useContext, useEffect, ReactNode, useState } from "react";
import { io, Socket } from "socket.io-client";
import { useAccount, useSetAccount } from "./UserProvider";
import { endpoint2 } from "@/types";
import axios from "axios";

const SocketContext = createContext<Socket | null>(null);
const SocketStatus = createContext<boolean>(false);

// Gestore di connessione con il server consistente
export const SocketProvider = ({ children }: { children: ReactNode }) => {
  const [socket, setSocket] = useState<Socket | null>(null);
  const [status, setStatus] = useState<boolean>(false);
  const account = useAccount();
  const setAccount = useSetAccount();

  useEffect(() => {
    let newSocket: Socket;

    if (!setAccount) return;

    // Connessione
    const connect = async () => {
      if (!account) return;

      try {
        // Recupera il token di accesso
        const token = localStorage.getItem("accessToken");

        if(!token) {
          return;
        };
        
        // Si connette al socket del server e gli passa il token d'autorizzazione
        newSocket = io("", {
          path: "/server1/socket.io",
          transports: ["websocket"],
          auth: {
            token: token
          }
        });
        console.log("connecting..")

        // Connessione riuscita
        newSocket.on("connect", () => {
          console.log("Socket connesso con ID:", newSocket.id);
          setStatus(true);
        });

        // Disconnessione 
        newSocket.on('disconnect', (reason) => {
          console.log('Disconnesso dal server:', reason);
          setStatus(false);

          // Tenta il login con il refreshToken
          const execute = async () => {
            try {
              const res = await axios.post(
                endpoint2 + "/login",
                {
                  withCredentials: true,
                  headers: {
                    "Content-Type": "application/json"
                  }
                }
              );

              localStorage.setItem("accessToken", res.data.accessToken);

            } catch (error) {
              localStorage.clear();
            }

          }
          execute()
        });

        newSocket.on("incomingCall", (data) => {
          const { caller } = data;
          alert("Chiamata in arrivo...");
          console.log(caller)
        });

        // Setta il socket globale
        setSocket(newSocket);
      } catch (e) {

        setStatus(false);
        console.log("error", e);

      }
    };

    connect()
    
    // Evita memory leak
    return () => {

      if (newSocket)
        newSocket.disconnect();
    };
  }, [account]);

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