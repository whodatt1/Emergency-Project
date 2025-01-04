import React from 'react';
import { Link } from 'react-router-dom';
import { Col, Container, Nav, Row } from 'react-bootstrap';

const Footer = () => {
  return (
    <Container fluid className="py-3 my-4 border-top">
      <Row className="align-items-center">
        {/* 왼쪽 콘텐츠 */}
        <Col md={4} className="d-flex align-items-center">
          <Link
            to="/"
            className="me-2 text-body-secondary text-decoration-none lh-1"
          >
            <svg className="bi" width="30" height="24">
              <use href="#bootstrap" />
            </svg>
          </Link>
          <span className="text-body-secondary">&copy; 2024 Company, Inc</span>
        </Col>

        {/* 오른쪽 소셜 아이콘 */}
        <Col md={4} className="d-flex justify-content-end">
          <Nav as="ul" className="list-unstyled d-flex">
            <Nav.Item as="li" className="ms-3">
              <Link to="#" className="text-body-secondary">
                <svg className="bi" width="24" height="24">
                  <use href="#twitter" />
                </svg>
              </Link>
            </Nav.Item>
            <Nav.Item as="li" className="ms-3">
              <Link to="#" className="text-body-secondary">
                <svg className="bi" width="24" height="24">
                  <use href="#instagram" />
                </svg>
              </Link>
            </Nav.Item>
            <Nav.Item as="li" className="ms-3">
              <Link to="#" className="text-body-secondary">
                <svg className="bi" width="24" height="24">
                  <use href="#facebook" />
                </svg>
              </Link>
            </Nav.Item>
          </Nav>
        </Col>
      </Row>
    </Container>
  );
};

export default Footer;
