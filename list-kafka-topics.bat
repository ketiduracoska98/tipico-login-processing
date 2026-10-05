@echo off
cd /d C:\KAFKA\kafka_2.13-4.3.1

echo.
echo ==========================================
echo        Kafka Topics
echo ==========================================
echo.

call .\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

echo.
echo ==========================================
echo        Done
echo ==========================================
echo.

pause