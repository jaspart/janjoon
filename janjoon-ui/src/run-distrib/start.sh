#!/bin/bash
java -jar -Djsse.enableSNIExtension=false -Dorg.eclipse.jetty.server.Request.maxFormKeys=2000 $HOME/lib/jetty-runner-8.1.14.v20131031.jar --port 9999 --path /PATH_URL --config $HOME/bin/jetty.xml $HOME/lib/janjoon-ui-2.0-BUILD_NUMBER.war &> $HOME/log/janjoon.log &
echo $! > $HOME/bin/janjoon.pid
