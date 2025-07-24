import React, { useContext, useEffect, useState } from 'react';
import { Container, Button, Card, ListGroup } from 'react-bootstrap';
import { useLocation, useSearchParams } from 'react-router-dom';
import {
  insertBookmark,
  existsBookmark,
  deleteBookmark,
} from '../../apis/bookmark';
import AlertDialog from '../../components/AlertDialog';
import { useAlertDialog } from '../../hooks/useAlertDialog';
import { LoginContext } from '../../context/LoginContextProvider';
import { getEmgcDtl } from '../../apis/emgc';
import Location from '../../components/Location';

const EmgcRltmDtl = () => {
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const [isBookmarked, setIsBookmarked] = useState(false);
  const [emgcDtl, setEmgcDtl] = useState({});
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
      hpId: hpId,
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

      console.log(res.data);

      if (res.data) {
        console.log('1111111111');
        setIsBookmarked(true);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchEmgcDtl = async (hpId) => {
    try {
      const res = await getEmgcDtl(hpId);

      if (res.status === 200) {
        console.log(res.data);
        setEmgcDtl(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (isLoggedIn && hpId) {
      chkBookmark();
    }
    fetchEmgcDtl(hpId);
  }, [isLoggedIn, hpId]);

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-5 fw-normal text-center">
        실시간 병상제공 병원 상세
      </h1>
      <div className="d-flex justify-content-between align-items-center mb-5">
        <h3 className="h3 fw-normal m-0">{emgcDtl.dutyName}</h3>
        {isBookmarked ? (
          <Button onClick={delBookmark}>즐겨찾기 제거</Button>
        ) : (
          <Button onClick={addBookmark}>즐겨찾기 추가</Button>
        )}
      </div>

      <div className="row">
        {/* 지도 영역 */}
        <div className="col-md-6 mb-3">
          {emgcDtl.dutyLat && emgcDtl.dutyLon && (
            <Location lat={emgcDtl.dutyLat} lng={emgcDtl.dutyLon} />
          )}
        </div>

        {/* 정보 카드 영역 */}
        <div className="col-md-6 mb-3">
          <Card className="shadow-sm border-0">
            <Card.Header className="bg-primary text-white fw-bold">
              기본 정보
            </Card.Header>
            <ListGroup variant="flush">
              <ListGroup.Item>
                <strong>주소:</strong> {emgcDtl.dutyAddr || '정보 없음'}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>전화번호:</strong> {emgcDtl.dutyTel || '정보 없음'}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>비고:</strong> {emgcDtl.dutyInf || '정보 없음'}
              </ListGroup.Item>
            </ListGroup>
          </Card>
        </div>
      </div>

      <div className="row mt-4">
        <div className="col">
          <div
            className="p-3 shadow-sm rounded"
            style={{ backgroundColor: '#f9f9f9', border: '1px solid #e0e0e0' }}
          >
            <h5
              className="mb-3"
              style={{
                borderBottom: '2px solid #007bff',
                paddingBottom: '6px',
                color: '#333',
                fontWeight: '600',
              }}
            >
              진료과목
            </h5>
            <ul
              style={{
                display: 'flex',
                flexWrap: 'wrap',
                paddingLeft: '1.2rem',
                listStyleType: 'disc',
                margin: 0,
              }}
            >
              {emgcDtl.dgidIdName &&
                emgcDtl.dgidIdName.split(',').map((item, index) => (
                  <li
                    key={index}
                    style={{
                      flexBasis: '25%',
                      listStylePosition: 'inside',
                      fontSize: '0.95rem',
                      marginBottom: '8px',
                    }}
                  >
                    {item.trim()}
                  </li>
                ))}
            </ul>
          </div>
        </div>
      </div>

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
