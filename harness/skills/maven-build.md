# Maven 构建技能

本文档提供 Maven 项目构建的标准流水线模板。

## Java Maven 流水线

```yaml
pipeline:
  name: Java Maven Build
  identifier: java_maven_build
  projectIdentifier: educational_administration
  orgIdentifier: default

  variables:
    - name: MAVEN_OPTS
      value: "-DskipTests"
      type: String

  stages:
    - stage:
        name: Build
        identifier: Build
        spec:
          execution:
            steps:
              - step:
                  type: Run
                  name: Maven Build
                  identifier: maven_build
                  spec:
                    image: maven:3.9-eclipse-temurin-11
                    shell: Sh
                    command: |
                      mvn clean package -DskipTests -B
                    envVariables:
                      MAVEN_OPTS: "-Xmx1024m"

              - step:
                  type: Run
                  name: Unit Tests
                  identifier: unit_tests
                  spec:
                    image: maven:3.9-eclipse-temurin-11
                    shell: Sh
                    command: |
                      mvn test -B

              - step:
                  type: Run
                  name: Code Analysis
                  identifier: code_analysis
                  spec:
                    image: maven:3.9-eclipse-temurin-11
                    shell: Sh
                    command: |
                      mvn sonar:sonar \
                        -Dsonar.projectKey=edu-system \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN}

    - stage:
        name: Docker Build
        identifier: Docker_Build
        spec:
          execution:
            steps:
              - step:
                  type: BuildAndPushDockerRegistry
                  name: Build and Push
                  identifier: build_push
                  spec:
                    connectorRef: dockerhub
                    repo: edu-system/edu-service
                    tags:
                      - latest
                      - <+codebase.commitSha>
                    dockerfile: Dockerfile
                    context: .
```

## Maven 命令速查

| 命令 | 说明 |
|------|------|
| `mvn clean compile` | 清理并编译 |
| `mvn clean package` | 打包 |
| `mvn clean install` | 安装到本地仓库 |
| `mvn test` | 运行单元测试 |
| `mvn verify` | 运行所有检查 |
| `mvn sonar:sonar` | 代码分析 |
| `mvn dependency:tree` | 依赖树 |

## 多模块构建

```bash
# 构建所有模块
mvn clean install -pl module1,module2 -am

# 跳过测试
mvn clean install -DskipTests

# 仅打包
mvn package -DskipTests
```
