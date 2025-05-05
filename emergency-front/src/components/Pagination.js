import React, { useEffect, useState } from 'react';
import { Nav } from 'react-bootstrap';

const Pagination = ({ totalPages, size, page, setPage }) => {
  const [currPageArr, setCurrPageArr] = useState([]);
  const [totalPageArr, setTotalPageArr] = useState([]);

  const sliceArrBySize = (totalPages, size) => {
    const totalPageArr = Array.from({ length: totalPages }, (_, i) => i);
    const result = [];

    for (let i = 0; i < totalPages; i += size) {
      result.push(totalPageArr.slice(i, i + size));
    }

    return result;
  };

  useEffect(() => {
    const groupIndex = Math.floor(page / size);
    setCurrPageArr(totalPageArr[groupIndex] || []);
  }, [page]);

  useEffect(() => {
    const slicePageArr = sliceArrBySize(totalPages, size);
    setTotalPageArr(slicePageArr);
    setCurrPageArr(slicePageArr[0]);
  }, [totalPages]);

  return (
    <Nav aria-label="Page navigation example">
      <ul className="pagination">
        {page > 0 && (
          <li className="page-item">
            <button className="page-link" onClick={() => setPage(page - 1)}>
              Prev
            </button>
          </li>
        )}
        {currPageArr?.map((i) => (
          <li key={i} className={`page-item ${page === i ? 'active' : ''}`}>
            <button className="page-link" onClick={() => setPage(i)}>
              {i + 1}
            </button>
          </li>
        ))}
        {page < totalPages && (
          <li className="page-item">
            <button className="page-link" onClick={() => setPage(page + 1)}>
              Next
            </button>
          </li>
        )}
      </ul>
    </Nav>
  );
};

export default Pagination;
