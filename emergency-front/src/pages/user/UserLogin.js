import React, { useContext, useState } from 'react';
import { Button, FloatingLabel, Form } from 'react-bootstrap';
import AlertDialog from '../components/AlertDialog';
import { LoginContext } from '../../context/LoginContextProvider';

const LoginForm = () => {
  const { loginUser, validMessage, errorMessage, dialogState, setDialogState } =
    useContext(LoginContext);

  const [user, setUser] = useState({
    userId: '',
    password: '',
  });

  const changeValue = (e) => {
    setUser({
      ...user, // 기존 user 객체 정보
      [e.target.name]: e.target.value,
    });
  };

  // context의 로그인 함수 호출
  const callLoginUser = async (e) => {
    e.preventDefault();
    await loginUser(user);
  };

  // 모달 닫기 핸들러
  const handleCloseDialog = () => {
    setDialogState({
      ...dialogState,
      open: false,
    });
  };

  return (
    <div className="container mt-5">
      <Form onSubmit={callLoginUser}>
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

        <AlertDialog
          open={dialogState.open}
          onClose={handleCloseDialog}
          message={dialogState.message}
          onConfirm={handleCloseDialog}
          type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
        />
      </Form>
    </div>
  );
};

export default LoginForm;
