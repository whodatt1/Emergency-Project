import apiAuth from './apiAuth';

export const getEmgcMstList = (params) =>
  apiAuth.post('/api/v1/emgc/mst', { params });
