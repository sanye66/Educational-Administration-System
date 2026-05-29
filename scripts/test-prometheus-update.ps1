# 测试Prometheus动态配置更新功能
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Prometheus 动态配置更新测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查edu-monitor服务是否运行
Write-Host "1. 检查edu-monitor服务状态..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8093/actuator/health" -Method Get -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ edu-monitor服务正在运行" -ForegroundColor Green
    } else {
        Write-Host "❌ edu-monitor服务返回异常状态码: $($response.StatusCode)" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ 无法连接到edu-monitor服务，请确保服务已启动" -ForegroundColor Red
    Write-Host "错误详情: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 获取当前服务列表
Write-Host "2. 获取当前注册的服务列表..." -ForegroundColor Yellow
try {
    $servicesResponse = Invoke-WebRequest -Uri "http://localhost:8093/api/prometheus/services" -Method Get -TimeoutSec 5
    $services = $servicesResponse.Content | ConvertFrom-Json
    Write-Host "发现的服务: $($services.data -join ', ')" -ForegroundColor Green
} catch {
    Write-Host "❌ 获取服务列表失败: $_" -ForegroundColor Red
}

Write-Host ""

# 手动触发配置更新
Write-Host "3. 手动触发Prometheus配置更新..." -ForegroundColor Yellow
try {
    $updateResponse = Invoke-WebRequest -Uri "http://localhost:8093/api/prometheus/update" -Method Post -TimeoutSec 10
    $result = $updateResponse.Content | ConvertFrom-Json
    if ($result.code -eq 200) {
        Write-Host "✅ 配置更新成功: $($result.message)" -ForegroundColor Green
    } else {
        Write-Host "❌ 配置更新失败: $($result.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ 触发配置更新失败: $_" -ForegroundColor Red
}

Write-Host ""

# 检查生成的配置文件
Write-Host "4. 检查生成的Prometheus配置文件..." -ForegroundColor Yellow
$configPath = ".\config\prometheus.yml"
if (Test-Path $configPath) {
    Write-Host "✅ Prometheus配置文件存在: $configPath" -ForegroundColor Green
    Write-Host "文件内容预览:" -ForegroundColor Cyan
    Get-Content $configPath | Select-Object -First 10 | ForEach-Object { Write-Host "  $_" }
} else {
    Write-Host "❌ Prometheus配置文件不存在: $configPath" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "测试完成!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "提示:" -ForegroundColor Yellow
Write-Host "- Java版本的自动更新服务已在edu-monitor中运行" -ForegroundColor White
Write-Host "- 每30秒自动检查服务变化并更新配置" -ForegroundColor White
Write-Host "- 可通过 POST http://localhost:8093/api/prometheus/update 手动触发更新" -ForegroundColor White
Write-Host "- 可通过 GET http://localhost:8093/api/prometheus/services 查看服务列表" -ForegroundColor White
