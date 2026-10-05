@echo off
cd /d C:\KAFKA\kafka_2.13-4.3.1

echo.
echo ==========================================
echo        customer-login-dlt
echo ==========================================
echo.

call .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic customer-login-dlt --from-beginning
pause