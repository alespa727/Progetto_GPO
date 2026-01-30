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
import { TracksProvider } from "./TracksContext";

export const ContextProvider = ({ children }: { children: ReactNode }) => {
    return (
        <AccountProvider>
            <SocketProvider>
                <ActiveRoomProvider>
                    <TracksProvider>
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
                    </TracksProvider>
                </ActiveRoomProvider>
            </SocketProvider>
        </AccountProvider>
    );
};
