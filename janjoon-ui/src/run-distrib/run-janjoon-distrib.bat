java -jar jetty-runner-8.1.9.v20130131.jar --config jetty.xml janjoon-ui-0.2-BUILD_NUMBER.war
timeout /t 35 /nobreak > NUL
start http://localhost:8080/
