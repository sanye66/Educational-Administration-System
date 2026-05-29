@echo off
REM 自动同步 Prometheus 配置脚本
REM 每30秒从 Nacos 获取服务列表并更新 Prometheus 配置

echo ========================================
echo Prometheus 配置自动同步服务
echo ========================================
echo.

:loop
echo [%date% %time%] 正在同步 Prometheus 配置...
python "%~dp0sync-prometheus-config.py"
echo.
echo 等待30秒后再次同步...
timeout /t 30 /nobreak >nul
goto loop
