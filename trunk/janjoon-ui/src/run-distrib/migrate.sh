#; This script may be ran by Jenkins as follow :
#; > sudo -n /usr/sbin/migrate actual 480 9494 integ /home/jj_integ

echo There are $# arguments to $0: $*
echo previous version : $1
echo next version : $2
echo application port : $3
echo url path : $4
if [ $# -ne 4 ] && [ $# -ne 5 ]
then
        echo "Error in arguments [previous_rev, next_rev, service_port] :"
        echo "Expected > ./migrate.sh 120 125 9898 urlpath (target_directory)"
        echo "urlpath : janjoon (prod), integ (integ), intern (starit)"
        exit 2
else
	PREVIOUS_BUILD=$1
	if [ $PREVIOUS_BUILD=="actual" ]
	then
                PREVIOUS_BUILD="$(echo /home/jj_integ/lib/janjoon-ui-*.war | cut -f4 -d"-" | cut -f1 -d'.')"
	fi
        NEXT_BUILD=$2
	APPLICATION_PORT=$3
	URL_PATH=$4
        if [ -z "$5" ]
        then
                DIRECTORY=${HOME}
        else
                DIRECTORY=$5
        fi
        echo home = ${HOME}
        echo directory = $DIRECTORY
	cd $DIRECTORY
	$DIRECTORY/bin/stop.sh
	rm -rf $DIRECTORY/bin/janjoon.pid
	rm -rf $DIRECTORY/log/janjoon.log
	rm -rf $DIRECTORY/save/$PREVIOUS_BUILD
	mkdir $DIRECTORY/save/$PREVIOUS_BUILD
	mv $DIRECTORY/bin $DIRECTORY/janjoon-ui $DIRECTORY/lib $DIRECTORY/log $DIRECTORY/license $DIRECTORY/upload $DIRECTORY/save/$PREVIOUS_BUILD/
	unzip $DIRECTORY/janjoon-ui-3.0-$NEXT_BUILD-distrib.zip -d $DIRECTORY/
	mv $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war.zip
	mkdir $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war
	unzip $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war.zip -d $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war/
	cd $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war/
	chmod -R 777 $DIRECTORY/log
	cd -
	mv -f $DIRECTORY/license/messages_*.properties $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war/WEB-INF/classes/com/starit/janjoonweb/ui/mb/i18n/
	mv $DIRECTORY/license/janjoon-base.jar $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war/run-distrib/
	mv -f $DIRECTORY/license/org.eclipse.jgit-3.2.0.201312181205-r.jar $DIRECTORY/lib/janjoon-ui-3.0-$NEXT_BUILD.war/WEB-INF/lib/
	sed -i 's/BUILD_NUMBER/'"$NEXT_BUILD"'/g' $DIRECTORY/bin/start.sh
	sed -i 's/9999/'"$APPLICATION_PORT"'/g' $DIRECTORY/bin/start.sh
	sed -i 's/PATH_URL/'"$URL_PATH"'/g' $DIRECTORY/bin/start.sh
	$DIRECTORY/bin/start.sh
	echo "Migration done. Process re-starting !..."
fi
