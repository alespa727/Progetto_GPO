import React from "react";
import { Channel } from "../types";

export const ChannelContext = React.createContext<Channel | undefined>(undefined);

/**
 * Ensures that a participant is provided via context.
 * If not inside a `ParticipantContext`, an error is thrown.
 * @public
 */
export function useChannelContext() {
  const channel = React.useContext(ChannelContext);
  if (!channel) {
    throw Error('tried to access participant context outside of participant context provider');
  }
  return channel;
}