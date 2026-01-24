const { contextBridge } = require('electron')

contextBridge.exposeInMainWorld('versions', {
  node: () => process.versions.node,
  chrome: () => process.versions.chrome,
  electron: () => process.versions.electron
})

const { contextBridge, clipboard } = require('electron');

contextBridge.exposeInMainWorld('clipboardAPI', {
  copy: (text) => clipboard.writeText(text),
  paste: () => clipboard.readText()
});
