import React, { createContext, useEffect, useState } from 'react';
import { getMe } from '../apis/user';
import { login, logout } from '../apis/auth';
import AlertDialog from '../components/AlertDialog';
import { setAuthMethods } from '../apis/apiAuth';
import { useAlertDialog } from '../hooks/useAlertDialog';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    userId: '',
  });

  // 에러 메시지
  const [errorMessage, setErrorMessage] = useState({
    errorCd: '',
    message: '',
  });

  // valid 메시지 (로그인 화면에서 사용)
  const [validMessage, setValidMessage] = useState({
    errorCd: '',
    userId: '',
    password: '',
  });

  const loginUser = async (user) => {
    // 기존 데이터 초기화
    setValidMessage({
      errorCd: '',
      userId: '',
      password: '',
    });
    setErrorMessage({
      errorCd: '',
      message: '',
    });

    try {
      const res = await login(user);
      console.log(res);

      const accessToken = res.headers['authorization'] || '';

      if (accessToken.startsWith('Bearer ')) {
        const splitToken = accessToken.split(' ')[1]; // Bearer 제거거

        loginCheck(splitToken);
        return true;
      } else {
        return false;
      }
    } catch (err) {
      if (err.response) {
        const data = err.response.data;

        if (data.errorCd === 'INVALID_FORM') {
          setValidMessage({
            errorCd: data.errorCd || '',
            userId: data.userId || '',
            password: data.password || '',
          });
        } else {
          setErrorMessage({
            errorCd: data.errorCd || '',
            message: data.message || '',
          });
        }
      }

      return false;
    }
  };

  const loginCheck = async (accessToken) => {
    localStorage.setItem('accessToken', accessToken);

    try {
      const res = await getMe(); // 리턴되는 값이 있어야 await가 작동

      if (res.data) {
        loginSetting(res.data.userId);
      } else {
        logoutUser();
      }
    } catch (err) {
      if (err.response) {
        logoutUser();
      }
    }
  };

  // 로그인 세팅
  const loginSetting = (userId) => {
    // 로그인 정보 세팅
    setLoggedIn(true);

    // 유저정보 세팅
    setUserInfo({ userId: userId });
  };

  // 로그아웃
  const logoutUser = async () => {
    try {
      const res = await logout();

      if (res.status === 200) {
        logoutSetting();
      }
    } catch (err) {
      console.log(err);
      logoutSetting();
    }
  };

  const logoutSetting = () => {
    // localStorage에서 accessToken 삭제
    localStorage.removeItem('accessToken');

    // 로그인 정보 세팅
    setLoggedIn(false);

    // 유저 정보 초기화
    setUserInfo(null);
  };

  useEffect(() => {
    // apiAuth에 메서드 주입
    setAuthMethods(loginCheck, logoutUser);

    const localAccessToken = localStorage.getItem('accessToken');

    // 항상 검사하면 서버요청이 계속됨 차라리 권한 필요한 요청이 들어왔을때 재로그인 시켜주는게 낫다.
    if (localAccessToken) {
      loginCheck(localAccessToken);
    }
  }, []);

  return (
    <div>
      <LoginContext.Provider
        value={{
          isLoggedIn,
          userInfo,
          validMessage,
          errorMessage,
          loginUser,
          loginCheck,
          logoutUser,
          loginSetting,
          logoutSetting,
        }}
      >
        {children}
      </LoginContext.Provider>

      <AlertDialog
        open={dialogState.open}
        onClose={closeDialog}
        onConfirm={closeDialog}
        message={dialogState.message}
        type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
      />
    </div>
  );
};

export default LoginContextProvider;
