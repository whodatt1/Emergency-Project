import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import UserLogin from './pages/user/UserLogin';

const App = () => {
  return (
    <div className="container">
      <Header />
      <Routes>
        <Route path="/userJoin" exact={true} element={<UserLogin />} />
      </Routes>
      <Footer />
    </div>
  );
};

export default App;
