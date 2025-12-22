import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import SearchPage from './pages/SearchPage';
import PropertyDetailPage from './pages/PropertyDetailPage';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="flex flex-col min-h-screen">
          <Navbar />
          <main className="flex-grow">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/search" element={<SearchPage />} />
              <Route path="/property/:id" element={<PropertyDetailPage />} />
              
              {/* Protected Routes - Add more as needed */}
              <Route
                path="/tenant/dashboard"
                element={
                  <ProtectedRoute roles={['tenant']}>
                    <div className="min-h-screen flex items-center justify-center">
                      <h1 className="text-3xl font-bold">Tenant Dashboard (Coming Soon)</h1>
                    </div>
                  </ProtectedRoute>
                }
              />
              
              <Route
                path="/owner/dashboard"
                element={
                  <ProtectedRoute roles={['owner']}>
                    <div className="min-h-screen flex items-center justify-center">
                      <h1 className="text-3xl font-bold">Owner Dashboard (Coming Soon)</h1>
                    </div>
                  </ProtectedRoute>
                }
              />
              
              <Route
                path="/admin/dashboard"
                element={
                  <ProtectedRoute roles={['admin']}>
                    <div className="min-h-screen flex items-center justify-center">
                      <h1 className="text-3xl font-bold">Admin Dashboard (Coming Soon)</h1>
                    </div>
                  </ProtectedRoute>
                }
              />

              {/* 404 Page */}
              <Route
                path="*"
                element={
                  <div className="min-h-screen flex items-center justify-center">
                    <div className="text-center">
                      <h1 className="text-6xl font-bold text-gray-900 mb-4">404</h1>
                      <p className="text-xl text-gray-600 mb-8">Page not found</p>
                      <a
                        href="/"
                        className="bg-primary-600 text-white px-6 py-3 rounded-md hover:bg-primary-700"
                      >
                        Go Home
                      </a>
                    </div>
                  </div>
                }
              />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
