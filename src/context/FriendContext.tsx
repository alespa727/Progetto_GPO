import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import axios from "axios";
import { Account, endpoint2 } from "@/types";
import { useAccount } from "./UserProvider";

const FriendContext = createContext<Account[] | null>(null);

export const FriendProvider = ({ children }: { children: ReactNode }) => {
  const [friends, setFriends] = useState<Account[] | null>(null);
  const account = useAccount();
  useEffect(() => {
    const fetchFriends = async () => {
      try {
        if(!account) return;
        const res = await axios.get(endpoint2+"/services/friends",
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
  }, [account]);

  return (
    <FriendContext.Provider value={friends}>
      {children}
    </FriendContext.Provider>
  );
};

export const useFriends = () => {
  return useContext(FriendContext);
};
