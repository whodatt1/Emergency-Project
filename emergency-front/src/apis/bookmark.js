import api from './api';
import apiAuth from './apiAuth';

export const insertBookmark = (hpInfo) =>
  apiAuth.post('/api/v1/bookmark/ins', hpInfo);
