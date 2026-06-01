# Frontend

Angular 21 application with Nx workspace, Tailwind CSS 4, and standalone components.

## Prerequisites

- Node.js 20+
- npm

## Development

```bash
npm install
npx nx serve --host 0.0.0.0
```

The dev server runs on `http://localhost:4200`.

## Build

```bash
npx nx build
```

Output goes to `dist/edc-frontend/browser/`.

## Docker

The root `docker-compose.yml` builds this frontend as the `frontend` service on port 4200.

## Architecture

- **Nx workspace** with a single Angular application
- **Tailwind CSS 4** for styling
- **Standalone components** (no NgModules)
- **Angular Router** with lazy-loaded routes
- **HttpClient** with JWT auth interceptor
- **Environment files** for dev/prod API configuration

## Structure

```
src/
├── app/
│   ├── pages/          # Route-level page components
│   │   ├── home/       # Landing page
│   │   ├── login/      # Authentication
│   │   └── register/   # Registration
│   ├── services/       # API client services
│   ├── interceptors/   # HTTP interceptors
│   ├── guards/         # Route guards
│   └── shared/         # Shared UI components (Zard)
├── environments/       # Environment configuration
├── main.ts
├── index.html
└── styles.css
```

## API Configuration

API base URLs are configured in `src/environments/`:

| Environment | Product Service | Inventory Service |
|-------------|----------------|-------------------|
| Development | `localhost:8080/api` | `localhost:8081/api` |
| Production  | `/api/product` | `/api/inventory` |
