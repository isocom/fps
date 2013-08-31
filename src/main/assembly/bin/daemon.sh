#!/bin/sh
#
# /etc/init.d/${debian.artifact.name} -- startup script for the Search Logs Consumer / Database Writer
#
# Written by Miquel van Smoorenburg <miquels@cistron.nl>.
# Modified for Debian GNU/Linux	by Ian Murdock <imurdock@gnu.ai.mit.edu>.
#
### BEGIN INIT INFO
# Provides:          ${debian.artifact.name}
# Required-Start:    $local_fs $remote_fs $network $all
# Required-Stop:     $local_fs $remote_fs $network $all
# Should-Start:      $named
# Should-Stop:       $named
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start Java service
# Description:       Start the Java runtime capable service.
### END INIT INFO

if [ -z "$JAVA_HOME" ]
then
  echo "JAVA_HOME was not set, I assume it is standard debian... /usr/lib/jvm/default-java"
  JAVA_HOME=/usr/lib/jvm/default-java
  export JAVA_HOME
else
  echo "JAVA_HOME was externally set to:" $JAVA_HOME
fi

RUNTIME_JSVC=/usr/bin/jsvc
RUNTIME_HOME=/opt/${debian.artifact.name}
RUNTIME_PID=/tmp/${debian.artifact.name}.pid
RUNTIME_USER=bart
RUNTIME_NAME=${debian.artifact.name}
RUNTIME_ENTRY=name.prokop.bart.runtime.RuntimeEngineDaemon

echo "RUNTIME_JSVC:  " $RUNTIME_JSVC
echo "RUNTIME_HOME:  " $RUNTIME_HOME
echo "RUNTIME_PID:   " $RUNTIME_PID
echo "RUNTIME_USER:  " $RUNTIME_USER
echo "RUNTIME_NAME:  " $RUNTIME_NAME
echo "RUNTIME_ENTRY: " $RUNTIME_ENTRY
echo "JAVA_HOME: " $JAVA_HOME

if [ `id -u` -ne 0 ]; then
	echo "You need root privileges to run this script"
	exit 1
fi

cd $RUNTIME_HOME/bin

RUNTIME_CP="../conf"
for i in ../lib/*.jar ; do RUNTIME_CP=$RUNTIME_CP:$i ; done
echo "CLASPATH: " $RUNTIME_CP

case "$1" in
  start)
    echo "Starting ${debian.artifact.name} daemon"
    $RUNTIME_JSVC -procname $RUNTIME_NAME -user $RUNTIME_USER -pidfile $RUNTIME_PID -cp $RUNTIME_CP $RUNTIME_ENTRY
    cd
    echo "Daemon ${debian.artifact.name} started"
	;;

  stop)
    echo "Stopping ${debian.artifact.name} daemon"
    $RUNTIME_JSVC -procname $RUNTIME_NAME -user $RUNTIME_USER -pidfile $RUNTIME_PID -stop -cp $RUNTIME_CP $RUNTIME_ENTRY
    cd
    echo "Daemon ${debian.artifact.name} stopped"
    /bin/sleep 2
	;;

  status)
        ;;
  restart|force-reload)
    $0 stop
    $0 start
	;;
  try-restart)
	;;
  *)
    echo "Usage: $0 {start|stop|restart|try-restart|force-reload|status}"
    exit 1
	;;
esac

exit 0 
