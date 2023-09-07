# Quanloop Todo Service Backend

application running port: 9000
Run command : 

### Approach 1
1. Here we can run the application in 3 different ways. Since application should be supported to
Postgres DB, you have to execute below commands. Application should work in docker support environment

Go to the root directory and execute below commands
1. ./mvnw clean package
2. docker-compose up --build

### Approach 2

If you want to run application by pointing  your own postgresDB,
you can change the below properties on application.properies file

##### spring.datasource.url={your postgres database url}
##### spring.datasource.username={username of the connecting user}
##### spring.datasource.password={password of the connecting user}

after setting up database configuration, 

1. you can just run in your IDE default or ./mvnw spring-boot:run

### Approach 3

This application supports to run on h2 database as well
you can uncomment h2 database properties and comment out postgres db properties in application.properties file
1. you can just run in your IDE default or ./mvnw spring-boot:run

## Login Credential
1. Here Authentication is handled using jwt token
2. below user can be used in the application and all authenticated related functionalities are eligible for this user
          username- quanloop
          password- quanloop@123

Test Cases
1. All the test cases are written under the "src/test" package.

### NOTE - 
There might be some errors in the log when application runs in first time. please ignore it. This is due the
setting below property value to create. ( if it is update, then no error occurs)

spring.jpa.hibernate.ddl-auto=create