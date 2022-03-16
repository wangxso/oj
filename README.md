# oj
The online judge backend with SpringBoot
## 1. How to run ?
### Pre Run
 
 - Install a MySQL server and run the table.sql
 - Install a Redis Server
 - Install a rabbitmq and set the queue name as "result" and "submit"
 - maven
 - jdk 1.8+

### Run
 
 - Modify the application.yaml.example
    ```bash
        cp application.yaml.example application.yaml
    ```

 - ```shell
    java oj-0.0.1-SNAPSHOT.jar
    ```