import { ReactNode } from "react";
import { ActiveChatProvider } from "./ActiveChatProvider";
import { ChatProvider } from "./ChatListContext";
import { FriendProvider } from "./FriendContext";
import { ActiveServerProvider } from "./ActiveServerProvider";
import { ServerProvider } from "./ServerListContext";
import { SocketProvider } from "./SocketProvider";
import { AccountProvider } from "./UserProvider";
import { ModeProvider } from "./ModeProvider";
import { SettingsStatusProvider } from "./SettingsContext";
import { RoomContext } from "@livekit/components-react";
import { ActiveRoomProvider } from "./CallContext";

export const ContextProvider = ({ children }: { children: ReactNode }) => {
    return (
        <AccountProvider>
            <SocketProvider>
                <ActiveRoomProvider>
                     <ServerProvider>
                        <ActiveServerProvider>
                            <FriendProvider>
                                <ChatProvider>
                                    <ActiveChatProvider>
                                        <SettingsStatusProvider>
                                            <ModeProvider>
                                                
                                                {children}
                                            </ModeProvider>
                                        </SettingsStatusProvider>
                                    </ActiveChatProvider>
                                </ChatProvider>
                            </FriendProvider>
                        </ActiveServerProvider>
                    </ServerProvider>
                </ActiveRoomProvider>
            </SocketProvider>
        </AccountProvider>
    );
};
