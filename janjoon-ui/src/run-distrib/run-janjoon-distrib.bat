java -jar jetty-runner-9.1.3.v20140225.jar --config jetty.xml janjoon-ui-0.2-BUILD_NUMBER.war
timeout /t 35 /nobreak > NUL
start http://localhost:8080/
