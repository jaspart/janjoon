#!/bin/bash
java -jar jetty-runner-8.1.9.v20130131.jar --config jetty.xml janjoon-ui-0.2-BUILD_NUMBER.war &> janjoon.log &
echo $! > /tmp/janjoon.pid
sleep 35
#nohup gnome-open http://localhost:8080 & 
nohup xdg-open http://localhost:8080 &

