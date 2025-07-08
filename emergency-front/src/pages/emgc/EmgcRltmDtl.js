import React, { useContext, useEffect, useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import { useLocation, useSearchParams } from 'react-router-dom';
import {
  insertBookmark,
  existsBookmark,
  deleteBookmark,
} from '../../apis/bookmark';
import AlertDialog from '../../components/AlertDialog';
import { useAlertDialog } from '../../hooks/useAlertDialog';
import { LoginContext } from '../../context/LoginContextProvider';

const EmgcRltmDtl = () => {
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const [isBookmarked, setIsBookmarked] = useState(false);
  const { mstSearchParams, page } = location.state || {};
  const { isLoggedIn } = useContext(LoginContext);

  const hpIdFromState = location.state?.hpId;
  // fcm으로 넘어올 경우
  const hpIdFromQuery = searchParams.get('hpid');
  const hpId = hpIdFromState || hpIdFromQuery;

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  const addBookmark = async (e) => {
    e.preventDefault();

    if (!isLoggedIn) {
      showDialog('로그인 후 이용가능합니다.', 'error');
      return;
    }

    const hpInfo = {
      hpId: hpId || hpIdFromQuery,
    };

    try {
      const res = await insertBookmark(hpInfo);

      console.log(res);

      if (res.status === 200) {
        setIsBookmarked(true);
        showDialog('즐겨찾기에 등록되었습니다.', 'success');
      }
    } catch (err) {
      console.log(err);
      if (err.response) {
        showDialog(err.response.data.message, 'error');
      }
    }
  };

  const delBookmark = async (e) => {
    e.preventDefault();

    if (!isLoggedIn) {
      showDialog('로그인 후 이용가능합니다.', 'error');
      return;
    }

    const hpInfo = {
      hpId: hpId,
    };

    try {
      const res = await deleteBookmark(hpInfo);

      console.log(res);

      if (res.status === 200) {
        setIsBookmarked(false);
        showDialog('즐겨찾기가 제거되었습니다.', 'success');
      }
    } catch (err) {
      console.log(err);
      if (err.response) {
        showDialog(err.response.data.message, 'error');
      }
    }
  };

  const chkBookmark = async () => {
    if (!isLoggedIn) return;

    try {
      const res = await existsBookmark(hpId);

      if (res.data) {
        console.log('1111111111');
        setIsBookmarked(true);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (isLoggedIn && hpId) {
      chkBookmark();
    }
  }, [isLoggedIn, hpId]);

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">
        실시간 병상제공 병원 상세
      </h1>
      {isBookmarked ? (
        <Button onClick={delBookmark}>즐겨찾기 제거</Button>
      ) : (
        <Button onClick={addBookmark}>즐겨찾기 추가</Button>
      )}

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
