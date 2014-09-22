#!/bin/bash
java -jar $HOME/lib/jetty-runner-8.1.9.v20130131.jar --port 9898 --config $HOME/bin/jetty.xml $HOME/lib/janjoon-ui-1.0-BUILD_NUMBER.war &> $HOME/log/janjoon.log &
echo $! > $HOME/bin/janjoon.pid
#sleep 35
#nohup gnome-open http://localhost:8080 & 
#nohup xdg-open http://localhost:8080 &

