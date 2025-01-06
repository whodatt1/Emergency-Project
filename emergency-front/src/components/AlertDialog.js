import React from 'react';
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Button,
} from '@mui/material';
import { FaCheckCircle, FaExclamationCircle } from 'react-icons/fa'; // react-icons에서 가져옴

const AlertDialog = ({ open, onClose, message, onConfirm, type }) => {
  // 아이콘 선택
  let IconComponent;
  let iconColor;
  if (type === 'success') {
    IconComponent = FaCheckCircle; // 성공 아이콘
    iconColor = 'green';
  } else if (type === 'error') {
    IconComponent = FaExclamationCircle; // 실패 아이콘
    iconColor = 'red';
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>알림</DialogTitle>
      <DialogContent>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <IconComponent
            style={{ color: iconColor, marginRight: 10, fontSize: 30 }}
          />
          <p>{message}</p>
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onConfirm} color="primary">
          확인
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AlertDialog;
