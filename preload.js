const { contextBridge, desktopCapturer, clipboard, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('versions', {
  node: () => process.versions.node,
  chrome: () => process.versions.chrome,
  electron: () => process.versions.electron,
});

contextBridge.exposeInMainWorld('electronAPI', {
  // Clipboard
  copy: (text) => clipboard.writeText(text),
  paste: () => clipboard.readText(),

  // Screen sources
  getSources: (opts) => desktopCapturer.getSources(opts),

  // Download remoto
  downloadRemote: (url) => ipcRenderer.invoke('download-remote', url),

  // Download locale
  downloadLocal: (filename, extension) => ipcRenderer.invoke('download-local', filename, extension)
});
