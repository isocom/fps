@echo off
setLocal EnableDelayedExpansion

set CLASSPATH="..\conf
for /R ..\lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo "The classpath is:"
echo !CLASSPATH!

java -cp !CLASSPATH! name.prokop.bart.fps.drivers.OptimusVivo COM1