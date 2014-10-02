echo There are $# arguments to $0: $*
echo previous version : $1
echo next version : $2
echo application port : $3
if [ $# -ne 3 ]
then
echo "Error in arguments [previous_rev, next_rev, service_port] :"
echo "Expected > ./migrate.sh 120 125 9898"
exit 2
fi
rm -rdf save/$1
mkdir save/$1
mv bin janjoon-ui lib log license upload save/$1/
unzip janjoon-ui-1.1-$2-distrib.zip
mv lib/janjoon-ui-1.1-$2.war lib/janjoon-ui-1.1-$2.war.zip
mkdir lib/janjoon-ui-1.1-$2.war
unzip lib/janjoon-ui-1.1-$2.war.zip -d lib/janjoon-ui-1.1-$2.war/
cd lib/janjoon-ui-1.1-$2.war/
ln -s ../../upload/images images
cd -
sed -i 's/9999/$3/g' bin/start.sh

