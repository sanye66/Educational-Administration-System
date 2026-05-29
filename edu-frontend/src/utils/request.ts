import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import { message } from 'antd';
import type { R } from '@/types';

const request = axios.create({
  baseURL: '/',
  timeout: 15000,
  headers: { 
    'Content-Type': 'application/json; charset=utf-8'
  }
});

// 请求拦截器 - 注入Token
request.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器 - 统一错误处理
request.interceptors.response.use(
  (response) => {
    const data = response.data as R;
    if (data.code !== 200) {
      message.error(data.msg || '请求失败');
      if (data.code === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      return Promise.reject(new Error(data.msg));
    }
    return response;
  },
  (error: AxiosError<R>) => {
    const status = error.response?.status;
    const msg = error.response?.data?.msg;
    if (status === 401) {
      message.error('登录已过期，请重新登录');
      localStorage.removeItem('token');
      window.location.href = '/login';
    } else if (status === 403) {
      message.error('无权限访问');
    } else if (status === 429) {
      message.warning('请求过于频繁，请稍后再试');
    } else {
      message.error(msg || '网络异常，请稍后重试');
    }
    return Promise.reject(error);
  }
);

export default request;
