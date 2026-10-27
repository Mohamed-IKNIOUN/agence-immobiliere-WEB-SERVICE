# Real Estate Agency Platform (Microservices Architecture)

This repository contains a polyglot microservices-based real estate platform built with Docker. Each service runs in its own isolated container and can be developed using a different programming language.

## Project Structure
- `announcements-service/` - Property listings & CRUD operations (Spring boot)
- `offers-service/` - Bids, price negotiations & offer tracking (Spring boot)
- `favourites-service/` - Saved & bookmarked listings 
- `visits-service/` - Property tour scheduling & calendar management 

## How to Run the Entire Platform
Make sure you have [Docker](https://www.docker.com/) and Docker Compose installed on your machine. From the root directory, run:

```bash
docker compose up --build