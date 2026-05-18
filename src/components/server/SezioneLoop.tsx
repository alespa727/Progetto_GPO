import React from "react";
import { Section } from "../../types";
import { ParticipantContext } from "@livekit/components-react";
import { SectionContext } from "../../context/SezioneContext";
import { useServerContext } from "../../context/ServerContext";
import { motion } from "framer-motion";

export interface SezioneLoopProps {
  serverSections?: Section[];
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function SectionLoop({ serverSections, ...props }: SezioneLoopProps) {
  const sections = serverSections ? serverSections : useServerContext() ? useServerContext()?.sections : null;

  if (!sections) return;
  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: 8, scale: 0.97 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, y: -8, scale: 0.97 }}
      transition={{
        type: "spring",
        stiffness: 260,
        damping: 20,
        mass: 0.8,
      }}
      whileHover={{ scale: 1 }}
      className={`
        bg-white/5
        hover:bg-white/10
        transition-colors
        w-full
        mb-1  overflow-y-auto
        cursor-pointer
        rounded-[var(--radius)]
        active:brightness-95
      `}
    >
      {sections.map((section) => (
        <SectionContext.Provider value={section} key={section.id}>
          <div className=" rounded-t-[10px] ">
            {
              props.children
            }
          </div>
        </SectionContext.Provider>
      ))}
    </motion.div>

  );
}
