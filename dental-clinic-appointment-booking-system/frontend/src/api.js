import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083', // API Gateway base URL
});

// Attach JWT token to all requests except login/register
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  const isAuthEndpoint =
    config.url.includes('/auth/login') ||
    config.url.includes('/auth/doctor/login') ||
    config.url.includes('/auth/register');
  if (token && !isAuthEndpoint) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
