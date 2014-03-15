#!/bin/bash
java -jar jetty-runner-9.1.3.v20140225.jar --config jetty.xml janjoon-ui-0.2-BUILD_NUMBER.war &> janjoon.log &
echo $! > /tmp/janjoon.pid
sleep 35
#nohup gnome-open http://localhost:8080 & 
nohup xdg-open http://localhost:8080 &

