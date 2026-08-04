# Multi-stage build for React app, serve with nginx
FROM node:20-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html
# Copy custom nginx config if needed (optional)
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
