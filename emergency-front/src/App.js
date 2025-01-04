import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import UserLogin from './pages/user/UserLogin';
import { Container } from 'react-bootstrap';

const App = () => {
  return (
    <Container fluid>
      <Header />
      <Routes>
        <Route path="/userLogin" exact={true} element={<UserLogin />} />
      </Routes>
      <Footer />
    </Container>
  );
};

export default App;
