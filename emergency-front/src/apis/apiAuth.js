import axios from 'axios';
import { login, refresh } from './auth';

const apiAuth = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    'Content-Type': 'application/json; charset=utf-8',
  },
  withCredentials: true,
});

var loginCheck = null;
var logoutUser = null;

// 메서드 주입
export const setAuthMethods = (loginCheckMethod, logoutUserMethod) => {
  loginCheck = loginCheckMethod;
  logoutUser = logoutUserMethod;
};

apiAuth.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

apiAuth.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.request;

    if (error.response?.status === 401 && !originalRequest.retry) {
      originalRequest.retry = true;

      try {
        const res = await refresh();

        const accessToken = res.headers['authorization'] || '';

        if (accessToken.startsWith('Bearer ')) {
          const splitToken = accessToken.split(' ')[1]; // Bearer 제거거

          if (loginCheck) await loginCheck(splitToken);

          const newToken = localStorage.getItem('accessToken');

          if (newToken) {
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
          }
          return apiAuth(originalRequest);
        }
      } catch (refError) {
        console.log('리프레쉬 기간 만료');

        if (logoutUser) await logoutUser();
        return Promise.reject(refError);
      }
    }

    return Promise.reject(error);
  },
);

export default apiAuth;
