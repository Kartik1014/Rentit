import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { propertyService, reviewService } from '../services/propertyService';
import { useAuth } from '../context/AuthContext';

const PropertyDetailPage = () => {
  const { id } = useParams();
  const { user, isAuthenticated } = useAuth();
  const [property, setProperty] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  useEffect(() => {
    fetchProperty();
    fetchReviews();
  }, [id]);

  const fetchProperty = async () => {
    try {
      const response = await propertyService.getPropertyById(id);
      setProperty(response.data.property);
    } catch (error) {
      console.error('Error fetching property:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchReviews = async () => {
    try {
      const response = await reviewService.getPropertyReviews(id, { page: 0, limit: 10 });
      setReviews(response.data.reviews);
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!property) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-xl text-gray-600">Property not found</p>
      </div>
    );
  }

  const images = property.images && property.images.length > 0
    ? property.images
    : [{ url: '/placeholder.jpg' }];

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Image Gallery */}
        <div className="bg-white rounded-lg shadow-lg overflow-hidden mb-8">
          <div className="relative h-96">
            <img
              src={`${import.meta.env.VITE_IMAGE_BASE_URL || 'http://localhost:5000'}${images[currentImageIndex].url}`}
              alt={property.title}
              className="w-full h-full object-cover"
              onError={(e) => {
                e.target.src = 'https://via.placeholder.com/800x600?text=No+Image';
              }}
            />
            {images.length > 1 && (
              <div className="absolute bottom-4 left-0 right-0 flex justify-center space-x-2">
                {images.map((_, index) => (
                  <button
                    key={index}
                    onClick={() => setCurrentImageIndex(index)}
                    className={`w-3 h-3 rounded-full ${
                      index === currentImageIndex ? 'bg-white' : 'bg-gray-400'
                    }`}
                  />
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Property Details */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow p-6 mb-6">
              <h1 className="text-3xl font-bold text-gray-900 mb-4">{property.title}</h1>
              <p className="text-xl text-gray-600 mb-4">
                {property.city}, {property.state}
              </p>

              <div className="flex items-center space-x-6 mb-6">
                <div>
                  <span className="text-3xl font-bold text-primary-600">
                    ₹{property.rentAmount.toLocaleString()}
                  </span>
                  <span className="text-gray-500">/month</span>
                </div>
                <div className="text-gray-600">
                  Deposit: ₹{property.deposit.toLocaleString()}
                </div>
              </div>

              <div className="flex items-center space-x-6 mb-6 text-gray-700">
                <span>{property.bedrooms} Bedrooms</span>
                <span>•</span>
                <span>{property.bathrooms} Bathrooms</span>
                {property.areaSqft && (
                  <>
                    <span>•</span>
                    <span>{property.areaSqft} sqft</span>
                  </>
                )}
              </div>

              <div className="mb-6">
                <h2 className="text-xl font-semibold mb-3">Description</h2>
                <p className="text-gray-700 whitespace-pre-line">{property.description}</p>
              </div>

              {property.amenities && property.amenities.length > 0 && (
                <div className="mb-6">
                  <h2 className="text-xl font-semibold mb-3">Amenities</h2>
                  <div className="flex flex-wrap gap-2">
                    {property.amenities.map((amenity, index) => (
                      <span
                        key={index}
                        className="bg-gray-100 text-gray-700 px-3 py-1 rounded-full"
                      >
                        {amenity}
                      </span>
                    ))}
                  </div>
                </div>
              )}

              <div>
                <h2 className="text-xl font-semibold mb-3">Address</h2>
                <p className="text-gray-700">{property.address}</p>
                <p className="text-gray-700">
                  {property.city}, {property.state} - {property.pincode}
                </p>
              </div>
            </div>

            {/* Reviews Section */}
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-2xl font-semibold mb-4">Reviews</h2>
              {reviews && reviews.length > 0 ? (
                <div className="space-y-4">
                  {reviews.map((review) => (
                    <div key={review.id} className="border-b border-gray-200 pb-4 last:border-0">
                      <div className="flex items-center mb-2">
                        <span className="font-semibold">{review.tenant?.username}</span>
                        <span className="ml-auto text-yellow-500">
                          {'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}
                        </span>
                      </div>
                      <p className="text-gray-700">{review.comment}</p>
                      <p className="text-sm text-gray-500 mt-1">
                        {new Date(review.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500">No reviews yet</p>
              )}
            </div>
          </div>

          {/* Sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow p-6 sticky top-8">
              <h3 className="text-xl font-semibold mb-4">Owner Details</h3>
              <div className="mb-6">
                <p className="text-gray-700">
                  <strong>Name:</strong> {property.owner?.username}
                </p>
                <p className="text-gray-700">
                  <strong>Email:</strong> {property.owner?.email}
                </p>
                {property.owner?.phone && (
                  <p className="text-gray-700">
                    <strong>Phone:</strong> {property.owner.phone}
                  </p>
                )}
              </div>

              {isAuthenticated && user?.role === 'tenant' && (
                <a
                  href={`/bookings/new?propertyId=${property.id}`}
                  className="block w-full bg-primary-600 text-white text-center px-6 py-3 rounded-md hover:bg-primary-700 transition-colors"
                >
                  Book Now
                </a>
              )}

              {!isAuthenticated && (
                <a
                  href="/login"
                  className="block w-full bg-primary-600 text-white text-center px-6 py-3 rounded-md hover:bg-primary-700 transition-colors"
                >
                  Login to Book
                </a>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PropertyDetailPage;
