import React from "react";
import { Message, Section } from "../../types";
import { MessageContext } from "../../context/MessageContext";

export interface ChatMessaggiLoopProps {
    /** The participants to loop over. Use `useParticipants()` hook to get participants. */
    messages: Message[];
    /** The template component to be used in the loop. */
    children: React.ReactNode;
}

export function ChatMessaggiLoop({ messages, ...props }: ChatMessaggiLoopProps) {
    return (
        <>
            {
                messages.map((m)=>{
                     <MessageContext.Provider value={m}>
                        {
                            props.children
                        }
                    </MessageContext.Provider>
                })
            }
           
        </>
    );
}
