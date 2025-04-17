import React, { useEffect, useState } from 'react';
import { Container, Form, Button, ListGroup } from 'react-bootstrap';
import { getSidoList, getGugunList, getDongList } from '../../apis/bjd';
import { getEmgcMstList } from '../../apis/emgc';
import { Link } from 'react-router-dom';

const EmgcRltmList = () => {
  const [sidoList, setSidoList] = useState([]);
  const [gugunList, setGugunList] = useState([]);
  const [dongList, setDongList] = useState([]);

  const [searchParams, setSearchParams] = useState({
    page: 0,
    size: 10,
    dutyNm: '',
    sidoCd: '',
    gugunCd: '',
    dongCd: '',
  });

  const [emgcMstList, setEmgcMstList] = useState([]);

  const fetchEgmcMstList = async () => {
    try {
      const res = await getEmgcMstList(searchParams);

      console.log(res);

      if (res.status === 200) {
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchSidoList = async () => {
    try {
      const res = await getSidoList();

      // console.log(res);

      if (res.status === 200) {
        setSidoList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchGugunList = async (sidoCd) => {
    try {
      const res = await getGugunList(sidoCd);

      // console.log(res);

      if (res.status === 200) {
        setGugunList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const fetchDongList = async (gugunCd) => {
    try {
      const res = await getDongList(gugunCd);

      // console.log(res);

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
    fetchGugunList(searchParams.sidoCd);
  }, [searchParams.sidoCd]);

  useEffect(() => {
    fetchDongList(searchParams.gugunCd);
  }, [searchParams.gugunCd]);

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
            value={searchParams.sidoCd}
            onChange={(e) =>
              setSearchParams({ ...searchParams, sidoCd: e.target.value })
            }
          >
            <option value={''}>시도 선택</option>
            {sidoList.map((sidoItem) => (
              <option value={sidoItem.bjdCd}>{sidoItem.bjdNm}</option>
            ))}
          </Form.Select>

          <Form.Select
            value={searchParams.gugunCd}
            onChange={(e) =>
              setSearchParams({ ...searchParams, gugunCd: e.target.value })
            }
          >
            <option value={''}>구군 선택</option>
            {gugunList.map((gugunItem) => (
              <option value={gugunItem.bjdCd}>{gugunItem.bjdNm}</option>
            ))}
          </Form.Select>

          <Form.Select
            value={searchParams.dongCd}
            onChange={(e) =>
              setSearchParams({ ...searchParams, dongCd: e.target.value })
            }
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
          value={searchParams.dutyNm}
          onChange={(e) =>
            setSearchParams({ ...searchParams, dutyNm: e.target.value })
          }
        />
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            width: '100%',
          }}
        >
          <Form.Select
            value={searchParams.size}
            onChange={(e) =>
              setSearchParams({ ...searchParams, size: e.target.value })
            }
            style={{ flex: '1', maxWidth: '20%' }}
          >
            <option value={10}>10건 씩 조회</option>
            <option value={20}>20건 씩 조회</option>
            <option value={30}>30건 씩 조회</option>
            <option value={40}>40건 씩 조회</option>
            <option value={50}>50건 씩 조회</option>
          </Form.Select>
          <Button
            style={{ flex: '1', maxWidth: '5%' }}
            onClick={() => fetchEgmcMstList(searchParams)}
          >
            검색
          </Button>
        </div>
      </div>

      <hr />

      <div>
        <ListGroup>
          <ListGroup.Item>
            <div className="p-2 d-flex justify-content-between align-items-center">
              {/* 병원이름 */}
              <h6 className="m-0">
                <Link
                  to="/hospital-detail"
                  className="text-decoration-none text-dark"
                >
                  병원이름
                  {/* 상세보기 버튼 */}
                  <Button variant="primary" size="sm" className="ms-2">
                    상세보기
                  </Button>
                </Link>
                <p>032-1234-1234</p>
                <p>
                  <em className="me-2 border border-dark px-2 py-1">도로명</em>
                  서울특별시 어쩌구저쩌구
                </p>
                <p className="text-danger">
                  정보 여기 if문걸어서 없으면 안나오도록
                </p>
                <div>
                  <p></p>
                </div>
              </h6>
            </div>
          </ListGroup.Item>
        </ListGroup>
      </div>
    </Container>
  );
};

export default EmgcRltmList;
