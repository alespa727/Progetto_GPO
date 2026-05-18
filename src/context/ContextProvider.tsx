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
import { ActiveRoomProvider } from "./RoomContext";
import { TracksProvider } from "./TracksContext";
import { AudioControlsProvider } from "./AudioControlContext";
import { IncomingCallStatusProvider } from "./CallContext";
import { NotificationHandler } from "./NotificationContext";

export const ContextProvider = ({ children }: { children: ReactNode }) => {
    return (
        <AccountProvider>
            <SocketProvider>
                <ActiveRoomProvider>
                    <TracksProvider>
                        <AudioControlsProvider>
                            <ServerProvider>
                                <ActiveServerProvider>
                                    <FriendProvider>
                                        <ChatProvider>
                                            <ActiveChatProvider>
                                                <SettingsStatusProvider>
                                                    <ModeProvider>
                                                        <NotificationHandler>
                                                            <IncomingCallStatusProvider>

                                                                {children}
                                                            </IncomingCallStatusProvider>
                                                        </NotificationHandler>

                                                    </ModeProvider>
                                                </SettingsStatusProvider>
                                            </ActiveChatProvider>
                                        </ChatProvider>
                                    </FriendProvider>
                                </ActiveServerProvider>
                            </ServerProvider>
                        </AudioControlsProvider>
                    </TracksProvider>
                </ActiveRoomProvider>
            </SocketProvider>
        </AccountProvider>
    );
};
