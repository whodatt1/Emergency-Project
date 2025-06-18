import React, { useState } from 'react';
import { Button, FloatingLabel, Form, Container } from 'react-bootstrap';
import AlertDialog from '../../components/AlertDialog';
import DaumPC from '../../components/DaumPC';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 임포트
import { signUp } from '../../apis/auth';
import { useAlertDialog } from '../../hooks/useAlertDialog';

const UserJoin = () => {
  const navigate = useNavigate();

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  const [user, setUser] = useState({
    userId: '',
    password: '',
    email: '',
    hp: '',
    postCd: '',
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
    hp: '',
    postCd: '',
    address: '',
  });

  const [showPostCode, setShowPostCode] = useState(false);

  const changeValue = (e) => {
    const { name, value } = e.target;

    if (name === 'hp') {
      const formattedValue = formatPhoneNumber(value);
      setUser({
        ...user,
        [name]: formattedValue,
      });
    } else {
      setUser({
        ...user,
        [name]: value,
      });
    }
  };

  const formatPhoneNumber = (data) => {
    const numbers = data.replace(/[^0-9]/g, '');

    if (numbers.length <= 3) {
      return numbers;
    } else if (numbers.length <= 7) {
      return `${numbers.slice(0, 3)}-${numbers.slice(3)}`; // 4 ~ 7 자리
    } else {
      return `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(
        7,
        11,
      )}`; // 자리 이상인 경우
    }
  };

  const handleAddressComplete = (data) => {
    setUser({
      ...user,
      postCd: data.zonecode,
      address: data.address,
    });
    setShowPostCode(false);
  };

  const handleAddressSearch = () => {
    setShowPostCode(true);
  };

  const joinUser = async (e) => {
    e.preventDefault();
    // 기존 데이터 초기화
    setValidMessage({
      errorCd: '',
      userId: '',
      password: '',
      email: '',
      hp: '',
      postCd: '',
      address: '',
    });

    setErrorMessage({
      errorCd: '',
      message: '',
    });

    try {
      // 회원가입 요청
      const res = await signUp(user);

      console.log(res);

      if (res.status === 200) {
        showDialog('회원가입에 성공하였습니다.', 'success');
      }
    } catch (err) {
      console.log(err);
      if (err.response) {
        const data = err.response.data;

        if (data.errorCd === 'INVALID_FORM') {
          setValidMessage({
            errorCd: data.errorCd || '',
            userId: data.userId || '',
            password: data.password || '',
            email: data.email || '',
            hp: data.hp || '',
            postCd: data.postCd || '',
            address: data.address || '',
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
    closeDialog();

    if (dialogState.type === 'success') {
      navigate('/userLogin');
    }
  };

  return (
    <Container fluid className="mt-5">
      <Form onSubmit={joinUser}>
        <h1 className="h3 mb-3 fw-normal text-center">회원가입</h1>

        {/* 아이디 입력 */}
        <FloatingLabel controlId="floatingID" label="ID" className="mb-3">
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
            type="tel"
            name="hp"
            placeholder="휴대폰 번호를 입력하세요."
            value={user.hp}
            onChange={changeValue}
            isInvalid={!!validMessage.hp}
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.hp}
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
          controlId="floatingPostCd"
          label="Post Code"
          className="mb-3"
        >
          <Form.Control
            type="text"
            name="postCd"
            placeholder="우편번호를 입력하세요."
            value={user.postCd}
            onChange={changeValue}
            isInvalid={!!validMessage.postCd}
            disabled
          />
          <Form.Control.Feedback type="invalid">
            {validMessage.postCd}
          </Form.Control.Feedback>
        </FloatingLabel>

        <FloatingLabel
          controlId="floatingAddress"
          label="Address"
          className="mb-3"
        >
          <Form.Control
            type="text"
            name="address"
            placeholder="주소를 입력하세요."
            value={user.address}
            onChange={changeValue}
            isInvalid={!!validMessage.address}
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
        open={dialogState.open}
        onClose={handleCloseDialog}
        message={dialogState.message}
        onConfirm={handleCloseDialog}
        type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
      />
    </Container>
  );
};

export default UserJoin;
