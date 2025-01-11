import React, { useContext, useState } from 'react';
import { Button, FloatingLabel, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 임포트
import AlertDialog from '../../components/AlertDialog';
import { login } from '../../apis/auth';
import api from '../../apis/api';
import { LoginContext } from '../../context/LoginContextProvider';

const LoginForm = () => {
  const navigate = useNavigate();
  const {} = useContext(LoginContext);

  const [user, setUser] = useState({
    userId: '',
    password: '',
  });
  const [errorMessage, setErrorMessage] = useState({
    errorCd: '',
    message: '',
  });
  const [validMessage, setValidMessage] = useState({
    errorCd: '',
    userId: '',
    password: '',
  });
  const [openDialog, setOpenDialog] = useState(false); // 모달 상태 추가
  const [dialogMessage, setDialogMessage] = useState(''); // 모달 메시지 상태
  const [dialogType, setDialogType] = useState(''); // 모달 타입 상태 ('success' or 'error')

  const changeValue = (e) => {
    setUser({
      ...user, // 기존 user 객체 정보
      [e.target.name]: e.target.value,
    });
  };

  const loginCheck = async (accessToken) => {
    // 로그인 직후 axios 객체의 header에 토큰 설정
    api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

    try {
    } catch (err) {}
  };

  const loginUser = async (e) => {
    e.preventDefault();

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

        // 로그인 성공시 모달 표시
        setDialogMessage('로그인에 성공하였습니다.');
        setDialogType('success');
        setOpenDialog(true);
      } else {
        // 로그인 실패시 모달 표시
        setDialogMessage('토큰이 유효하지 않습니다. 다시 시도해주세요.');
        setDialogType('error');
        setOpenDialog(true);
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
    }
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);

    if (dialogType === 'success') {
      navigate('/');
    }
  };

  return (
    <div className="container mt-5">
      <Form onSubmit={loginUser}>
        <h1 className="h3 mb-3 fw-normal text-center">로그인</h1>

        <FloatingLabel controlId="floatingInput" label="ID" className="mb-3">
          <Form.Control
            type="text"
            name="userId"
            placeholder="아이디를 입력하세요."
            value={user.userId}
            onChange={changeValue}
            isInvalid={!!validMessage.userId} // validMessage가 있을 경우 invalid 상태로 설정
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.userId} {/* validMessage에 들어있는 메시지를 출력 */}
          </Form.Control.Feedback>
        </FloatingLabel>

        <FloatingLabel
          controlId="floatingPassword"
          label="Password"
          className="mb-3"
        >
          <Form.Control
            type="password"
            name="password"
            placeholder="비밀번호를 입력하세요."
            value={user.password}
            onChange={changeValue}
            isInvalid={!!validMessage.password} // validMessage가 있을 경우 invalid 상태로 설정
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.password}
            {/* validMessage에 들어있는 메시지를 출력 */}
          </Form.Control.Feedback>
        </FloatingLabel>
        {/* 통신 에러 메시지가 있을 경우만 렌더링 */}
        {errorMessage && (
          <Form.Text className="text-danger">{errorMessage.message}</Form.Text>
        )}

        <Button variant="primary" type="submit" className="w-100 py-2">
          로그인
        </Button>
      </Form>

      <AlertDialog
        open={openDialog}
        onClose={handleCloseDialog}
        message={dialogMessage}
        onConfirm={handleCloseDialog}
        type={dialogType} // 'success' 또는 'error'로 알림 타입 전달
      />
    </div>
  );
};

export default LoginForm;
