import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import UserLogin from './pages/user/UserLogin';
import UserJoin from './pages/user/UserJoin';
import { Container } from 'react-bootstrap';
import LoginContextProvider from './context/LoginContextProvider';
import EmgcRltmList from './pages/emgc/EmgcRltmList';
import EmgcRltmDtl from './pages/emgc/EmgcRltmDtl';

const App = () => {
  return (
    <Container fluid>
      <LoginContextProvider>
        <Header />
        <Routes>
          <Route path="/userLogin" exact={true} element={<UserLogin />} />
          <Route path="/userJoin" exact={true} element={<UserJoin />} />
          <Route path="/emgcRltmList" exact={true} element={<EmgcRltmList />} />
          <Route path="/emgcRltmDtl" exact={true} element={<EmgcRltmDtl />} />
        </Routes>
        <Footer />
      </LoginContextProvider>
    </Container>
  );
};

export default App;
