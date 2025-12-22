import api from './api';

export const propertyService = {
  // Get all properties
  getProperties: (params = {}) => {
    return api.get('/properties', { params });
  },

  // Get property by ID
  getPropertyById: (id) => {
    return api.get(`/properties/${id}`);
  },

  // Create property
  createProperty: (propertyData) => {
    return api.post('/properties', propertyData);
  },

  // Update property
  updateProperty: (id, propertyData) => {
    return api.put(`/properties/${id}`, propertyData);
  },

  // Delete property
  deleteProperty: (id) => {
    return api.delete(`/properties/${id}`);
  },

  // Get owner's properties
  getOwnerProperties: (ownerId, params = {}) => {
    return api.get(`/properties/owner/${ownerId}`, { params });
  },

  // Update property status
  updatePropertyStatus: (id, status) => {
    return api.patch(`/properties/${id}/status`, { status });
  },

  // Search properties
  searchProperties: (params) => {
    return api.get('/search', { params });
  },

  // Search nearby properties
  searchNearby: (params) => {
    return api.get('/search/nearby', { params });
  },
};

export const bookingService = {
  // Create booking
  createBooking: (bookingData) => {
    return api.post('/bookings', bookingData);
  },

  // Get booking by ID
  getBookingById: (id) => {
    return api.get(`/bookings/${id}`);
  },

  // Approve booking
  approveBooking: (id) => {
    return api.patch(`/bookings/${id}/approve`);
  },

  // Reject booking
  rejectBooking: (id) => {
    return api.patch(`/bookings/${id}/reject`);
  },

  // Cancel booking
  cancelBooking: (id) => {
    return api.patch(`/bookings/${id}/cancel`);
  },

  // Get tenant bookings
  getTenantBookings: (tenantId, params = {}) => {
    return api.get(`/bookings/tenant/${tenantId}`, { params });
  },

  // Get owner bookings
  getOwnerBookings: (ownerId, params = {}) => {
    return api.get(`/bookings/owner/${ownerId}`, { params });
  },
};

export const reviewService = {
  // Submit review
  submitReview: (reviewData) => {
    return api.post('/reviews', reviewData);
  },

  // Get property reviews
  getPropertyReviews: (propertyId, params = {}) => {
    return api.get(`/reviews/property/${propertyId}`, { params });
  },

  // Update review
  updateReview: (id, reviewData) => {
    return api.put(`/reviews/${id}`, reviewData);
  },

  // Delete review
  deleteReview: (id) => {
    return api.delete(`/reviews/${id}`);
  },
};

export const imageService = {
  // Upload images
  uploadImages: (formData) => {
    return api.post('/images/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },

  // Get image URL
  getImageUrl: (filename) => {
    const baseUrl = import.meta.env.VITE_IMAGE_BASE_URL || 'http://localhost:5000';
    return `${baseUrl}/uploads/${filename}`;
  },

  // Delete image
  deleteImage: (id) => {
    return api.delete(`/images/${id}`);
  },
};

export const adminService = {
  // Get all users
  getUsers: (params = {}) => {
    return api.get('/admin/users', { params });
  },

  // Delete user
  deleteUser: (id) => {
    return api.delete(`/admin/users/${id}`);
  },

  // Update user role
  updateUserRole: (id, role) => {
    return api.patch(`/admin/users/${id}/role`, { role });
  },

  // Get pending properties
  getPendingProperties: (params = {}) => {
    return api.get('/admin/properties/pending', { params });
  },

  // Verify property
  verifyProperty: (id) => {
    return api.patch(`/admin/properties/${id}/verify`);
  },

  // Get analytics
  getAnalytics: () => {
    return api.get('/admin/analytics');
  },
};
