import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <div>
      <nav className="py-2 bg-body-tertiary border-bottom">
        <div className="container d-flex flex-wrap">
          <ul className="nav me-auto">
            <li className="nav-item">
              <Link
                href="#"
                className="nav-link link-body-emphasis px-2 active"
                aria-current="page"
              >
                Home
              </Link>
            </li>
            <li className="nav-item">
              <Link href="#" className="nav-link link-body-emphasis px-2">
                Features
              </Link>
            </li>
            <li className="nav-item">
              <Link href="#" className="nav-link link-body-emphasis px-2">
                Pricing
              </Link>
            </li>
            <li className="nav-item">
              <Link href="#" className="nav-link link-body-emphasis px-2">
                FAQs
              </Link>
            </li>
            <li className="nav-item">
              <Link href="#" className="nav-link link-body-emphasis px-2">
                About
              </Link>
            </li>
          </ul>
          <ul className="nav">
            <li className="nav-item">
              <Link
                to={'/userJoin'}
                className="nav-link link-body-emphasis px-2"
              >
                Login
              </Link>
            </li>
            <li class="nav-item">
              <Link href="#" className="nav-link link-body-emphasis px-2">
                Sign up
              </Link>
            </li>
          </ul>
        </div>
      </nav>
      <header className="py-3 mb-4 border-bottom">
        <div className="container d-flex flex-wrap justify-content-center">
          <Link
            href="/"
            className="d-flex align-items-center mb-3 mb-lg-0 me-lg-auto link-body-emphasis text-decoration-none"
          >
            <svg className="bi me-2" width="40" height="32">
              <use href="#bootstrap" />
            </svg>
            <span className="fs-4">EMERGENCY</span>
          </Link>
          <form className="col-12 col-lg-auto mb-3 mb-lg-0" role="search">
            <input
              type="search"
              className="form-control"
              placeholder="Search..."
              aria-label="Search"
            />
          </form>
        </div>
      </header>
    </div>
  );
};

export default Header;
