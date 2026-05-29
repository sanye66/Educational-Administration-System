import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/auth': {
        target: 'http://localhost:8081',  // 直接访问认证服务
        changeOrigin: true,
      },
      '/user': {
        target: 'http://localhost:8082',  // 直接访问用户服务
        changeOrigin: true,
      },
      '/teacher': {
        target: 'http://localhost:8083',  // 直接访问教师服务
        changeOrigin: true,
      },
      '/student': {
        target: 'http://localhost:8084',  // 直接访问学生服务
        changeOrigin: true,
      },
      '/course': {
        target: 'http://localhost:8085',  // 直接访问课程服务
        changeOrigin: true,
      },
      '/classroom': {
        target: 'http://localhost:8086',  // 直接访问教室服务
        changeOrigin: true,
      },
      '/selection': {
        target: 'http://localhost:8087',  // 直接访问选课服务
        changeOrigin: true,
      },
      '/grade': {
        target: 'http://localhost:8088',  // 直接访问成绩服务
        changeOrigin: true,
      },
      '/exam': {
        target: 'http://localhost:8089',  // 直接访问考试服务
        changeOrigin: true,
      },
      '/schedule': {
        target: 'http://localhost:8090',  // 直接访问排课服务
        changeOrigin: true,
      },
      '/graduation': {
        target: 'http://localhost:8091',  // 直接访问毕设服务
        changeOrigin: true,
      },
      '/evaluation': {
        target: 'http://localhost:8092',  // 直接访问评价服务
        changeOrigin: true,
      }
    }
  },
  resolve: {
    alias: {
      '@': '/src'
    }
  }
})
