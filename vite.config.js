import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'node:path'  

export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src")
    }
  },
  server: {
    host: true, 
    hmr: {
      protocol: "wss",               // secure WebSocket
      host: "https://weightlessly-tres-dagmar.ngrok-free.dev/",       // your ngrok URL                     // usually ngrok forwards HTTPS on 443
    },
  }
});
