#!/bin/sh
CP="../conf"
for i in ../lib/*.jar ; do CP=$SRE_CP:$i ; done
echo "CLASPATH: " $CP

java -cp $CP name.prokop.bart.runtime.EntryPoint
