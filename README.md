# Nozama

Nozama is a media shop for books, dvds and music cds. It was created as part of the [media store project](https://git.informatik.uni-leipzig.de/dbs/dbpraktikum-mediastore/-/tree/d10a346bd543da2ad86c7bedd09a0d806335995c) 
at Leipzig University.  
Nozama consists of two separate applications. The application in the app/ directory is responsible for reading the 
available products from XML files. It puts the available products into a postgres database. The actual media shop application is located in the shop-app/ directory.
It consists of a backend that utilizes Hibernate and a console application that allows reading product information (price, quantity etc.) and adding reviews.

### Get Started  

Switch to the Docker/ directory: `cd Docker`

Create the database container and create a new database: `docker-compose -f docker-compose.base.yml -f
docker-compose.psqlseed.yml up -d --build`  

Load the products into the database: `docker-compose -f docker-compose.base.yml -f docker-compose.app.yml up -d --build`  

Create and access the shop container: `docker-compose -f docker-compose.base.yml -f docker-compose.shop-app.yml up -d --build 
&& docker exec -it docker_shop-app_1 sh`   

Start the console application: `java -jar shop-app-1.0-SNAPSHOT-jar-with-dependencies.jar`

Stop and remove the database container: `docker-compose -f docker-compose.base.yml down`

In debug mode, you can inspect the product database at http://localhost:8080/.  