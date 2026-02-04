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
    allowedHosts: true,
    port: 5173,
    proxy: {
      // Proxy /server1 → http://localhost:4000
      '/server1': {
        target: 'http://localhost:4000',
        ws: true,
        changeOrigin: true,
        rewrite: (path) => {
          if (!path.startsWith('/socket.io')) return path.replace(/^\/server1/, '')
          return path;
        },
      }
      ,
      
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,

        cookiePathRewrite: "/",
        cookieDomainRewrite: "localhost"
      },

      '/images': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path,
      },
      '/files': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path,
      },
      '/dev': {
        target: 'http://localhost:8097',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev/, ''),
        ws: true,

        cookiePathRewrite: "/",
        cookieDomainRewrite: "localhost"
      },
    }
  }
});
