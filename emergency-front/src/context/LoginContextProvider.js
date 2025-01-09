import React, { createContext, useState } from 'react';
import api from '../apis/api';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    userId: '',
  });

  const loginCheck = (accessToken) => {};

  // 로그인 세팅
  const loginSetting = (userId, accessToken) => {
    // axios 객체의 header에 토큰 설정
    api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

    // 로그인 정보 세팅
    setLoggedIn(true);

    // 유저정보 세팅
    setUserInfo({ userId: userId });
  };

  const logoutSetting = () => {
    // axios 객체의 header 초기화
    api.defaults.headers.common.Authorization = undefined;

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
