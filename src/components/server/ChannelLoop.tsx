import React from "react";
import { ChannelType, Section, TextChannel } from "../../types";
import { SectionContext, useSectionContext } from "../../context/SezioneContext";
import { ChannelContext } from "../../context/ChannelContext";

export interface ChannelLoopProps {
  /** The template component to be used in the loop. */
  children: React.ReactNode;
}

export function ChannelLoop({...props }: ChannelLoopProps) {
    const section = useSectionContext();
    return (
        <>
        {section.channels.map((channel) => (
            <ChannelContext.Provider value={channel} key={channel.id}>
                <div className={"m-1 transition duration-400 ease-in-out" }>
                    {
                        props.children
                    }
                </div>
            </ChannelContext.Provider>
        ))}
        </>
    );
}
