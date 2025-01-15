import React, { createContext, useEffect, useState } from 'react';
import { getMe } from '../apis/user';
import { login, logout } from '../apis/auth';
import AlertDialog from '../components/AlertDialog';
import { useNavigate } from 'react-router-dom';

export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  // 로그인 여부
  const [isLoggedIn, setLoggedIn] = useState(false);

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

  // dialog 상태 관리 (현재 페이지)
  const [dialogState, setDialogState] = useState({
    open: false,
    message: '', // 모달 메시지 상태
    type: '', // 모달 타입 상태 ('success' or 'error')
    cd: '',
  });

  const showDialog = (message, type) => {
    setDialogState({
      open: true,
      message: message,
      type: type,
    });
  };

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
        loginCheck(accessToken);
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
    const splitToken = accessToken.split(' ')[1]; // Bearer 제거거
    localStorage.setItem('accessToken', splitToken);
    try {
      const res = await getMe(); // 리턴되는 값이 있어야 await가 작동

      console.log(res.data);

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
      console.log(res);

      if (res.status === 200) {
        logoutSetting();
        showDialog('로그아웃 되었습니다.', 'success');
      }
    } catch (err) {
      showDialog('로그아웃에 실패하였습니다.', 'error');
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
    loginCheck(localStorage.getItem('accessToken'));
  }, []); // 컴포넌트가 마운트 될때 한번만 실행

  // 모달 닫기 핸들러
  const handleCloseDialog = () => {
    setDialogState({
      ...dialogState,
      open: false,
    });
  };

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
          loginSetting,
          logoutSetting,
        }}
      >
        {children}
      </LoginContext.Provider>

      <AlertDialog
        open={dialogState.open}
        onClose={handleCloseDialog}
        message={dialogState.message}
        onConfirm={handleCloseDialog}
        type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
      />
    </div>
  );
};

export default LoginContextProvider;
