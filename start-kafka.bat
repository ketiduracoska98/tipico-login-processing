@echo off
cd /d C:\KAFKA\kafka_2.13-4.3.1
echo Starting Kafka...

set KAFKA_LOG4J_OPTS=-Dlog4j2.configurationFile=file:/C:/KAFKA/kafka_2.13-4.3.1/config/log4j2.yaml

.\bin\windows\kafka-server-start.bat .\config\server.properties
pause