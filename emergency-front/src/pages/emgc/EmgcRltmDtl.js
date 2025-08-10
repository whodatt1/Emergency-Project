import React, { useContext, useEffect, useMemo, useState } from 'react';
import {
  Container,
  Button,
  Card,
  ListGroup,
  Col,
  Row,
  Badge,
} from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useSearchParams } from 'react-router-dom';
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
  const [searchParams] = useSearchParams();

  const [isBookmarked, setIsBookmarked] = useState(false);
  const [emgcDtl, setEmgcDtl] = useState({});
  const { isLoggedIn } = useContext(LoginContext);
  const hpId = searchParams.get('hpId');

  const { dialogState, showDialog, closeDialog } = useAlertDialog();

  const gubun = searchParams.get('gubun');

  const filteredSearchParams = useMemo(() => {
    const params = new URLSearchParams(searchParams.toString());
    params.delete('hpId');

    if (gubun === '1') {
      params.delete('gubun');
    }

    return params.toString();
  }, [searchParams, gubun]);

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
                <strong>대표번호:</strong> {emgcDtl.dutyTel || '정보 없음'}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>응급실번호:</strong> {emgcDtl.dutyErTel || '정보 없음'}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>소개:</strong> {emgcDtl.dutyInf || '정보 없음'}
              </ListGroup.Item>
            </ListGroup>
          </Card>
        </div>
      </div>

      <Card className="shadow-sm border-0 mb-3">
        <Card.Header className="bg-success text-white fw-bold">
          진료과목
        </Card.Header>
        <Card.Body>
          <Row>
            {emgcDtl.dgidIdName &&
              emgcDtl.dgidIdName.split(',').map((item, index) => (
                <Col key={index} xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    {item.trim()}
                  </Badge>
                </Col>
              ))}
          </Row>
        </Card.Body>
      </Card>

      <Card className="shadow-sm border-0 mb-3">
        <Card.Header className="bg-danger text-white fw-bold">
          가용 의료자원
        </Card.Header>
        <Card.Body>
          <Row>
            {emgcDtl.ctAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  CT
                </Badge>
              </Col>
            )}
            {emgcDtl.mriAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  MRI
                </Badge>
              </Col>
            )}
            {emgcDtl.angioAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  혈관촬영기
                </Badge>
              </Col>
            )}
            {emgcDtl.ventiAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  인공호흡기
                </Badge>
              </Col>
            )}
            {emgcDtl.ventiPretAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  조산아 인공호흡기
                </Badge>
              </Col>
            )}
            {emgcDtl.incuAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  인큐베이터
                </Badge>
              </Col>
            )}
            {emgcDtl.crrtAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  CRRT
                </Badge>
              </Col>
            )}
            {emgcDtl.ecmoAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  ECMO
                </Badge>
              </Col>
            )}
            {emgcDtl.oxyAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  고압 산소 치료기
                </Badge>
              </Col>
            )}
            {emgcDtl.hypoAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  중심 체온 조절 유도기
                </Badge>
              </Col>
            )}
            {emgcDtl.etcDlvrRoomYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  분만실
                </Badge>
              </Col>
            )}
            {emgcDtl.etcBurnSpctrtYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  화상전용처치실
                </Badge>
              </Col>
            )}
            {emgcDtl.ambulAvailYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  구급차
                </Badge>
              </Col>
            )}
            {emgcDtl.icuDrugYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  약물중환자
                </Badge>
              </Col>
            )}
            {emgcDtl.neurolInptYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  신경과입원실
                </Badge>
              </Col>
            )}
            {emgcDtl.ventiYn === 'Y' && (
              <Col xs={6} md={3} className="mb-2">
                <Badge bg="light" text="dark" className="w-100 text-start">
                  VENTI(소아)
                </Badge>
              </Col>
            )}
          </Row>
        </Card.Body>
      </Card>

      <Card className="shadow-sm border-0 mb-3">
        <Card.Header className="bg-warning text-white fw-bold">
          실시간 병상정보
        </Card.Header>
        <Card.Body>
          <div className="mb-3">
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              응급실
            </h6>
            <Row>
              {emgcDtl.genStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급실 일반 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcErsGenBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcErsGenPressStddBedPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급실 일반격리 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcErsGenPressBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcErsNegPressStddBedPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급실 음압격리 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcErsNegPressBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIcuNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 중환자실 음압격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIcuNegPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIcuGenPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 중환자실 일반격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIcuGenPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIptNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 입원실 음압격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIptNegPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIptGenPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 입원실 일반격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIptGenPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIcuStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 중환자실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIcuPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcIptStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 입원실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcIptPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcPedIptStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 소아입원실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcPedIptPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.emgcPedIcuStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    응급전용 소아중환자실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.emgcPedIcuPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              중환자실
            </h6>
            <Row>
              {emgcDtl.icuNeuroStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 신경과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuNeuroPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuNeoStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 신생아{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuNeoPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuThorSurgStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 흉부외과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuThorSurgPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuGenStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 일반{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuGenPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuMedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 내과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuMedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuSurgStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 외과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuSurgStddPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuNeuroSurgStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 신경외과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuNeuroSurgPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuBurnStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 화상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuBurnPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuTraumaStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 외상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuTraumaPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuPedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 소아{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuPedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuCardioStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 심장내과{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuCardioPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.icuNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    중환자실 음압격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.icuNegPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              입원실
            </h6>
            <Row>
              {emgcDtl.iptGenStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    입원실 일반{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.iptGenPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.iptTraumaStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    입원실 외상전용{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.iptTraumaPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.iptPsyClosedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    입원실 정신과 폐쇄병동{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.iptPsyClosedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.iptNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    입원실 음압격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.iptNegPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              소아
            </h6>
            <Row>
              {emgcDtl.pedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    소아{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.pedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.pedNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    소아 음압격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.pedPressBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.pedGenPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    소아 일반격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.pedIsolBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              수술실
            </h6>
            <Row>
              {emgcDtl.etcOpStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    수술실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.opPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.etcTraumaOpStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    외상전용 수술실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.etcTraumaOpPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              격리
            </h6>
            <Row>
              {emgcDtl.isolareaNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    격리진료구역 음압격리 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.isolareaNegPressBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.isolareaGenPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    격리진료구역 일반격리 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.isolareaGenPressBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              감염
            </h6>
            <Row>
              {emgcDtl.infecDdcIcuStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    감염병 전담병상 중환자실{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.infecDdcIcuPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.infecDdcIcuNegPressStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    감염병 전담병상 중환자실 내 음압격리 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.infecDdcIcuNegPressPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.infecSevereBedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    감염병 전담병상 중증 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.infecSevereBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.infecSemiSevereBedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    감염병 전담병상 준-중증 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.infecSemiSevereBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
              {emgcDtl.infecMdrateBedStddPsn > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    감염병 전담병상 중등증 병상{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.infecMdrateBedPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
            <h6 className="fw-bold text-primary border-start border-4 border-primary ps-2">
              코호트
            </h6>
            <Row>
              {emgcDtl.chrtIsolStddCnt > 0 && (
                <Col xs={6} md={3} className="mb-2">
                  <Badge bg="light" text="dark" className="w-100 text-start">
                    코호트 격리{' '}
                    <span style={{ color: 'red', fontWeight: 'bold' }}>
                      [{emgcDtl.chrtIsolPsn}명 이용가능]
                    </span>
                  </Badge>
                </Col>
              )}
            </Row>
          </div>
        </Card.Body>
      </Card>

      <Link
        to={{
          pathname: gubun === '1' ? '/emgcRltmList' : '/bookmarkList',
          search: filteredSearchParams,
        }}
        className="text-decoration-none text-dark"
      >
        {/* 상세보기 버튼 */}
        <Button variant="primary" size="sm" className="ms-2">
          목록
        </Button>
      </Link>

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
