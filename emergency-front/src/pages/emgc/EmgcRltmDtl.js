import React, { useContext, useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import { useLocation } from 'react-router-dom';
import { insertBookmark } from '../../apis/bookmark';
import AlertDialog from '../../components/AlertDialog';
import { useAlertDialog } from '../../hooks/useAlertDialog';
import { LoginContext } from '../../context/LoginContextProvider';

const EmgcRltmDtl = () => {
  const location = useLocation();
  const { mstSearchParams, page, hpId } = location.state || {};
  const { isLoggedIn } = useContext(LoginContext);

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  const addBookmark = async (e) => {
    e.preventDefault();

    if (!isLoggedIn) {
      showDialog('로그인 후 이용가능합니다.', 'error');
      return;
    }

    const hpInfo = {
      hpId: hpId,
    };

    try {
      const res = await insertBookmark(hpInfo);

      console.log(res);

      if (res.status === 200) {
        showDialog('즐겨찾기에 등록되었습니다.', 'success');
      }
    } catch (err) {
      console.log(err);
      if (err.response) {
        showDialog(err.response.data.message, 'error');
      }
    }
  };

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">
        실시간 병상제공 병원 상세
      </h1>
      <Button onClick={addBookmark}>즐겨찾기 추가 테스트</Button>

      {/* 모달 알림 */}
      <AlertDialog
        open={dialogState.open}
        onClose={closeDialog}
        onConfirm={closeDialog}
        message={dialogState.message}
        type={dialogState.type} // 'success' 또는 'error'로 알림 타입 전달
      />
    </Container>
  );
};

export default EmgcRltmDtl;
