import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './styles/index.css'
import Sidebar from './components/Sidebar.tsx'
import Topbar from './components/Topbar.tsx'
import Menu from './components/Menu.tsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Topbar></Topbar>
    <Menu></Menu>
  </StrictMode>,
)
