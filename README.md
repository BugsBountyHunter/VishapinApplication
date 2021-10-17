# Welcome to Javaspring Vishapin application!

**Vishapin** is a simple java spring boot application using [{JSON} Placeholder Api](https://jsonplaceholder.typicode.com) to return all comments, save it in database(mysql), get it from database make it searchable, export pdf reports by using [Jasper lib](https://community.jaspersoft.com/project/jasperreports-library) and so on.

# Database Configuration (MySQL)

Before Run application you must create a new database with a new username and password.

 **1. Create New username and pssword** 

    CREATE USER 'springtask'@'localhost' IDENTIFIED BY 'springtask';
    GRANT ALL PRIVILEGES ON * . * TO 'springtask'@'localhost';
    ALTER USER 'springtask'@'localhost' IDENTIFIED WITH mysql_native_password BY 'springtask';

 **2. Create database** 

    DATABASE  IF NOT EXISTS `springdb`;
    
**Note** you can change database (username&password) and database name as you like but you shoud change it in **application.properties**

    spring.datasource.url=jdbc:mysql://localhost:3306/springdb  
    spring.datasource.username=springtask
    spring.datasource.password=springtask

# Pom file and External dependencies

In this project I using **Maven** to manage a project's build.

 **1. Lombok**
	Is a java library that automatically plugs into your editor and build tools, spicing up your java.

    <dependency>  
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    </dependency>

 **2. Springfox ([swagger](http://swagger.io/))**
**Swagger** offers the most powerful and easiest to use tools to take full advantage of the OpenAPI Specification.


    <dependency>  
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
    </dependency>
   
**Note** to using swigger local link: http://localhost:8080/swagger-ui/index.html

# Filter and pagination respons.
![Normal](https://github.com/DEV-A7med/VishapinApplication/blob/main/src/main/resources/assets/img.png "Filter and pagination respons")

 **3. librepdf**
	 	 It Helps in export some data to a pdf file and customize it

    <dependency>  
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.8</version>
    </dependency>
  

 **4. Jasper Reports**
The **JasperReports** Library is the world's most popular open source reporting engine.


    <dependency>  
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.17.0</version>
    </dependency>

 **5. Logback**
Is one of the most widely used logging frameworks in the Java Community.
    
    
    <dependency>  
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    </dependency>
    



# End Points

SmartyPants converts ASCII punctuation characters into "smart" typographic punctuation HTML entities. For example:

|       Vreb         |Path                        			|description|
|----------------|------------------------------------------|-----------------------------|
|GET             |`localhost:8080/api/v1/comments`  		|Make a REST call to this URL ( “ https://jsonplaceholder.typicode.com/comments ” ) and get all the data available and store it into a Database ( MySQL )          |
|GET			 |`localhost:8080/api/v1/comments_from_db`  |"list all the data stored into your Database"  This endpoint searchable by using the following criteria: ( name & email) too apply Pagination on the Response     |
|GET			 |`localhost:8080/api/v1/export/pdf`		|Export pdf file|
|GET			 |`localhost:8080/api/v1/report/{format}`	| Generate formate in two type (html-pdf|
