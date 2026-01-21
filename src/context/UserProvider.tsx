import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { User } from "../types";

const UserContext = createContext<User | null>(null);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await axios.get("http://localhost:4000/whoami");
        let user: User = User.fromJSON(res.data);
        setUser(user);
        console.log("user: ");
        console.log(user);
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchUser();
  }, []);

  return (
    <UserContext.Provider value={user}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  return useContext(UserContext);
};
