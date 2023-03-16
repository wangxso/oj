# OJ Online Judge Backend with SpringBoot
This is a backend implementation for an online judge system built using SpringBoot. It provides the functionality for users to submit their code for different programming problems and get the results of the submission. This implementation uses MySQL, Redis, and RabbitMQ as backend components.

## Requirements
- MySQL server
- Redis server
- RabbitMQ
- Java Development Kit (JDK) 1.8 or later
## Pre-run steps
1. Install and configure MySQL server and run the `table.sql` script to create the required tables for the database.
2. Install and configure Redis server.
3. Install and configure RabbitMQ and set the queue name as "result" and "submit".
## Running the application
1 Copy the `application.yaml.example` file to `application.yaml`.
2 Modify the `application.yaml` file with the appropriate configurations for your system.
3 Run the following command to start the application:
```bash
java -jar oj-0.0.1-SNAPSHOT.jar
```
4. Once the application is running, you can access it at `http://localhost:8080`.
## Dependencies
- Frontend: https://github.com/wangxso/onlinejudge-FE
- Judging Core: https://github.com/wangxso/OnlineJudgeCore
- Judging Core Wrapper: https://github.com/wangxso/judger_server
Note: Please make sure to configure the dependencies according to your system before running the application.

## Contributors
This implementation was created by wangxso. Contributions are welcome, please create a pull request with your changes.

## License
This implementation is licensed under the MIT License. See the `LICENSE` file for details.
