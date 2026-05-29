# 测试 Prometheus 配置同步脚本
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "测试 Prometheus 配置同步" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$scriptPath = Join-Path $PSScriptRoot "sync-prometheus-config.py"

if (Test-Path $scriptPath) {
    Write-Host "正在运行同步脚本..." -ForegroundColor Yellow
    python $scriptPath
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ 同步成功!" -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "❌ 同步失败，退出码: $LASTEXITCODE" -ForegroundColor Red
    }
} else {
    Write-Host "❌ 找不到脚本文件: $scriptPath" -ForegroundColor Red
}

Write-Host ""
Write-Host "按任意键退出..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
