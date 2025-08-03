import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  base: '/dev/bot/ozon/helper/miniapp/',
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  build: {
    outDir: '../src/main/resources/static/miniapp',
    emptyOutDir: true
  },
  server: {
    port: 5173,
    proxy: {
      '/dev/bot/ozon/helper/save': 'http://localhost:1212'
    }
  }
})
