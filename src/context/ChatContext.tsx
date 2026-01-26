import React from "react";
import { Chat } from "../types";

export const ChatContext = React.createContext<Chat | null>(null);

/**
 * Ensures that a participant is provided via context.
 * If not inside a `ParticipantContext`, an error is thrown.
 * @public
 */
export function useChatContext() {
  const chat = React.useContext(ChatContext);
  return chat;
}