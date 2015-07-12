java -jar ../lib/jetty-runner-8.1.14.v20131031 --config jetty.xml ../lib/janjoon-ui-3.0-BUILD_NUMBER.war
timeout /t 35 /nobreak > NUL
start http://localhost:8080/
