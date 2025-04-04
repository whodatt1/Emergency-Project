import apiAuth from './apiAuth';

export const getEmgcMstList = (params) =>
  apiAuth.get('/api/v1/emgc/mst', { params });
