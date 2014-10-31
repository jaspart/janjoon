echo There are $# arguments to $0: $*
echo previous version : $1
echo next version : $2
echo application port : $3
echo url path : $4
if [ $# -ne 4 ]
then
	echo "Error in arguments [previous_rev, next_rev, service_port] :"
	echo "Expected > ./migrate.sh 120 125 9898 urlpath"
	echo "urlpath : janjoon (prod), integ (integ), intern (starit)"
	exit 2
else
	$HOME/bin/stop.sh
	rm -rf $HOME/bin/janjoon.pid
	rm -rf $HOME/log/janjoon.log
	rm -rf $HOME/save/$1
	mkdir $HOME/save/$1
	mv $HOME/bin $HOME/janjoon-ui $HOME/lib $HOME/log $HOME/license $HOME/upload $HOME/save/$1/
	unzip $HOME/janjoon-ui-1.2-$2-distrib.zip -d $HOME/
	mv $HOME/lib/janjoon-ui-1.2-$2.war $HOME/lib/janjoon-ui-1.2-$2.war.zip
	mkdir $HOME/lib/janjoon-ui-1.2-$2.war
	unzip $HOME/lib/janjoon-ui-1.2-$2.war.zip -d $HOME/lib/janjoon-ui-1.2-$2.war/
	cd $HOME/lib/janjoon-ui-1.2-$2.war/
	ln -s ../../upload/images images
	cd -
	sed -i 's/9999/$3/g' $HOME/bin/start.sh
	sed -i 's/PATH_URL/$4/g' $HOME/bin/start.sh
	$HOME/bin/start.sh
	echo "Migration done. Process re-started!..."
fi
