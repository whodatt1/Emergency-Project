import React, { useEffect, useState } from 'react';
import { Container, Form, Button } from 'react-bootstrap';
import { getSidoList, getGugunList, getDongList } from '../../apis/bjd';

const EmgcRltmList = () => {
  const [sidoCd, setSidoCd] = useState('');
  const [gugunCd, setGugunCd] = useState('');
  const [dongCd, setDongCd] = useState('');

  const [sidoList, setSidoList] = useState([]);
  const [gugunList, setGugunList] = useState([]);
  const [dongList, setDongList] = useState([]);

  const fetchSidoList = async () => {
    try {
      const res = await getSidoList();

      console.log(res);

      if (res.status === 200) {
        setSidoList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchGugunList = async (sidoCd) => {
    try {
      console.log('2222 : ' + sidoCd);

      const res = await getGugunList(sidoCd);

      console.log(res);

      if (res.status === 200) {
        setGugunList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchDongList = async (gugunCd) => {
    try {
      console.log('1111 : ' + gugunCd);

      const res = await getDongList(gugunCd);

      console.log(res);

      if (res.status === 200) {
        setDongList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchSidoList();
  }, []);

  useEffect(() => {
    fetchGugunList(sidoCd);
  }, [sidoCd]);

  useEffect(() => {
    fetchDongList(gugunCd);
  }, [gugunCd]);

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">
        실시간 병상제공 병원 목록
      </h1>

      <div
        className="p-3 rounded shadow"
        style={{ backgroundColor: '#cce8f4' }}
      >
        <div className="d-flex gap-3 mb-3">
          <Form.Select
            value={sidoCd}
            onChange={(e) => setSidoCd(e.target.value)}
          >
            <option value={''}>시도 선택</option>
            {sidoList.map((sidoItem) => (
              <option value={sidoItem.bjdCd}>{sidoItem.bjdNm}</option>
            ))}
          </Form.Select>

          <Form.Select
            value={gugunCd}
            onChange={(e) => setGugunCd(e.target.value)}
          >
            <option value={''}>구군 선택</option>
            {gugunList.map((gugunItem) => (
              <option value={gugunItem.bjdCd}>{gugunItem.bjdNm}</option>
            ))}
          </Form.Select>

          <Form.Select
            value={dongCd}
            onChange={(e) => setDongCd(e.target.value)}
          >
            <option value={''}>동 선택</option>
            {dongList.map((dongItem) => (
              <option value={dongItem.bjdCd}>{dongItem.bjdNm}</option>
            ))}
          </Form.Select>
        </div>
        <Form.Control
          className="mb-3"
          type="text"
          placeholder="병원명을 입력하세요."
        />

        <Button>검색</Button>
      </div>
    </Container>
  );
};

export default EmgcRltmList;
