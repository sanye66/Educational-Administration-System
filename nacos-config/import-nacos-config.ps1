# Nacos 配置导入脚本 - PowerShell
# 用法: .\import-nacos-config.ps1

$NacosServer = "http://localhost:8848"
$ConfigDir = "nacos-config"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Nacos 配置导入工具" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查 Nacos 是否启动
Write-Host "[1/2] 检查 Nacos 服务..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$NacosServer/nacos/actuator/health" -Method Get -TimeoutSec 5 -UseBasicParsing
    $health = $response.Content | ConvertFrom-Json
    if ($health.status -eq "UP") {
        Write-Host "  Nacos 服务正常 (status: UP)" -ForegroundColor Green
    } else {
        Write-Host "  警告: Nacos 状态异常 (status: $($health.status))" -ForegroundColor Yellow
    }
} catch {
    Write-Host "  错误: Nacos 未启动或无法访问，请检查 Docker 容器" -ForegroundColor Red
    Write-Host "  详情: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[2/2] 开始导入配置文件..." -ForegroundColor Yellow
Write-Host ""

# 配置文件列表
$configFiles = @(
    @{ DataId = "shared-config.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-gateway.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-auth.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-user.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-teacher.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-student.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-course.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-classroom.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-selection.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-grade.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-exam.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-schedule.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-graduation.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-evaluation.yml"; Group = "DEFAULT_GROUP" },
    @{ DataId = "edu-selection-flow-rules"; Group = "SENTINEL_GROUP" },
    @{ DataId = "seataServer.properties"; Group = "DEFAULT_GROUP" }
)

$successCount = 0
$failCount = 0
$skipCount = 0

foreach ($config in $configFiles) {
    $dataId = $config.DataId
    $group = $config.Group
    $filePath = Join-Path $ConfigDir $dataId

    if (-not (Test-Path $filePath)) {
        Write-Host "  跳过: $dataId (文件不存在)" -ForegroundColor DarkYellow
        $skipCount++
        continue
    }

    $content = Get-Content $filePath -Raw -Encoding UTF8

    $url = "$NacosServer/nacos/v1/cs/configs?dataId=$dataId&group=$group"
    $body = "content=" + [System.Web.HttpUtility]::UrlEncode($content)

    try {
        $response = Invoke-RestMethod -Uri $url -Method Post -Body $body -ContentType "application/x-www-form-urlencoded; charset=UTF-8" -TimeoutSec 10
        
        if ($response -eq $true) {
            Write-Host "  成功: $dataId ($group)" -ForegroundColor Green
            $successCount++
        } else {
            Write-Host "  失败: $dataId ($group) - $response" -ForegroundColor Red
            $failCount++
        }
    } catch {
        Write-Host "  错误: $dataId ($group) - $($_.Exception.Message)" -ForegroundColor Red
        $failCount++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  导入完成!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  成功: $successCount" -ForegroundColor Green
Write-Host "  失败: $failCount" -ForegroundColor Red
Write-Host "  跳过: $skipCount" -ForegroundColor DarkYellow
Write-Host ""
Write-Host "  访问 Nacos 控制台查看配置:" -ForegroundColor Cyan
Write-Host "  http://localhost:8848/nacos" -ForegroundColor Cyan
Write-Host ""
