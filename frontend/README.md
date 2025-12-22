# Rentit Frontend

Frontend application for the Rentit house rental platform built with React, Vite, and Tailwind CSS.

## Features

- User authentication (Login, Register)
- Property browsing and search with filters
- Property detail view with image gallery
- Reviews and ratings display
- Responsive design with Tailwind CSS
- Protected routes for authenticated users
- Role-based navigation (Tenant, Owner, Admin)

## Tech Stack

- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **State Management**: React Context API
- **Icons**: React Icons / Heroicons

## Prerequisites

- Node.js 16 or higher
- npm or yarn

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Create a `.env` file based on `.env.example`:
```bash
cp .env.example .env
```

4. Update the `.env` file with your backend API URL:
```env
VITE_API_BASE_URL=http://localhost:5000/api
VITE_IMAGE_BASE_URL=http://localhost:5000
```

## Running the Application

### Development mode:
```bash
npm run dev
```

The app will start on `http://localhost:3000`

### Build for production:
```bash
npm run build
```

### Preview production build:
```bash
npm run preview
```

## Project Structure

```
frontend/
├── public/
├── src/
│   ├── components/          # Reusable components
│   │   ├── Footer.jsx
│   │   ├── Navbar.jsx
│   │   ├── PropertyCard.jsx
│   │   └── ProtectedRoute.jsx
│   ├── context/             # React Context
│   │   └── AuthContext.jsx
│   ├── pages/               # Page components
│   │   ├── HomePage.jsx
│   │   ├── LoginPage.jsx
│   │   ├── RegisterPage.jsx
│   │   ├── SearchPage.jsx
│   │   └── PropertyDetailPage.jsx
│   ├── services/            # API services
│   │   ├── api.js
│   │   └── propertyService.js
│   ├── utils/               # Utility functions
│   ├── App.jsx              # Main app component
│   ├── main.jsx             # Entry point
│   └── index.css            # Global styles
├── .env.example
├── .gitignore
├── index.html
├── package.json
├── postcss.config.js
├── tailwind.config.js
├── vite.config.js
└── README.md
```

## Available Routes

- `/` - Home page with featured properties
- `/login` - Login page
- `/register` - Registration page
- `/search` - Property search page with filters
- `/property/:id` - Property detail page
- `/tenant/dashboard` - Tenant dashboard (Protected)
- `/owner/dashboard` - Owner dashboard (Protected)
- `/admin/dashboard` - Admin dashboard (Protected)

## Key Components

### AuthContext
Provides authentication state and methods throughout the app:
- `user` - Current user object
- `token` - JWT token
- `isAuthenticated` - Boolean authentication status
- `login()` - Login method
- `register()` - Registration method
- `logout()` - Logout method
- `isOwner`, `isAdmin`, `isTenant` - Role checks

### ProtectedRoute
Wrapper component for routes requiring authentication:
```jsx
<ProtectedRoute roles={['tenant']}>
  <TenantDashboard />
</ProtectedRoute>
```

### PropertyCard
Displays property summary in grid/list view:
- Property image
- Title and location
- Price per month
- Bedrooms, bathrooms, area
- Amenities preview

## API Integration

The frontend communicates with the backend API through Axios. The API client is configured in `src/services/api.js` with:
- Base URL configuration
- Request interceptors for adding JWT tokens
- Response interceptors for token refresh
- Automatic redirect to login on 401 errors

### Using API Services

```javascript
import { propertyService } from '../services/propertyService';

// Get all properties
const response = await propertyService.getProperties({ page: 0, limit: 10 });

// Search properties
const response = await propertyService.searchProperties({
  location: 'Mumbai',
  minPrice: 10000,
  maxPrice: 50000
});

// Get property by ID
const response = await propertyService.getPropertyById(propertyId);
```

## Styling with Tailwind CSS

The project uses Tailwind CSS for styling. Key features:
- Utility-first CSS framework
- Responsive design with breakpoints
- Custom color palette (primary colors)
- Component classes for common patterns

### Custom Tailwind Configuration
See `tailwind.config.js` for custom theme extensions.

## Environment Variables

Create a `.env` file with the following variables:

```env
VITE_API_BASE_URL=http://localhost:5000/api
VITE_IMAGE_BASE_URL=http://localhost:5000
```

## Development Guidelines

### Adding a New Page

1. Create component in `src/pages/`
2. Import in `App.jsx`
3. Add route in the Routes component
4. Add navigation link in Navbar (if needed)

### Adding a New API Service

1. Add methods to `src/services/propertyService.js`
2. Use the configured `api` client from `src/services/api.js`
3. Handle errors appropriately

### State Management

- Use React Context for global state (auth, theme, etc.)
- Use local state (useState) for component-specific state
- Use useEffect for side effects and API calls

## Building for Production

```bash
npm run build
```

This creates an optimized production build in the `dist/` directory.

### Deployment

The build output can be deployed to:
- Vercel
- Netlify
- AWS S3 + CloudFront
- Any static hosting service

Example Vercel deployment:
```bash
vercel deploy
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT
