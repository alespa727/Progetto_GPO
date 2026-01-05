const { app, BrowserWindow } = require('electron')
const path = require('path')

function createWindow() {
  const win = new BrowserWindow({
    width: 900,
    height: 600,
    autoHideMenuBar: true,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // Se siamo in modalità sviluppo (vite), carichiamo il server locale
  if (process.env.NODE_ENV === 'development') {
    win.loadURL('http://localhost:5174')
  } else {
    // In produzione, carichiamo i file buildati
    win.loadFile(path.join(__dirname, 'dist/index.html'))
  }
}

app.whenReady().then(() => {
  createWindow()
})
