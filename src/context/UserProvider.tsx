import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { Account } from "../types";
type AccountProps = {
  account: Account | null,
  setAccount: (account: Account) => void
}

const AccountContext = createContext<AccountProps | null>(null);


export const AccountProvider = ({ children }: { children: ReactNode }) => {
  const [account, setAccount] = useState<Account | null>(null);

  useEffect(()=>{
    if(account)
      console.log(account);
  }, [account]);

  return (
    <AccountContext.Provider value={{ account, setAccount }}>
      {children}
    </AccountContext.Provider>
  );
};

export const useAccount = () => {
  const props = useContext(AccountContext);
  if (props)
    return props.account;
};

export const useSetAccount = () => {
  const props = useContext(AccountContext);
  if (props && props !== undefined)
    return props.setAccount;
};

