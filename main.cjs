const { app, BrowserWindow, ipcMain } = require('electron')
const path = require('path')

let n = 0

function createWindow() {
  const win = new BrowserWindow({
    width: 900,
    height: 600,
    autoHideMenuBar: true,
    frame: false,
    webPreferences: {
      partition: `persist:account${n++}`,
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false
    }
  })

  if (process.env.NODE_ENV === 'development') {
    win.loadURL('http://localhost:5173')
  } else {
    win.loadFile(path.join(__dirname, 'dist/index.html'))
  }


  ipcMain.handle('download-remote', async (event, url) => {
    try {
      const dl = await download(win, url, { directory: app.getPath('downloads') });
      return { success: true, path: dl.getSavePath() };
    } catch (err) {
      return { success: false, error: err.message };
    }
  });
}

app.whenReady().then(() => {
  createWindow()
  createWindow()
})
