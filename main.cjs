const { app, BrowserWindow } = require('electron')
const path = require('path')

let n = 0

function createWindow() {
  const win = new BrowserWindow({
    width: 900,
    height: 600,
    autoHideMenuBar: true,
    frame: false,
    webPreferences: {
      partition: `persist:account${n++}`, // 👈 QUI
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false
    }
  })

  if (process.env.NODE_ENV === 'development') {
    win.loadURL('http://localhost:5000')
  } else {
    win.loadFile(path.join(__dirname, 'dist/index.html'))
  }
}

app.whenReady().then(() => {
  createWindow()
  createWindow()
})
