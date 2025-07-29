import React, { useEffect, useState } from 'react';
import { Button, Container, Form, ListGroup } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { getDongList, getGugunList, getSidoList } from '../../apis/bjd';
import { getEmgcMstList } from '../../apis/emgc';
import Pagination from '../../components/Pagination';
import { useSearchParams } from 'react-router-dom';

const EmgcRltmList = () => {
  const [sidoList, setSidoList] = useState([]);
  const [gugunList, setGugunList] = useState([]);
  const [dongList, setDongList] = useState([]);
  const [page, setPage] = useState(0);
  const [searchParams, setSearchParams] = useSearchParams();

  const [bjdSearchParams, setBjdSearchParams] = useState({
    sidoCd: null,
    gugunCd: null,
    dongCd: null,
  });

  const [mstSearchParams, setMstSearchParams] = useState({
    size: 10,
    dutyNm: '',
  });

  const [emgcMstList, setEmgcMstList] = useState([]);
  const [pageable, setPageable] = useState({
    first: '',
    last: '',
    size: 10,
    pageNumber: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const fetchEgmcMstList = async () => {
    try {
      setEmgcMstList([]); // 요청 전 초기화 과정

      const query = Object.fromEntries(searchParams.entries());

      const res = await getEmgcMstList(query);

      console.log(res);

      if (res.status === 200) {
        setEmgcMstList(res.data.content);
        setPageable({
          ...pageable,
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

  // 쿼리스트링 세팅
  const handleSearchClick = () => {
    const newParams = new URLSearchParams();

    if (mstSearchParams.dutyNm) newParams.set('dutyNm', mstSearchParams.dutyNm);
    if (mstSearchParams.size) newParams.set('size', mstSearchParams.size);

    if (bjdSearchParams.sidoCd) {
      const sidoNm = sidoList.find(
        (s) => s.bjdCd === bjdSearchParams.sidoCd,
      )?.bjdNm;
      if (sidoNm) newParams.set('sidoNm', sidoNm);
    }
    if (bjdSearchParams.gugunCd) {
      const gugunNm = gugunList.find(
        (s) => s.bjdCd === bjdSearchParams.gugunCd,
      )?.bjdNm;
      if (gugunNm) newParams.set('gugunNm', gugunNm);
    }
    if (bjdSearchParams.dongCd) {
      const dongNm = dongList.find(
        (s) => s.bjdCd === bjdSearchParams.dongCd,
      )?.bjdNm;
      if (dongNm) newParams.set('dongNm', dongNm);
    }

    newParams.set('page', 0);
    setPage(0);
    setSearchParams(newParams);
    // fetchEgmcMstList();
  };

  const handlePageChange = (newPage) => {
    setPage(newPage);

    searchParams.set('page', newPage);
    setSearchParams(searchParams);
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
    fetchEgmcMstList();
  }, [page, searchParams]);

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
            value={searchParams.get('size') || 10}
            style={{ flex: '1', maxWidth: '20%' }}
            onChange={(e) =>
              setMstSearchParams({ ...mstSearchParams, size: e.target.value })
            }
          >
            <option value={10}>10건 씩 조회</option>
            <option value={20}>20건 씩 조회</option>
            <option value={30}>30건 씩 조회</option>
            <option value={40}>40건 씩 조회</option>
            <option value={50}>50건 씩 조회</option>
          </Form.Select>
          <Button
            style={{ flex: '1', maxWidth: '5%' }}
            onClick={() => handleSearchClick()}
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
                    to={{
                      pathname: '/emgcRltmDtl',
                      search: `${searchParams.toString()}&hpId=${item.hpId}`,
                    }}
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
        handlePageChange={handlePageChange}
      />
    </Container>
  );
};

export default EmgcRltmList;
