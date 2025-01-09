import api from './api';

export const login = (user) =>
  api.post('/api/v1/auth/login', user, {
    withCredentials: true,
  });

export const getUserInfoFromToken = (accessToken) => {};

export const signUp = (user) => api.post('/api/v1/auth/signup', user);
