import React from "react";
import { Message } from "../types";

export const MessageContext = React.createContext<Message | undefined>(undefined);

export function useMessageContext() {
  const message = React.useContext(MessageContext);
  if (!message) {
    throw Error('tried to access messages context outside of participant context provider');
  }
  return message;
}