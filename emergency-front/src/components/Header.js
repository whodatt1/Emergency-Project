import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Form, FormControl, Nav, Navbar } from 'react-bootstrap';

const Header = () => {
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
              <Nav.Link as={Link} to="/" className="px-2">
                Home
              </Nav.Link>
              <Nav.Link as={Link} to="/features" className="px-2">
                Features
              </Nav.Link>
              <Nav.Link as={Link} to="/pricing" className="px-2">
                Pricing
              </Nav.Link>
              <Nav.Link as={Link} to="/faqs" className="px-2">
                FAQs
              </Nav.Link>
              <Nav.Link as={Link} to="/about" className="px-2">
                About
              </Nav.Link>
            </Nav>
            <Nav>
              <Nav.Link as={Link} to="/userLogin" className="px-2">
                Login
              </Nav.Link>
              <Nav.Link as={Link} to="/userJoin" className="px-2">
                Sign up
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* Search Section */}
      <header className="py-3 border-bottom">
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
      </header>
    </div>
  );
};

export default Header;
