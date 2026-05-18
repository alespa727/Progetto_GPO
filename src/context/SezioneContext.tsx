import React from "react";
import { Section } from "../types";

export const SectionContext = React.createContext<Section | undefined>(undefined);

/**
 * Ensures that a participant is provided via context.
 * If not inside a `ParticipantContext`, an error is thrown.
 * @public
 */
export function useSectionContext() {
  const section = React.useContext(SectionContext);
  if (!section) {
    throw Error('tried to access participant context outside of participant context provider');
  }
  return section;
}