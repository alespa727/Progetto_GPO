import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { Account } from "@/types";

const FriendContext = createContext<Account[] | null>(null);

export const FriendProvider = ({ children }: { children: ReactNode }) => {
  const [friends, setFriends] = useState<Account[] | null>(null);

  useEffect(() => {
    const fetchFriends = async () => {
      try {
    
        const res = await axios.get("http://localhost:8080/api/services/friends",
                {
                    withCredentials: true
                });
        console.log(res.data) 
        const friends : Account[] = res.data.friends.map((f: any)=>Account.fromJSON(f))
        
        setFriends(friends)
      } catch (err) {
        console.error("Errore nel fetch degli amici:", err);
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
