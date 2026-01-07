import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './styles/index.css'
import Topbar from './components/Topbar.tsx'
import Menu from './components/Menu.tsx'
import { SocketProvider } from './context/SocketProvider.tsx'
import { UserProvider } from './context/UserProvider.tsx'
import { FriendProvider } from './context/FriendContext.tsx'
import { ServerProvider } from './context/ServerContext.tsx'
import { ModeProvider } from './context/ModeProvider.tsx'
import { ActiveServerProvider } from './context/ActiveServerProvider.tsx'
import { ChatProvider } from './context/ChatProvider.tsx'
import { ActiveChatProvider } from './context/ActiveChatProvider.tsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
   <UserProvider>
      <SocketProvider>
        <ServerProvider>
          <ActiveServerProvider>
            <FriendProvider>
              <ChatProvider>
                <ActiveChatProvider>
                     <ModeProvider>
                    <Topbar />
                    <Menu />
                  </ModeProvider>
                </ActiveChatProvider>
              </ChatProvider>
            </FriendProvider>
          </ActiveServerProvider>
        </ServerProvider>
      </SocketProvider>
    </UserProvider>

  </StrictMode>,
)
