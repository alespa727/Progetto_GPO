const { contextBridge, clipboard, ipcRenderer, desktopCapturer } = require('electron')

contextBridge.exposeInMainWorld('electronAPI', {
  copy: (text) => {
    clipboard.writeText(text)
    return true
  },

  paste: () => {
    return clipboard.readText()
  },

  getSources: (opts) => desktopCapturer.getSources(opts),

  downloadRemote: (url) => ipcRenderer.invoke('download-remote', url),

  downloadLocal: (filename, extension) =>
    ipcRenderer.invoke('download-local', filename, extension)
})