import React, { createContext, useState } from 'react';
import { getMe } from '../apis/user';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    userId: '',
  });

  const loginCheck = async (accessToken) => {
    localStorage.setItem('accessToken', accessToken);

    try {
      const res = await getMe();

      console.log(res);
    } catch (err) {}
  };

  // 로그인 세팅
  const loginSetting = (userId) => {
    // 로그인 정보 세팅
    setLoggedIn(true);

    // 유저정보 세팅
    setUserInfo({ userId: userId });
  };

  const logoutSetting = () => {
    // 로그인 정보 세팅
    setLoggedIn(false);

    // 유저 정보 초기화
    setUserInfo(null);
  };

  return (
    <div>
      <LoginContext.Provider value={{ isLoggedIn }}>
        {children}
      </LoginContext.Provider>
    </div>
  );
};

export default LoginContextProvider;
