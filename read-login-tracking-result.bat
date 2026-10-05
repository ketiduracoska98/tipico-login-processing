@echo off
cd /d C:\KAFKA\kafka_2.13-4.3.1

echo.
echo ==========================================
echo   login-tracking-result
echo ==========================================
echo.

call .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic login-tracking-result --from-beginning --formatter-property print.partition=true --formatter-property print.offset=true
pause