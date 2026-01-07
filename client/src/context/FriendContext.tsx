import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { User } from "../types";

const FriendContext = createContext<User[] | null>(null);

export const FriendProvider = ({ children }: { children: ReactNode }) => {
  const [friends, setFriends] = useState<User[] | null>(null);

  useEffect(() => {
    const fetchFriends = async () => {
      try {
        let list: User[] = [];
        const res = await axios.get("http://localhost:4000/friends")
        res.data.forEach((element: any) => {
            list.push(User.fromJSON(element));
        }); 
        setFriends(list);
      
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };

    fetchFriends();
  }, []);

  return (
    <FriendContext.Provider value={friends}>
      {children}
    </FriendContext.Provider>
  );
};

export const useFriends = () => {
  return useContext(FriendContext);
};
