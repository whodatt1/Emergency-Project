import React, { useState } from 'react';
import { Button, FloatingLabel, Form } from 'react-bootstrap';
import AlertDialog from '../../components/AlertDialog';
import DaumPC from '../../components/DaumPC';

const UserJoin = () => {
  const [user, setUser] = useState({
    userId: '',
    password: '',
    email: '',
    phone: '',
    postNo: '',
    address: '',
  });

  const [errorMessage, setErrorMessage] = useState({
    errorCd: '',
    message: '',
  });

  const [validMessage, setValidMessage] = useState({
    errorCd: '',
    userId: '',
    password: '',
    email: '',
    phone: '',
    postNo: '',
    address: '',
  });

  const [openDialog, setOpenDialog] = useState(false); // 모달 상태
  const [dialogMessage, setDialogMessage] = useState(''); // 모달 메시지
  const [dialogType, setDialogType] = useState(''); // 모달 타입
  const [showPostCode, setShowPostCode] = useState(false);

  const changeValue = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  const handleAddressComplete = () => {};

  const handleAddressSearch = () => {
    setShowPostCode(true);
  };

  const joinUser = (e) => {
    e.preventDefault();

    // 기존 데이터 초기화
    setValidMessage({
      errorCd: '',
      userId: '',
      password: '',
      email: '',
      phone: '',
      postNo: '',
      address: '',
    });

    setErrorMessage({
      errorCd: '',
      message: '',
    });

    // 회원가입 로직 (API 호출, 유효성 검사 등)
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  return (
    <div className="container mt-5">
      <Form onSubmit={joinUser}>
        <h1 className="h3 mb-3 fw-normal text-center">회원가입</h1>

        {/* 아이디 입력 */}
        <FloatingLabel controlId="floatingInput" label="ID" className="mb-3">
          <Form.Control
            type="text"
            name="userId"
            placeholder="아이디를 입력하세요."
            value={user.userId}
            onChange={changeValue}
            isInvalid={!!validMessage.userId}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.userId}
          </Form.Control.Feedback>
        </FloatingLabel>

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
            isInvalid={!!validMessage.password}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.password}
          </Form.Control.Feedback>
        </FloatingLabel>

        {/* 이메일 입력 */}
        <FloatingLabel controlId="floatingEmail" label="Email" className="mb-3">
          <Form.Control
            type="email"
            name="email"
            placeholder="이메일을 입력하세요."
            value={user.email}
            onChange={changeValue}
            isInvalid={!!validMessage.email}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.email}
          </Form.Control.Feedback>
        </FloatingLabel>

        {/* 휴대폰 입력 */}
        <FloatingLabel controlId="floatingPhone" label="Phone" className="mb-3">
          <Form.Control
            type="text"
            name="phone"
            placeholder="휴대폰 번호를 입력하세요."
            value={user.phone}
            onChange={changeValue}
            isInvalid={!!validMessage.phone}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.phone}
          </Form.Control.Feedback>
        </FloatingLabel>

        {/* 주소 검색 버튼 */}
        <Button
          variant="secondary"
          onClick={handleAddressSearch}
          className="w-100 mb-3"
        >
          주소 검색
        </Button>

        <FloatingLabel
          controlId="floatingInput"
          label="Post Code"
          className="mb-3"
        >
          <Form.Control
            type="text"
            name="postNo"
            placeholder="우편번호를 입력하세요."
            value={user.postNo}
            onChange={changeValue}
            isInvalid={!!validMessage.userId}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.postNo}
          </Form.Control.Feedback>
        </FloatingLabel>

        <FloatingLabel
          controlId="floatingInput"
          label="Address"
          className="mb-3"
        >
          <Form.Control
            type="text"
            name="address"
            placeholder="주소를 입력하세요."
            value={user.address}
            onChange={changeValue}
            isInvalid={!!validMessage.userId}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.address}
          </Form.Control.Feedback>
        </FloatingLabel>

        {/* DaumPC 모달 */}
        <DaumPC
          show={showPostCode}
          onClose={() => setShowPostCode(false)}
          onComplete={handleAddressComplete}
        />

        {/* 통신 에러 메시지가 있을 경우만 렌더링 */}
        {errorMessage && (
          <Form.Text className="text-danger">{errorMessage.message}</Form.Text>
        )}

        <Button variant="primary" type="submit" className="w-100 py-2">
          회원가입
        </Button>
      </Form>

      {/* 모달 알림 */}
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

export default UserJoin;
