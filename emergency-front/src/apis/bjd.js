import api from './api';

export const getSidoList = () => api.get('/api/v1/bjd/sido');

export const getGugunList = (sidoCd) => api.get(`/api/v1/bjd/gugun/${sidoCd}`);

export const getDongList = (gugunCd) => api.get(`/api/v1/bjd/dong/${gugunCd}`);
