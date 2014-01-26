#!/bin/bash
java -jar jetty-runner-8.1.9.v20130131.jar janjoon-ui-0.2-BUILD_NUMBER.war&
echo $! > /tmp/janjoon.pid
sleep 15
nohup ./run-browser.sh &
