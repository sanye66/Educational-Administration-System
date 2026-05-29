# Prometheus 配置自动同步服务 (PowerShell版本)
# 每30秒从 Nacos 获取服务列表并更新 Prometheus 配置

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Prometheus 配置自动同步服务" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$scriptPath = Join-Path $PSScriptRoot "sync-prometheus-config.py"

if (-not (Test-Path $scriptPath)) {
    Write-Host "❌ 错误: 找不到同步脚本: $scriptPath" -ForegroundColor Red
    exit 1
}

Write-Host "同步脚本: $scriptPath" -ForegroundColor Green
Write-Host "同步间隔: 30秒" -ForegroundColor Green
Write-Host ""
Write-Host "按 Ctrl+C 停止服务" -ForegroundColor Yellow
Write-Host ""

while ($true) {
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Write-Host "[$timestamp] 正在同步 Prometheus 配置..." -ForegroundColor Cyan
    
    try {
        python $scriptPath
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ 同步成功" -ForegroundColor Green
        } else {
            Write-Host "❌ 同步失败，退出码: $LASTEXITCODE" -ForegroundColor Red
        }
    } catch {
        Write-Host "❌ 执行出错: $_" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "等待30秒后再次同步..." -ForegroundColor Gray
    
    # 倒计时显示
    for ($i = 30; $i -gt 0; $i--) {
        Write-Host "`r下次同步倒计时: $i 秒  " -NoNewline -ForegroundColor DarkGray
        Start-Sleep -Seconds 1
    }
    
    Write-Host "`r" -NoNewline  # 清除倒计时行
    Write-Host ""
}
