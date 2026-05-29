# 部署脚本模板

本文档提供部署相关的脚本模板，基于回滚策略规范生成。

## 1. Docker 构建脚本

```bash
#!/bin/bash
# build-docker.sh - Docker 构建脚本

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

SERVICE_NAME=${1:-"edu-user"}
VERSION=${2:-"latest"}
REGISTRY=${3:-"edu-system"}

echo -e "${GREEN}开始构建 Docker 镜像: ${SERVICE_NAME}:${VERSION}${NC}"

# 构建镜像
docker build -t ${REGISTRY}/${SERVICE_NAME}:${VERSION} .

# 推送镜像
docker push ${REGISTRY}/${SERVICE_NAME}:${VERSION}

echo -e "${GREEN}构建完成: ${REGISTRY}/${SERVICE_NAME}:${VERSION}${NC}"
```

## 2. K8s 部署脚本

```bash
#!/bin/bash
# deploy-k8s.sh - K8s 部署脚本

# 参数检查
if [ $# -lt 2 ]; then
    echo "用法: $0 <服务名> <版本> [环境]"
    echo "示例: $0 edu-user v1.0.0 dev"
    exit 1
fi

SERVICE_NAME=$1
VERSION=$2
ENV=${3:-"prod"}
NAMESPACE="education-${ENV}"

# 替换镜像版本
sed -i "s|image: edu-system/${SERVICE_NAME}:.*|image: edu-system/${SERVICE_NAME}:${VERSION}|" \
    k8s/deployment-${ENV}.yaml

# 应用部署
kubectl apply -f k8s/deployment-${ENV}.yaml -n ${NAMESPACE}

# 等待部署完成
kubectl rollout status deployment/${SERVICE_NAME} -n ${NAMESPACE}

# 验证服务
kubectl get pods -n ${NAMESPACE} -l app=${SERVICE_NAME}

echo "部署完成: ${SERVICE_NAME}:${VERSION}"
```

## 3. 回滚脚本

```bash
#!/bin/bash
# rollback.sh - K8s 回滚脚本

# 参数检查
if [ $# -lt 1 ]; then
    echo "用法: $0 <服务名> [版本号]"
    echo "示例: $0 edu-user     # 回滚到上一版本"
    echo "示例: $0 edu-user 3   # 回滚到第3个版本"
    exit 1
fi

SERVICE_NAME=$1
REVISION=$2
NAMESPACE="education-prod"

# 执行回滚
if [ -z "$REVISION" ]; then
    echo "回滚到上一版本..."
    kubectl rollout undo deployment/${SERVICE_NAME} -n ${NAMESPACE}
else
    echo "回滚到版本 ${REVISION}..."
    kubectl rollout undo deployment/${SERVICE_NAME} -n ${NAMESPACE} --to-revision=${REVISION}
fi

# 等待回滚完成
kubectl rollout status deployment/${SERVICE_NAME} -n ${NAMESPACE}

# 验证状态
kubectl get pods -n ${NAMESPACE} -l app=${SERVICE_NAME}

echo "回滚完成"
```

## 4. 数据库回滚脚本

```bash
#!/bin/bash
# rollback-db.sh - 数据库回滚脚本

# 参数检查
if [ $# -lt 2 ]; then
    echo "用法: $0 <服务名> <sql文件>"
    echo "示例: $0 edu-user rollback.sql"
    exit 1
fi

SERVICE_NAME=$1
SQL_FILE=$2
DB_NAME="edu_${SERVICE_NAME}"

echo "开始执行数据库回滚: ${DB_NAME}"

# 备份当前数据
BACKUP_FILE="/backup/${DB_NAME}_$(date +%Y%m%d_%H%M%S).sql"
echo "备份数据到: ${BACKUP_FILE}"
mysqldump -u root -p${MYSQL_ROOT_PASSWORD} ${DB_NAME} > ${BACKUP_FILE}

# 执行回滚脚本
echo "执行回滚脚本: ${SQL_FILE}"
mysql -u root -p${MYSQL_ROOT_PASSWORD} ${DB_NAME} < ${SQL_FILE}

# 验证结果
echo "验证数据..."
mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT COUNT(*) FROM ${DB_NAME}.sys_user;"

echo "数据库回滚完成"
```

## 5. 健康检查脚本

```bash
#!/bin/bash
# health-check.sh - 服务健康检查脚本

SERVICE_NAME=$1
PORT=${2:-8080}
HOST=${3:-localhost}
MAX_RETRIES=10

echo "开始健康检查: ${SERVICE_NAME}"

for i in $(seq 1 ${MAX_RETRIES}); do
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://${HOST}:${PORT}/actuator/health)

    if [ "$RESPONSE" = "200" ]; then
        echo "服务健康检查通过: ${SERVICE_NAME}"
        exit 0
    fi

    echo "尝试 ${i}/${MAX_RETRIES}: HTTP ${RESPONSE}"
    sleep 3
done

echo "健康检查失败: ${SERVICE_NAME}"
exit 1
```

## 6. 一键部署脚本

```bash
#!/bin/bash
# deploy-all.sh - 一键部署脚本

# 环境检查
if [ ! -f ".env" ]; then
    echo "错误: .env 文件不存在"
    exit 1
fi

source .env

# 部署顺序（按依赖关系）
SERVICES=(
    "edu-common"
    "edu-api"
    "edu-gateway"
    "edu-auth"
    "edu-user"
    "edu-student"
    "edu-teacher"
    "edu-course"
)

for SERVICE in "${SERVICES[@]}"; do
    echo "========================================="
    echo "部署服务: ${SERVICE}"
    echo "========================================="

    # 构建
    ./build-docker.sh ${SERVICE} ${VERSION}

    # 部署
    ./deploy-k8s.sh ${SERVICE} ${VERSION} ${ENV}

    # 健康检查
    ./health-check.sh ${SERVICE}

    if [ $? -ne 0 ]; then
        echo "部署失败: ${SERVICE}"
        read -p "是否回滚? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            ./rollback.sh ${SERVICE}
        fi
        exit 1
    fi
done

echo "========================================="
echo "所有服务部署完成!"
echo "========================================="
```

## 7. 流水线的回滚配置 (Harness YAML)

```yaml
pipeline:
  name: Deploy with Rollback
  identifier: deploy_with_rollback

  stages:
    - stage:
        name: Deploy
        identifier: Deploy
        spec:
          execution:
            steps:
              - step:
                  type: K8sRollingDeploy
                  name: Deploy to Prod
                  identifier: deploy
                  spec:
                    skipDryRun: false
                    # 开启失败自动回滚
                    prune: false

              - step:
                  type: K8sRollingRollback
                  name: Rollback on Failure
                  identifier: rollback
                  spec:
                    # 回滚到上一版本
                    lastSuccessful: true
                  when:
                    stageStatus: FAILED
```

## 8. 紧急回滚 Checklist

```
□ 确认影响范围
□ 通知相关人员（电话/短信/邮件）
□ 执行回滚脚本
□ 验证服务状态
□ 确认数据一致性
□ 通知相关人员回滚完成
□ 记录问题
□ 分析根因
```
