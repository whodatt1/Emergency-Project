import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { Container, Form, FormControl, Nav, Navbar } from 'react-bootstrap';
import { LoginContext } from '../context/LoginContextProvider';

const Header = () => {
  const { isLoggedIn, userInfo, logoutUser } = useContext(LoginContext);

  const handleLogout = async () => {
    await logoutUser();
  };

  return (
    <div>
      {/* Top Navigation */}
      <Navbar bg="light" expand="lg" className="border-bottom">
        <Container fluid>
          <Navbar.Brand href="/" className="fs-4">
            EMERGENCY
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to="/emgcRltmList" className="px-2">
                응급기관 정보
              </Nav.Link>
            </Nav>
            {isLoggedIn ? (
              <Nav>
                <Nav.Link as={Link} to="/bookmarkList" className="px-2">
                  즐겨찾기
                </Nav.Link>
                <Nav.Link as={Link} to="/" className="px-2">
                  {userInfo.userId}
                </Nav.Link>
                <Nav.Link onClick={handleLogout} className="px-2">
                  Logout
                </Nav.Link>
              </Nav>
            ) : (
              <Nav>
                <Nav.Link as={Link} to="/userLogin" className="px-2">
                  Login
                </Nav.Link>
                <Nav.Link as={Link} to="/userJoin" className="px-2">
                  Sign up
                </Nav.Link>
              </Nav>
            )}
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* Search Section */}
      {/* <header className="py-3 border-bottom">
        <Container
          fluid
          className="d-flex justify-content-between align-items-center"
        >
          <Form className="d-flex" role="search">
            <FormControl
              type="search"
              placeholder="Search..."
              aria-label="Search"
              className="me-2"
            />
          </Form>
        </Container>
      </header> */}
    </div>
  );
};

export default Header;
