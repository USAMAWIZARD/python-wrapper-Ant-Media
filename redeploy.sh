#!/bin/sh
AMS_DIR=/usr/local/antmedia
mvn clean install -X -e -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dgpg.skip=true 
OUT=$?

if [ $OUT -ne 0 ]; then
    exit $OUT
fi

rm -r $AMS_DIR/plugins/PluginApp*
cp target/PluginApp.jar /home/usama/AntMedia/Plugins/SamplePlugin/python_wrapper/jpy.jar $AMS_DIR/plugins/

OUT=$?

if [ $OUT -ne 0 ]; then
    exit $OUT
fi
#./start-debug.sh
#java  -Djpy.jpyLib=/home/usama/Pictures/jpy/build/lib.linux-x86_64-3.8/jpy.cpython-38-x86_64-linux-gnu.so -Djpy.jdlLib=/home/usama/Pictures/jpy/build/lib.linux-x86_64-3.8/jdl.cpython-38-x86_64-linux-gnu.so -Djpy.pythonLib=/snap/gnome-3-38-2004/119/usr/lib/python3.8/config-3.8-x86_64-linux-gnu/libpython3.8.so -cp "./:./jpy.jar" org.jpy.JPyExample
