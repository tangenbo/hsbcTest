@echo off

setlocal

set classpath=lib\*;properties

title server.lax
java\jre\bin\java -server -showversion -Xms256m -Xmx1024m server.com.hsbc.ServerEntrance %*
