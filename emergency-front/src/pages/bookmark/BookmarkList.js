import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Container, Form, ListGroup } from 'react-bootstrap';
import { useSearchParams } from 'react-router-dom';
import { getBookmarkMstList } from '../../apis/bookmark';

const BookmarkList = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [bookmarkList, setBookmarkList] = useState([]);
  const [bmTotalCnt, setBmTotalCnt] = useState(0);
  const [mstSearchParams, setMstSearchParams] = useState({
    dutyNm: '',
  });

  const fetchBookmarkList = async () => {
    try {
      const query = Object.fromEntries(searchParams.entries());

      const offset = Number(searchParams.get('offset') || 0);
      const gubun = searchParams.get('gubun') || '';

      if ((!Number.isNaN(offset) && offset === 0) || gubun) {
        setBookmarkList([]);

        if (gubun) {
          // 디테일에서 돌아왔을 경우 일시적 세팅
          query.offset = 0;
          query.size = offset + Number(query.size || 10);

          // 구분 쿼리스트링 삭제
          const newParams = new URLSearchParams(searchParams.toString());
          newParams.delete('gubun');
        }
      }

      const res = await getBookmarkMstList(query);

      console.log(res);

      if (res.status === 200) {
        setBookmarkList((prev) => [...prev, ...res.data.content]);
        setBmTotalCnt(res.data.totalCnt);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleMoreClick = () => {
    const newParams = new URLSearchParams(searchParams.toString());
    const currentOffset = Number(searchParams.get('offset') || 0);
    newParams.set('offset', currentOffset + 10);
    setSearchParams(newParams);
  };

  const handleSearchClick = () => {
    const newParams = new URLSearchParams(searchParams.toString());
    // 병원명 입력받기
    newParams.set('dutyNm', mstSearchParams.dutyNm);
    setSearchParams(newParams);
  };

  // 쿼리스트링 기반 조회조건 세팅
  const initParams = async () => {
    const query = Object.fromEntries(searchParams.entries());

    const { dutyNm } = query;

    // 조회 조건 세팅
    setMstSearchParams({ dutyNm });
  };

  useEffect(() => {
    const gubun = searchParams.get('gubun') || '';

    if (gubun) {
      initParams();
    }
    fetchBookmarkList();
  }, [searchParams.toString()]);

  return (
    // 더보기 페이지네이션 사용 예정 page를 기억해두고 limit을 늘리는 식으로 하여
    // 디테일 페이지에서 돌아왔을때도 해당 페이지 유지되게끔 만들 예정
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">즐겨찾기 병원 목록</h1>

      <div
        className="p-3 rounded shadow mb-3"
        style={{ backgroundColor: '#cce8f4' }}
      >
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
          <Button onClick={() => handleSearchClick()}>검색</Button>
        </div>
      </div>

      <div className="mb-3">
        <ListGroup>
          {bookmarkList.map((item) => (
            <ListGroup.Item key={item.hpId}>
              <div className="p-2 d-flex justify-content-between align-items-center">
                {/* 병원이름 */}
                <h6 className="m-0">
                  <Link
                    to={{
                      pathname: '/emgcRltmDtl',
                      search: `${searchParams.toString()}&hpId=${
                        item.hpId
                      }&gubun=2`,
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

      {bookmarkList.length < bmTotalCnt && (
        <div className="text-center mb-5">
          <Button onClick={() => handleMoreClick()}>더보기</Button>
        </div>
      )}
    </Container>
  );
};

export default BookmarkList;
