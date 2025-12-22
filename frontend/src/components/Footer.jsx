import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-xl font-bold mb-4">Rentit</h3>
            <p className="text-gray-400">
              Find your perfect home with ease. Browse thousands of rental properties.
            </p>
          </div>

          <div>
            <h4 className="text-lg font-semibold mb-4">Quick Links</h4>
            <ul className="space-y-2">
              <li>
                <Link to="/" className="text-gray-400 hover:text-white">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/search" className="text-gray-400 hover:text-white">
                  Browse Properties
                </Link>
              </li>
              <li>
                <Link to="/login" className="text-gray-400 hover:text-white">
                  Login
                </Link>
              </li>
              <li>
                <Link to="/register" className="text-gray-400 hover:text-white">
                  Sign Up
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h4 className="text-lg font-semibold mb-4">For Owners</h4>
            <ul className="space-y-2">
              <li>
                <Link to="/owner/dashboard" className="text-gray-400 hover:text-white">
                  List Your Property
                </Link>
              </li>
              <li>
                <Link to="/owner/properties" className="text-gray-400 hover:text-white">
                  Manage Properties
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h4 className="text-lg font-semibold mb-4">Contact</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Email: support@rentit.com</li>
              <li>Phone: +1 (555) 123-4567</li>
              <li>Address: 123 Main St, City, State</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
          <p>&copy; {new Date().getFullYear()} Rentit. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
