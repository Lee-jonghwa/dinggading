# Multi-stage build for Next.js application
FROM node:18-slim AS base

WORKDIR /app

# Development stage for local development
FROM base AS development
WORKDIR /app/frontend

# Copy package.json and package-lock.json
COPY ./frontend/package*.json ./

# Install dependencies
RUN npm install
ARG NEXT_PUBLIC_API_BASE_URL=https://defaultvalue.example.com
ARG NEXT_PUBLIC_ENV_MODE=DOCKER_FILE

ENV NEXT_PUBLIC_API_BASE_URL=$NEXT_PUBLIC_API_BASE_URL
ENV NEXT_PUBLIC_ENV_MODE=$NEXT_PUBLIC_ENV_MODE

# Copy OpenAPI generated code if it exists
COPY ./frontend/generated /app/generated

# Set the working directory
WORKDIR /app/frontend

# Command to run development server
CMD ["npm", "run", "dev"]

# Test stage for running tests
FROM development AS test
WORKDIR /app/frontend

# Copy source code
COPY ./frontend .

# Run tests
CMD ["npm", "run", "test"]

# Build stage for creating optimized production build
FROM development AS build
WORKDIR /app/frontend

# Copy source code
COPY ./frontend .

# Build Next.js app
RUN npm run build

# Production stage with minimal footprint
FROM base AS production
WORKDIR /app

# Install production dependencies only
COPY ./frontend/package*.json ./
RUN npm install --production

ARG NEXT_PUBLIC_API_BASE_URL=https://defaultvalue.example.com
ARG NEXT_PUBLIC_ENV_MODE=DOCKER_FILE

ENV NEXT_PUBLIC_API_BASE_URL=$NEXT_PUBLIC_API_BASE_URL
ENV NEXT_PUBLIC_ENV_MODE=$NEXT_PUBLIC_ENV_MODE
# Copy built assets from build stage
COPY --from=build /app/frontend/.next ./.next
COPY --from=build /app/frontend/public ./public
COPY --from=build /app/frontend/node_modules ./node_modules
COPY --from=build /app/frontend/package.json ./package.json

# Start the application
CMD ["npm", "start"]