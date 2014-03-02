java -jar jetty-runner-8.1.9.v20130131.jar janjoon-ui-0.2-BUILD_NUMBER.war
timeout /t 15 /nobreak > NUL
start http://localhost:8080/
