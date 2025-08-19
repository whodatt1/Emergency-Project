import apiAuth from './apiAuth';

export const getMe = () => apiAuth.post('/api/v1/user/me');

export const chkMe = (user) => apiAuth.post('/api/v1/user/chk', user);

export const getMeDetail = () => apiAuth.post('/api/v1/user/medetail');

export const modMe = (user) => apiAuth.post('/api/v1/user/modme', user);
