import api from './api';

export const getEmgcMstList = (params) =>
  api.get('/api/v1/emgc/mst', { params });

export const getEmgcDtl = (hpId) => api.get(`/api/v1/emgc/dtl/${hpId}`);
