import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import UserLogin from './pages/user/UserLogin';
import UserJoin from './pages/user/UserJoin';
import { Container } from 'react-bootstrap';
import LoginContextProvider from './context/LoginContextProvider';

const App = () => {
  return (
    <Container fluid>
      <LoginContextProvider>
        <Header />
        <Routes>
          <Route path="/userLogin" exact={true} element={<UserLogin />} />
          <Route path="/userJoin" exact={true} element={<UserJoin />} />
        </Routes>
        <Footer />
      </LoginContextProvider>
    </Container>
  );
};

export default App;
