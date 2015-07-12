mvn clean install -Dmaven.test.skip=true
cd janjoon-ui
export MAVEN_OPTS="-Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
mvn jetty:run-exploded -Djsse.enableSNIExtension=false

