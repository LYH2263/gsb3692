import axios, { type AxiosResponse, type InternalAxiosRequestConfig } from 'axios';
import { showToast } from 'vant';

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    const userId = localStorage.getItem('userId');
    if (userId) {
      if (!config.params) config.params = {};
      config.params.userId = userId;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data;
    if (res.code !== 200) {
      showToast(res.msg || 'Error');
      return Promise.reject(new Error(res.msg || 'Error'));
    }
    return res.data;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      showToast('登录已过期，请重新登录');
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('userInfo');
      // 可以选择跳转到登录页
      // window.location.href = '/login';
    } else {
      showToast(error.message || '网络错误');
    }
    return Promise.reject(error);
  }
);

export default instance;
