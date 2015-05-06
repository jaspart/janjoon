#!/bin/bash
BASEDIR=$(dirname $0)/..
java -jar -Djsse.enableSNIExtension=false -Dorg.eclipse.jetty.server.Request.maxFormKeys=2000 $BASEDIR/lib/jetty-runner-8.1.14.v20131031.jar --port 9999 --path /PATH_URL --config $BASEDIR/bin/jetty.xml $BASEDIR/lib/janjoon-ui-2.0-BUILD_NUMBER.war &> $BASEDIR/log/janjoon.log &
echo $! > $BASEDIR/bin/janjoon.pid
