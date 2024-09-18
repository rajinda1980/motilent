# Building and Deploying Application

### Prerequisites
- Java 17
- Maven

### Build and Test the Application
- Open your terminal window.
- Download the application from GitHub.
  ```
  sudo git clone https://github.com/rajinda1980/motilent.git
  ```
- Navigate to the "motilent" root folder.
  ```
  cd motilent
  ```
- Run the following command to build and package the application.
  ```
  mvn clean package
  ```
  The following log entry should be displayed if the application is built successfully.
  ```
  [INFO] --- maven-jar-plugin:3.4.2:jar (default-jar) @ notification ---
  [INFO] Building jar: /home/kafka/Application/Interview/motilent/target/notification-1.0-SNAPSHOT.jar
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  6.592 s
  [INFO] Finished at: 2024-09-18T12:00:24+01:00
  [INFO] ------------------------------------------------------------------------
  ```
- Run the following command to run test cases.
  ```
  mvn test
  ```
  The following log entry will be displayed if all the test cases run successfully.
  ```
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.828 s -- in com.motlient.notification.processor.WebhookClientWireMockTest
  [INFO] Running com.motlient.notification.processor.WebhookJsonParserTest
  [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.080 s -- in com.motlient.notification.processor.WebhookJsonParserTest
  [INFO] Running com.motlient.notification.processor.NotificationProcessorTest
  [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.091 s -- in com.motlient.notification.processor.NotificationProcessorTest
  [INFO] Running com.motlient.notification.processor.WebhookClientTest
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.083 s -- in com.motlient.notification.processor.WebhookClientTest
  [INFO]
  [INFO] Results:
  [INFO]
  [INFO] Tests run: 47, Failures: 0, Errors: 0, Skipped: 0
  [INFO]
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  4.997 s
  [INFO] Finished at: 2024-09-18T12:01:16+01:00
  [INFO] ------------------------------------------------------------------------
  ```

### To Run the Application
- Execute the following command to run the command-line application:
  ```
  java -jar target/notification-1.0-SNAPSHOT-jar-with-dependencies.jar /home/kafka/motilent/src/main/resources/notification.json
  ```

  Command format:
  ```
  java -jar <jar file name> <path to notification JSON file>
  ```

  Console output
  ```
  Sep 18, 2024 1:10:09 PM com.motlient.notification.processor.NotificationProcessorImpl sendNotification
  INFO: Notification URL : https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb
  =================================== 
  Notification URL: https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb
  Content Sent: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
  Response Received: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
  HTTP Response Code: 200
  Response Time: 757 ms
  ===================================
  Sep 18, 2024 1:10:10 PM com.motlient.notification.NotificationApp process
  INFO: Notification URL: https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb
  Content Sent: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
  Response Received: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
  HTTP Response Code: 200
  Response Time: 757 ms
  ```

# Assumptions

1. The application expects the reportUID and studyInstanceUID fields to be directly part of the notificationContent object, rather than being enclosed in a wrapper object. The reportUID and studyInstanceUID fields should be provided in the root of the notificationContent without any additional nesting
2. The URL must be a valid HTTPS URL. It follows the standard URL structure, including a protocol (https), a domain name (webhook.site), and a path like 3b8c180a-dcac-4700-afee-9ebde4abbfcb.