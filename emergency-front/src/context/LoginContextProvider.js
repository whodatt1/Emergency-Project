import React, { createContext, useState } from 'react';
import api from '../apis/api';
import { getUserInfo } from '../apis/auth';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    userId: '',
  });

  // 로그인 세팅
  const loginSetting = (userId) => {
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
