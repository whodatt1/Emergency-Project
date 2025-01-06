import React from 'react';
import DaumPostCode from 'react-daum-postcode';
import { Modal, Button } from 'react-bootstrap';

const DaumPC = ({ onComplete, onClose, show }) => {
  const handleComplete = (data) => {
    const { address, zonecode } = data;
    onComplete({ address, zonecode }); // 부모로 전달
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>주소 검색</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <DaumPostCode
          onComplete={handleComplete}
          style={{ width: '100%', height: '450px' }}
        />
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DaumPC;
