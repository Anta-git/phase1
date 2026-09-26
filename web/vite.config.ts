import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Forward /api/* to the Spring Boot app so the browser sees one origin (no CORS setup needed in dev)
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
