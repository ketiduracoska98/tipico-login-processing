@echo off
cd /d C:\KAFKA\kafka_2.13-4.3.1

echo.
echo ==========================================
echo   Kafka Producer - customer-login
echo ==========================================
echo.
echo Add JSON message and press ENTER.
echo.

.\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic customer-login

pause