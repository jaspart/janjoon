#!/bin/bash
java -jar -Djsse.enableSNIExtension=false $HOME/lib/jetty-runner-8.1.14.v20131031.jar --port 9999 --path /PATH_URL --config $HOME/bin/jetty.xml $HOME/lib/janjoon-ui-1.2-BUILD_NUMBER.war &> $HOME/log/janjoon.log &
echo $! > $HOME/bin/janjoon.pid
