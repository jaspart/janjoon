#!/bin/bash
java -jar ../lib/jetty-runner-8.1.9.v20130131.jar --config jetty.xml ../lib/janjoon-ui-1.0-BUILD_NUMBER.war &> ../log/janjoon.log &
echo $! > /tmp/janjoon.pid
sleep 35
#nohup gnome-open http://localhost:8080 & 
nohup xdg-open http://localhost:8080 &

