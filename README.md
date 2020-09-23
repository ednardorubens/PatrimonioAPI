# Patrimônio API
API REST para o gerenciamento de patrimônios de uma empresa. 

## Requirementos
 - [JDK 13](https://www.oracle.com/java/technologies/javase-jdk13-downloads.html)
 - [Maven](https://maven.apache.org/download.cgi)

## Construir e rodar a API
```
$ docker run -d --name sqlserver2017 --restart=always -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=79qASUYzx!@1245" -p 1433:1433 mcr.microsoft.com/mssql/server:2017-latest
$ cd PatrimonioAPI
$ mvn package spring-boot:run
```

## Swagger
Você pode aprender e utilizar a api através da documentação do swagger na URL `http://localhost:8080/swagger-ui.html`

## Recursos
 - [Spring Boot](https://spring.io/projects/spring-boot)
 - [Spring Boot Actuator](https://spring.io/guides/gs/actuator-service)
 - [Spring Boot Security](https://spring.io/guides/gs/securing-web)
 - [JJWT Token](https://java.jsonwebtoken.io)
 - [Spring Cache](https://spring.io/guides/gs/caching)
 - [Caffeine](https://www.caffeine.tv)
 - [Spring Data Jpa](https://spring.io/guides/gs/accessing-data-jpa)
 - [Hibernate](https://hibernate.org)
 - [Microsoft SQL Server](https://dzone.com/articles/configuring-spring-boot-for-microsoft-sql-server)
 - [H2 Database](http://www.h2database.com/html/main.html)
 - [Spring Data Envers](https://spring.io/projects/spring-data-envers)
 - [Flyway](https://flywaydb.org/getstarted/java)
 - [Lombok](https://projectlombok.org)
 - [Swagger 2](https://springfox.github.io/springfox/docs/current)
 - [Spring Boot Test](https://spring.io/guides/gs/testing-web)
 - [Spring Boot Security Test](https://docs.spring.io/spring-security/site/docs/current/reference/html5)