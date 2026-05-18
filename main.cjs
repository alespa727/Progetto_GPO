const { app, BrowserWindow, ipcMain, net } = require('electron')
const path = require('path')

let mainWin = null

function checkConnection() {
  return net.isOnline()
}

function createWindow() {
  mainWin = new BrowserWindow({
    width: 900,
    height: 600,
    autoHideMenuBar: true,
    frame: false,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false
    }
  })
  mainWin.loadFile(
    path.join(__dirname, "./dist/index.html")
  );
  /*
  if (checkConnection()) {
    mainWin.loadURL('http://localhost:5173')
  } else {
  
    const interval = setInterval(() => {
      if (checkConnection()) {
        mainWin.loadURL('https://ale727.duckdns.org')
        clearInterval(interval)
      }
    }, 5000)
  }*/
}

ipcMain.handle('download-remote', async (event, url) => {
  try {
    const dl = await download(mainWin, url, { directory: app.getPath('downloads') })
    return { success: true, path: dl.getSavePath() }
  } catch (err) {
    return { success: false, error: err.message }
  }
})

app.whenReady().then(() => {
  createWindow()

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})