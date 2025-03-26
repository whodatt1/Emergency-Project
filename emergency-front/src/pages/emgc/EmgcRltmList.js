import React, { useState } from 'react';
import { Container, Form } from 'react-bootstrap';

const EmgcRltmList = () => {
  const [sido, setSido] = useState('');
  const [gugun, setGugun] = useState('');
  const [dong, setDong] = useState('');

  return (
    <Container fluid className="mt-5">
      <h1 className="h3 mb-3 fw-normal text-center">응급기관 목록</h1>

      <div
        className="p-3 rounded shadow"
        style={{ backgroundColor: '#cce8f4' }}
      >
        <div className="d-flex gap-3">
          <Form.Select value={sido} onChange={(e) => setSido(e.target.value)}>
            <option value={''}>시도 선택</option>
          </Form.Select>

          <Form.Select value={gugun} onChange={(e) => setGugun(e.target.value)}>
            <option value={''}>구군 선택</option>
          </Form.Select>

          <Form.Select value={dong} onChange={(e) => setDong(e.target.value)}>
            <option value={''}>동 선택</option>
          </Form.Select>
        </div>
      </div>
    </Container>
  );
};

export default EmgcRltmList;
