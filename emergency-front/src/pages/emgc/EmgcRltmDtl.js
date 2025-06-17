import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { useLocation } from 'react-router-dom';
import { insertBookmark } from '../../apis/bookmark';

const EmgcRltmDtl = () => {
  const location = useLocation();
  const { mstSearchParams, page, hpId } = location.state || {};

  const hpInfo = {
    hpId: hpId,
  };

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">
        실시간 병상제공 병원 상세
      </h1>
      <Button onClick={() => insertBookmark(hpInfo)}>
        즐겨찾기 추가 테스트
      </Button>
    </Container>
  );
};

export default EmgcRltmDtl;
