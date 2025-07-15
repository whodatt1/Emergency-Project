import React, { useContext, useEffect } from 'react';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import UserLogin from './pages/user/UserLogin';
import UserJoin from './pages/user/UserJoin';
import { Container } from 'react-bootstrap';
import LoginContextProvider from './context/LoginContextProvider';
import EmgcRltmList from './pages/emgc/EmgcRltmList';
import EmgcRltmDtl from './pages/emgc/EmgcRltmDtl';
import BookmarkList from './pages/bookmark/BookmarkList';
import { initializeApp } from 'firebase/app';

const App = () => {
  const navigate = useNavigate();

  const firebaseConfig = {
    apiKey: process.env.REACT_APP_FIREBASE_API_KEY,
    authDomain: process.env.REACT_APP_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_FIREBASE_PROJECT_ID,
    storageBucket: process.env.REACT_APP_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.REACT_APP_FIREBASE_APP_ID,
    measurementId: process.env.REACT_APP_FIREBASE_MEASUREMENT_ID,
  };

  const app = initializeApp(firebaseConfig);

  const messaging = getMessaging(app);

  useEffect(() => {
    const initFcmToken = async () => {
      const storedToken = localStorage.getItem('fcm_token');

      if (!storedToken) {
        try {
          const newToken = await getToken(messaging, {
            vapidKey: process.env.REACT_APP_FIREBASE_VAPID_KEY,
          });

          if (!storedToken || storedToken !== newToken) {
            localStorage.setItem('fcm_token', newToken);
          } else {
            console.log('기존 FCM 토큰 동일. 변경 없음');
          }
        } catch (error) {
          console.error('FCM 토큰 발급 에러:', error);
        }
      }
    };

    initFcmToken();

    onMessage(messaging, (payload) => {
      console.log('[포그라운드 메시지 수신]', payload);

      const title = payload?.data?.title || '알림';
      const body = payload?.data?.body || '';
      const hpId = payload?.data?.payload || '';

      if (Notification.permission === 'granted') {
        const notification = new Notification(title, {
          body: body,
          icon: '/icon.png',
          requireInteraction: false, // 몇 초 후 사라짐
        });

        notification.onclick = () => {
          if (hpId) {
            navigate('/emgcRltmDtl', {
              state: { hpId: hpId },
            });
            window.focus();
          }
        };
      }
    });
  }, [messaging, navigate]);

  // 테스트용 알림 띄우기 함수 (윈도우 방해금지 모드를 사용하지 않아야 뜸)
  const testNotification = () => {
    if (Notification.permission === 'granted') {
      console.log('여기');
      new Notification('테스트 알림', {
        body: '이 알림이 뜨면 알림 설정이 정상입니다!',
        icon: '/icon.png',
      });
    } else {
      Notification.requestPermission().then((permission) => {
        if (permission === 'granted') {
          new Notification('테스트 알림', {
            body: '이 알림이 뜨면 알림 설정이 정상입니다!',
            icon: '/icon.png',
          });
        } else {
          alert('알림 권한이 거부되었습니다.');
        }
      });
    }
  };

  return (
    <Container fluid>
      <LoginContextProvider>
        <Header />

        {/* 테스트용 버튼 추가 */}
        <div style={{ padding: 16, textAlign: 'center' }}>
          <button onClick={testNotification} variant="primary">
            테스트 알림 보내기
          </button>
        </div>
        <Routes>
          <Route path="/userLogin" exact={true} element={<UserLogin />} />
          <Route path="/userJoin" exact={true} element={<UserJoin />} />
          <Route path="/emgcRltmList" exact={true} element={<EmgcRltmList />} />
          <Route path="/emgcRltmDtl" exact={true} element={<EmgcRltmDtl />} />
          <Route path="/bookmarkList" exact={true} element={<BookmarkList />} />
        </Routes>
        <Footer />
      </LoginContextProvider>
    </Container>
  );
};

export default App;
