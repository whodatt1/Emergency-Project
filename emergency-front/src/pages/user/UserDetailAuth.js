import React, { useState } from 'react';
import { Button, FloatingLabel, Form, Container } from 'react-bootstrap';
import AlertDialog from '../../components/AlertDialog';
import DaumPC from '../../components/DaumPC';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 임포트
import { chkMe } from '../../apis/user';
import { useAlertDialog } from '../../hooks/useAlertDialog';

const UserDetailAuth = () => {
  const navigate = useNavigate();

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  const [user, setUser] = useState({
    password: '',
  });

  const changeValue = (e) => {
    const { name, value } = e.target;

    setUser({
      ...user,
      [name]: value,
    });
  };

  const chkUser = async (e) => {
    e.preventDefault();

    try {
      // 회원가입 요청
      const res = await chkMe(user);

      console.log(res);

      if (res.status === 200) {
        // 보안상 http-only로 할 예정
        // 토큰을 사용하는 이유는 UserDetail url을 직접 쳐서 들어갈경우 해당 토큰으로 접근 여부를 관리하기 위해

        if (res.data) {
          showDialog('본인확인에 성공하였습니다.', 'success');
        } else {
          showDialog('본인확인에 실패하였습니다.', 'error');
        }
      }
    } catch (err) {
      console.log(err);
    }
  };

  const handleCloseDialog = () => {
    closeDialog();

    if (dialogState.type === 'success') {
      navigate('/userDetail');
    }
  };

  return (
    <Container fluid className="mt-5">
      <Form onSubmit={chkUser}>
        <h1 className="h3 mb-3 fw-normal text-center">개인정보 확인</h1>

        <div className="text-center mb-3">
          <small className="text-muted">
            개인정보에 접근하기 위해 비밀번호를 한번 더 입력하세요.
          </small>
        </div>

        {/* 비밀번호 입력 */}
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
          />
        </FloatingLabel>

        <Button variant="primary" type="submit" className="w-100 py-2">
          확인
        </Button>
      </Form>

      {/* 모달 알림 */}
      <AlertDialog
        open={dialogState.open}
        onClose={handleCloseDialog}
        message={dialogState.message}
        onConfirm={handleCloseDialog}
        type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
      />
    </Container>
  );
};

export default UserDetailAuth;
