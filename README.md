# Integrated-School-Management-System

A centralized Integrated School Management Portal (ISMP) for role-based institutional data governance and oversight.

Repository layout
- ismp-backend — Spring Boot backend (Java 17, Maven)
- ismp-frontend — React frontend (Create React App)

Quick start (development)

Prerequisites:
- Java 17
- Maven 3.8+
- Node.js 20+, npm
- Docker & docker-compose (optional)
- PostgreSQL (used in docker-compose example)

Run backend locally:
```bash
cd ismp-backend
# copy example config or set environment variables (see application.yml or .env)
mvn spring-boot:run
```

Run frontend locally:
```bash
cd ismp-frontend
npm ci
npm start
# opens http://localhost:3000
```

Run tests:
- Backend: `cd ismp-backend && mvn test`
- Frontend: `cd ismp-frontend && npm test -- --watchAll=false`

Build for production:
- Backend: `cd ismp-backend && mvn -DskipTests package`
- Frontend: `cd ismp-frontend && npm run build`

Docker (local dev)
- Build & start all services:
```bash
docker compose up --build
# Backend will be available at http://localhost:8080
# Frontend will be available at http://localhost:3000 (or nginx port in compose)
```

CI
A GitHub Actions workflow runs builds & tests for both backend and frontend on push and PRs.

Next suggestions
1. Add .env.example for environment variables (DATABASE_URL, JWT_SECRET, etc.).
2. Add Checkstyle/SpotBugs integration in the backend POM (snippet provided).
3. Add ESLint/Prettier + lint scripts for the frontend (configs included).
4. Add more unit/integration tests and code coverage reporting.
5. Consider migrating the frontend to Vite for better build speed / React 19 compatibility if you run into issues with react-scripts.

Security notes
- Keep JWT signing keys out of source control; store them in secrets or a vault.
- Use Dependabot (already included) and periodically run security scans.
