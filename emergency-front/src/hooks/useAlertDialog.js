import { useEffect, useCallback, useState } from 'react';

// Dialog Hook (중복 코드 제거)
export function useAlertDialog() {
  const [dialogState, setDialogState] = useState({
    open: false,
    message: '', // 모달 메시지 상태
    type: '', // 모달 타입 상태 ('success' or 'error')
    errorCd: '',
  });

  const showDialog = useCallback((message, type, errorCd = '') => {
    setDialogState({
      open: true,
      message,
      type,
      errorCd,
    });
  }, []);

  const closeDialog = useCallback(() => {
    setDialogState((prev) => ({
      ...prev,
      open: false,
    }));
  }, []);

  return { dialogState, showDialog, closeDialog };
}
