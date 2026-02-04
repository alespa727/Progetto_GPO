import React from "react";
import { Section } from "../../types";
import { ParticipantContext } from "@livekit/components-react";
import { SectionContext } from "../../context/SezioneContext";
import { useServerContext } from "../../context/ServerContext";

export interface SezioneLoopProps {
  serverSections?: Section[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function SectionLoop({ serverSections, ...props }: SezioneLoopProps) {
  const sections = serverSections ? serverSections : useServerContext() ? useServerContext()?.sections : null;
  
  if(!sections) return;
  return (
    <div className="mb-1 flex-1 overflow-y-auto">
      {sections.map((section) => (
        <SectionContext.Provider value={section} key={section.id}>
            <div className="border-b border-white/10 rounded-t-[10px] ">
                {
                    props.children
                }
            </div>
        </SectionContext.Provider>
      ))}
    </div>
  );
}
