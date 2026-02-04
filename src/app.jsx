import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './styles/index.css'
import Topbar from './components/Topbar.tsx'
import Menu from './components/Menu.tsx'
import { ContextProvider } from './context/ContextProvider.tsx'

createRoot(document.getElementById('root')).render(
    <ContextProvider>
      <Topbar />
      
      <Menu />
    </ContextProvider>,
)


/*
createRoot(document.getElementById('root')).render(
    <ContextProvider>
      
    </ContextProvider>,
)



*/
