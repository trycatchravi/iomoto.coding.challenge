##Description
This service implements the solution to complete iomoto coding challenge.
## Supported versions:
- Java 8 to 15
- Spring boot 2.6.4
- MongoDB 5.0.2
- MongoDB Java driver 4.5.0
- Maven 3.8.4
- Swagger 3.0.0
## Commands
- Start the server in a console with `mvn spring-boot:run`.
## Swagger 3
- Swagger 3 is already configured in this project in `SpringFoxConfig.java`.
- The Swagger UI can be seen at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
## Missing Implementations
- As I am lacking experience of working with large scale application with MongoDB, I was not able to figure out how to store both Json and XML
data in the same document column. As of now I am assuming String data type can handle it.
- I have implemented the test cases, but they are failing because of some MongoDB configurations and getting error 
`This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.`
- I would appreciate if you can provide some suggestion in review to fix these issues, it may help to improve my MongoDB skills.
## Author
- Ravinder Kumar @trycatchravi