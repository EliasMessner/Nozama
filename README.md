# Get Started  

Create new Database (by means of a Script executed in a Docker Container): `docker-compose -f docker-compose.base.yml -f
docker-compose.psqlseed.yml up -d --build`  

Load Data into Database: `docker-compose -f docker-compose.base.yml -f docker-compose.app.yml up -d --build`  

Start Shop Application: `docker-compose -f docker-compose.base.yml -f docker-compose.shop-app.yml up -d --build`  

Inspect Docker Containers: `docker-compose logs -f`  

Stop Docker Database Containers: `docker-compose -f docker-compose.base.yml`  

(note: every Docker-related command needs to be executed from inside the 'Docker' directory)

You can inspect the database at http://localhost:8080/.  