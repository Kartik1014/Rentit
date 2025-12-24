import React from 'react';
import { Link } from 'react-router-dom';

const PropertyCard = ({ property }) => {
  const primaryImage = property.images?.find(img => img.isPrimary)?.url || property.images?.[0]?.url;
  const imageUrl = primaryImage 
    ? `${import.meta.env.VITE_IMAGE_BASE_URL || 'http://localhost:5000'}${primaryImage}`
    : 'https://via.placeholder.com/400x300?text=No+Image';

  return (
    <Link to={`/property/${property._id}`} className="block">
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300">
        <div className="relative h-48 overflow-hidden">
          <img
            src={imageUrl}
            alt={property.title}
            className="w-full h-full object-cover"
            onError={(e) => {
              e.target.src = 'https://via.placeholder.com/400x300?text=No+Image';
            }}
          />
          <div className="absolute top-2 right-2 bg-primary-600 text-white px-2 py-1 rounded text-sm">
            {property.propertyType}
          </div>
          {property.availabilityStatus === 'rented' && (
            <div className="absolute top-2 left-2 bg-red-600 text-white px-2 py-1 rounded text-sm">
              Rented
            </div>
          )}
        </div>

        <div className="p-4">
          <h3 className="text-xl font-semibold text-gray-800 mb-2 truncate">
            {property.title}
          </h3>
          
          <p className="text-gray-600 text-sm mb-2">
            {property.city}, {property.state}
          </p>

          <div className="flex items-center justify-between mb-3">
            <div>
              <span className="text-2xl font-bold text-primary-600">
                ₹{property.rentAmount.toLocaleString()}
              </span>
              <span className="text-gray-500 text-sm">/month</span>
            </div>
          </div>

          <div className="flex items-center text-gray-600 text-sm space-x-4">
            <span>{property.bedrooms} Beds</span>
            <span>•</span>
            <span>{property.bathrooms} Baths</span>
            {property.areaSqft && (
              <>
                <span>•</span>
                <span>{property.areaSqft} sqft</span>
              </>
            )}
          </div>

          {property.amenities && property.amenities.length > 0 && (
            <div className="mt-3 flex flex-wrap gap-1">
              {property.amenities.slice(0, 3).map((amenity, index) => (
                <span
                  key={index}
                  className="bg-gray-100 text-gray-700 px-2 py-1 rounded text-xs"
                >
                  {amenity}
                </span>
              ))}
              {property.amenities.length > 3 && (
                <span className="bg-gray-100 text-gray-700 px-2 py-1 rounded text-xs">
                  +{property.amenities.length - 3} more
                </span>
              )}
            </div>
          )}
        </div>
      </div>
    </Link>
  );
};

export default PropertyCard;
