java -jar ../lib/jetty-runner-8.1.9.v20130131 --config jetty.xml ../lib/janjoon-ui-0.9-BUILD_NUMBER.war
timeout /t 35 /nobreak > NUL
start http://localhost:8080/
