import apiAuth from './apiAuth';

export const getMe = () => {
  apiAuth.post('/api/v1/user/me');
};
