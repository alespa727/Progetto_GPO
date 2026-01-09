import { ReactNode } from "react";
import { ActiveChatProvider } from "./ActiveChatProvider";
import { ChatProvider } from "./ChatProvider";
import { FriendProvider } from "./FriendContext";
import { ActiveServerProvider } from "./ActiveServerProvider";
import { ServerProvider } from "./ServerContext";
import { SocketProvider } from "./SocketProvider";
import { UserProvider } from "./UserProvider";
import { ModeProvider } from "./ModeProvider";

export const ContextProvider = ({ children }: { children: ReactNode }) => {
    return (
        <UserProvider>
            <SocketProvider>
                <ServerProvider>
                    <ActiveServerProvider>
                        <FriendProvider>
                            <ChatProvider>
                                <ActiveChatProvider>
                                    <ModeProvider>
                                        {children}
                                    </ModeProvider>
                                </ActiveChatProvider>
                            </ChatProvider>
                        </FriendProvider>
                    </ActiveServerProvider>
                </ServerProvider>
            </SocketProvider>
        </UserProvider>
    );
};
