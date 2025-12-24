import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="text-2xl font-bold text-primary-600">
              Rentit
            </Link>
            <div className="hidden md:ml-10 md:flex md:space-x-8">
              <Link to="/search" className="text-gray-700 hover:text-primary-600 px-3 py-2">
                Browse Properties
              </Link>
              {user?.role === 'owner' && (
                <Link to="/owner/dashboard" className="text-gray-700 hover:text-primary-600 px-3 py-2">
                  My Properties
                </Link>
              )}
              {user?.role === 'admin' && (
                <Link to="/admin/dashboard" className="text-gray-700 hover:text-primary-600 px-3 py-2">
                  Admin Dashboard
                </Link>
              )}
            </div>
          </div>

          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <Link
                  to={
                    user?.role === 'owner'
                      ? '/owner/dashboard'
                      : user?.role === 'admin'
                      ? '/admin/dashboard'
                      : '/tenant/dashboard'
                  }
                  className="text-gray-700 hover:text-primary-600"
                >
                  Dashboard
                </Link>
                <button
                  onClick={handleLogout}
                  className="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-gray-700 hover:text-primary-600">
                  Login
                </Link>
                <Link
                  to="/register"
                  className="bg-primary-600 text-white px-4 py-2 rounded-md hover:bg-primary-700"
                >
                  Sign Up
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
