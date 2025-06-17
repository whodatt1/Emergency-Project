import React, { useEffect, useState } from 'react';
import { Button, Container, Form, ListGroup } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { getDongList, getGugunList, getSidoList } from '../../apis/bjd';
import { getEmgcMstList } from '../../apis/emgc';
import Pagination from '../../components/Pagination';

const EmgcRltmList = () => {
  const [sidoList, setSidoList] = useState([]);
  const [gugunList, setGugunList] = useState([]);
  const [dongList, setDongList] = useState([]);
  const [page, setPage] = useState(0);

  const [bjdSearchParams, setBjdSearchParams] = useState({
    sidoCd: null,
    gugunCd: null,
    dongCd: null,
  });

  const [mstSearchParams, setMstSearchParams] = useState({
    size: 10,
    dutyNm: '',
    sidoNm: '',
    gugunNm: '',
    dongNm: '',
  });

  const [emgcMstList, setEmgcMstList] = useState([]);
  const [pageable, setPageable] = useState({
    first: '',
    last: '',
    size: 0,
    pageNumber: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const fetchEgmcMstList = async () => {
    try {
      setEmgcMstList([]); // 요청 전 초기화 과정

      const res = await getEmgcMstList({ ...mstSearchParams, page: page });

      console.log(res);

      if (res.status === 200) {
        setEmgcMstList(res.data.content);
        setPageable({
          size: res.data.size,
          pageNumber: res.data.pageable.pageNumber,
          totalPages: res.data.totalPages,
        });
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
    setBjdSearchParams({
      sidoCd: null,
      gugunCd: null,
      dongCd: null,
    });
    fetchSidoList();
  }, []);

  useEffect(() => {
    if (bjdSearchParams.sidoCd === '') {
      setGugunList([]);
      setDongList([]);
    } else {
      fetchGugunList(bjdSearchParams.sidoCd);
    }
  }, [bjdSearchParams.sidoCd]);

  useEffect(() => {
    if (bjdSearchParams.gugunCd === '') {
      setDongList([]);
    } else {
      fetchDongList(bjdSearchParams.gugunCd);
    }
  }, [bjdSearchParams.gugunCd]);

  useEffect(() => {
    setMstSearchParams({ ...mstSearchParams, page: page });
    fetchEgmcMstList(mstSearchParams);
  }, [page]);

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
            onChange={(e) => {
              const selected = sidoList.find(
                (item) => item.bjdCd === e.target.value,
              );
              setMstSearchParams({
                ...mstSearchParams,
                sidoNm: selected?.bjdNm,
              });
              setBjdSearchParams({
                ...bjdSearchParams,
                sidoCd: e.target.value,
              });
            }}
          >
            <option value={''}>시도 선택</option>
            {sidoList.map((sidoItem) => (
              <option key={sidoItem.bjdCd} value={sidoItem.bjdCd}>
                {sidoItem.bjdNm}
              </option>
            ))}
          </Form.Select>

          <Form.Select
            onChange={(e) => {
              const selected = gugunList.find(
                (item) => item.bjdCd === e.target.value,
              );
              setMstSearchParams({
                ...mstSearchParams,
                gugunNm: selected?.bjdNm,
              });
              setBjdSearchParams({
                ...bjdSearchParams,
                gugunCd: e.target.value,
              });
            }}
          >
            <option value={''}>구군 선택</option>
            {gugunList.map((gugunItem) => (
              <option key={gugunItem.bjdCd} value={gugunItem.bjdCd}>
                {gugunItem.bjdNm}
              </option>
            ))}
          </Form.Select>

          <Form.Select
            onChange={(e) => {
              const selected = dongList.find(
                (item) => item.bjdCd === e.target.value,
              );
              setMstSearchParams({
                ...mstSearchParams,
                dongNm: selected?.bjdNm,
              });
              setBjdSearchParams({
                ...bjdSearchParams,
                dongCd: e.target.value,
              });
            }}
          >
            <option value={''}>동 선택</option>
            {dongList.map((dongItem) => (
              <option key={dongItem.bjdCd} value={dongItem.bjdCd}>
                {dongItem.bjdNm}
              </option>
            ))}
          </Form.Select>
        </div>
        <Form.Control
          className="mb-3"
          type="text"
          placeholder="병원명을 입력하세요."
          value={mstSearchParams.dutyNm}
          onChange={(e) =>
            setMstSearchParams({ ...mstSearchParams, dutyNm: e.target.value })
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
            value={mstSearchParams.size}
            onChange={(e) =>
              setMstSearchParams({ ...mstSearchParams, size: e.target.value })
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
            onClick={() => fetchEgmcMstList({ ...mstSearchParams, page: page })}
          >
            검색
          </Button>
        </div>
      </div>

      <hr />

      <div className="mb-3">
        <ListGroup>
          {emgcMstList.map((item) => (
            <ListGroup.Item key={item.hpId}>
              <div className="p-2 d-flex justify-content-between align-items-center">
                {/* 병원이름 */}
                <h6 className="m-0">
                  <Link
                    to="/emgcRltmDtl"
                    state={{ page, mstSearchParams, hpId: item.hpId }}
                    className="text-decoration-none text-dark"
                  >
                    {item.dutyName}
                    {/* 상세보기 버튼 */}
                    <Button variant="primary" size="sm" className="ms-2">
                      상세보기
                    </Button>
                  </Link>
                  <p>{item.dutyTel}</p>
                  <p>
                    <em className="me-2 border border-dark px-2 py-1">
                      도로명
                    </em>
                    {item.dutyAddr}
                  </p>
                  {item.dutyInf && (
                    <p className="text-danger">{item.dutyInf}</p>
                  )}
                  <div>
                    <p></p>
                  </div>
                </h6>
              </div>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </div>

      <Pagination
        totalPages={pageable.totalPages}
        size={pageable.size}
        page={pageable.pageNumber}
        setPage={setPage}
      />
    </Container>
  );
};

export default EmgcRltmList;
