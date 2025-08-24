import api from './api';
import apiAuth from './apiAuth';

export const getMe = () => apiAuth.post('/api/v1/user/me');

export const chkMe = (user) =>
  apiAuth.post(
    '/api/v1/user/chk',
    user,
    // withCredentials: true 옵션을 주어야 서버에서 내려주는 HttpOnly 쿠키(verifyToken 등)가
    // 브라우저에 저장되고 이후 요청 시 자동으로 함께 전송됨 (CORS 환경에서 필수)
    { withCredentials: true },
  );

export const getMeDetail = () =>
  apiAuth.post('/api/v1/user/medetail', { withCredentials: true });

export const outMeDetail = () =>
  apiAuth.post('/api/v1/user/outdetail', { withCredentials: true });

export const modMe = (user) => apiAuth.post('/api/v1/user/modme', user);
