# Building and Deploying Application

### Prerequisites
- Docker
- Maven

### Build and Run the Application
- Open your terminal window.
- Download the application from GitHub.
  ```
  sudo git clone https://github.com/rajinda1980/navigator.git
  ```
- Navigate to the "navigator" root folder.
  ```
  cd navigator
  ```
- Run the following command to build the Docker image.
  ```
  sudo docker build -t navigator:v1 .
  ```
- Run the Docker image.
  ```
  sudo docker run -d -p 8080:8090 --name navigator-container navigator:v1
  ```
  > -d: Runs the container in detached mode (in the background). <br>
  > -p 8080:8080: Maps port 8080 on your local machine to port 8080 inside the container. <br>
  > --name navigator-container: Assigns a name to the container (navigator-container). <br>
  > navigator:v1: The name and tag of the Docker image. <br>
- The following log entry should be displayed if the application is running successfully.
  ```
  2024-08-28T19:52:01.689Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8090 (http) with context path '/'
  2024-08-28T19:52:01.711Z  INFO 1 --- [           main] org.phinxt.navigator.NavigatorApp        : Started NavigatorApp in 2.686 seconds (process running for 3.22)
  ```

### How to Test the application
- Open the Docker log file if it is not already open in the terminal window
  ```
  sudo docker logs -f <container id>
  Ex : sudo docker logs -f 89015aae79d9
  ```
- Swagger provides information about the payload. Please follow the URL below to view the payload details
  ```
  http://localhost:8080/swagger-ui/index.html
  ```
- Open Postman.
- Type the following url:
  ```
  localhost:8080/api/v1/hoover/clean
  ```
- Add the following json payload:
  ```
  { 
      "roomSize" : [5, 5],
      "coords" : [1, 2],
      "patches" : [
         [1, 0],
         [2, 2],
         [2, 3]
      ],
      "instructions" : "NNESEESWNWW"
  }
- Set the Content-Type to application/json
- Send the request
- The following response should be received
  ```
  {
     "coords" : [1, 3],
     "patches" : 1
  }
  ```

### Error response
- Error responses should be generated according to the following format:
  ```
  {
    "timestamp": "2024-08-25T21:33:14.45788239",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation Failed",
    "detail": [
        {
            "field": "coords",
            "issue": "Coordinates must have exactly 2 values"
        },
        {
            "field": "roomSize",
            "issue": "Room size must have exactly 2 dimensions"
        }
    ]
  }
  ```


### External parameters
No external parameters are required to be provided.

### How to run test cases
- Navigate to the "navigator" root folder.
- Run the following command
  ```
  mvn test
  ```
- The following log entry should be displayed if all the test cases are successful.
  ```
  [INFO] Results:
  [INFO]
  [INFO] Tests run: 82, Failures: 0, Errors: 0, Skipped: 0
  [INFO]
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  9.607 s
  ```
- The unit test cases and integration test cases cover the following functionalities:
  - Positive values
  - Negative values
  - Null values
  - Empty values
  - All validations
  - Each individual unit of the application (controller class, validation class, service class, dispatcher class, and DTO classes)
  - End-to-end flow

***
### How to Package and Run the Application if It Cannot Run with Docker
- Navigate to the "navigator" root folder.
- Run the following command
  ```
  mvn clean package
  ```
- The following log entry should be displayed if the application build successful.
  ```
  [INFO] --- spring-boot-maven-plugin:3.3.2:repackage (repackage) @ navigator ---
  [INFO] Replacing main artifact /home/kafka/Application/Interview/navigator/target/navigator-1.0-SNAPSHOT.jar with repackaged archive, adding nested dependencies in BOOT-INF/.
  [INFO] The original artifact has been renamed to /home/kafka/Application/Interview/navigator/target/navigator-1.0-SNAPSHOT.jar.original
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  10.724 s
  [INFO] Finished at: 2024-08-28T21:58:12+01:00
  [INFO] ------------------------------------------------------------------------
  ```
- Navigate to target folder and run the following command to start the application
  ```
  java -jar navigator-1.0-SNAPSHOT.jar
  ```
- The following log entry should be displayed if the application is running successfully.
  ```
  2024-08-28T22:03:24.988+01:00  INFO 2824981 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8090 (http) with context path '/'
  2024-08-28T22:03:25.008+01:00  INFO 2824981 --- [           main] org.phinxt.navigator.NavigatorApp        : Started NavigatorApp in 2.057 seconds (process running for 2.433)
  ```
  
***
### Assumptions

1. Patches can be null or empty. This means coordinates for dirty patches are not provided with the request, so the application assumes there are no dirty patches to be cleaned by the hoover.
2. Instructions can be null or empty. This means the hoover will not change its location.
3. The application accepts only application/json requests.
4. However, the application allows any website to interact with the API since the requirement does not specify which origin should be allowed to access the API. Once the requirement is clarified, CorsConfig can be explicitly updated with allowed origins based on trusted domains.
   ```
   corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
   ```
5. The Authorization header (e.g., for authorization tokens) has not been used since the application does not use any token-based communication. If the API is updated to use tokens, such as JWT tokens generated from an OAuth server, then the Authorization header can be added to the setAllowedHeaders attribute.
   ```
   corsConfiguration.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE));
   ```
6. Currently, the API supports only POST requests. Therefore, the CORS configuration allows only POST calls. This can be enhanced with other methods if the API is updated to support those methods in the future.
   ```
        corsConfiguration.setAllowedMethods(
                Arrays.asList(
                        HttpMethod.POST.name()
                )
        )
   ```

***
### Useful commands
- List Docker processes.
  ```
  sudo docker ps
  ```
- Show Docker log file.
  ```
  sudo docker logs -f 89015aae79d9
  ```
- Stop Docker container.
  ```
  sudo docker stop 89015aae79d9
  ```
