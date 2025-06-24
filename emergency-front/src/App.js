import React, { useEffect } from 'react';
import { getMessaging, getToken } from 'firebase/messaging';
import { Route, Routes } from 'react-router-dom';
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
          const token = await getToken(messaging, {
            vapidKey: process.env.REACT_APP_FIREBASE_VAPID_KEY,
          });
          if (token) {
            localStorage.setItem('fcm_token', token);
            console.log('새로 생성된 Token : ', token);
          }
        } catch (error) {
          console.error('FCM 토큰 발급 에러:', error);
        }
      } else {
        console.log('스토리지 토큰: ', storedToken);
      }
    };

    initFcmToken();
  }, [messaging]);

  return (
    <Container fluid>
      <LoginContextProvider>
        <Header />
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
