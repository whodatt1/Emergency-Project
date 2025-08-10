import api from './api';
import apiAuth from './apiAuth';

export const getBookmarkMstList = (params) =>
  apiAuth.get('/api/v1/bookmark/mst', { params });

export const insertBookmark = (hpInfo) =>
  apiAuth.post('/api/v1/bookmark/ins', hpInfo);

export const deleteBookmark = (hpInfo) =>
  apiAuth.post('/api/v1/bookmark/del', hpInfo);

export const existsBookmark = (hpId) =>
  apiAuth.get(`/api/v1/bookmark/exists/${hpId}`);
