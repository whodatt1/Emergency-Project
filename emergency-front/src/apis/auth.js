import api from './api';
import apiAuth from './apiAuth';

export const login = (user) =>
  api.post('/api/v1/auth/login', user, {
    withCredentials: true,
  });

export const signUp = (user) => api.post('/api/v1/auth/signup', user);

export const logout = () => apiAuth.post('/api/v1/auth/logout');

export const refresh = () => apiAuth.post('/api/v1/auth/refresh');
