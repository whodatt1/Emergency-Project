import React, { createContext, useState } from 'react';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

  // 유저 정보
  const [userInfo, setUserInfo] = useState({});

  // 로그인 세팅
  const loginSetting = (accessToken) => {};

  const logout = () => {
    setLoggedIn(false);
  };

  return (
    <div>
      <LoginContext.Provider value={{ isLoggedIn, logout }}>
        {children}
      </LoginContext.Provider>
    </div>
  );
};

export default LoginContextProvider;
