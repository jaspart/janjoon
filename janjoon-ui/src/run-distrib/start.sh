#!/bin/bash
java -jar $HOME/lib/jetty-runner-8.1.9.v20130131.jar --port 9999 --path /PATH_URL --config $HOME/bin/jetty.xml $HOME/lib/janjoon-ui-1.2-BUILD_NUMBER.war &> $HOME/log/janjoon.log &
echo $! > $HOME/bin/janjoon.pid
