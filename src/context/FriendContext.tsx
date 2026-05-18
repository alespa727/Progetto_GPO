import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { Account, ClientHttp } from "@/types";
import { useAccount } from "./UserProvider";

const FriendContext = createContext<Account[] | null>(null);

export const FriendProvider = ({ children }: { children: ReactNode }) => {
  const [friends, setFriends] = useState<Account[] | null>(null);
  const account = useAccount();
  useEffect(() => {
    const fetchFriends = async () => {
      try {
        if(!account) return;

        const friends = await ClientHttp.fetchFriends();
        await setFriends(friends)
        console.log(friends);
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
