# API 接口文档

本文档提供教学管理系统的 API 接口说明。

## 1. 接口规范

### 1.1 基础信息

| 项目 | 说明 |
|------|------|
| 基础URL | http://localhost:8080 |
| 协议 | HTTP/HTTPS |
| 数据格式 | JSON |
| 编码 | UTF-8 |
| 认证方式 | Bearer Token |

### 1.2 统一响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": 1704067200000
}
```

### 1.3 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 1000 | 系统异常 |
| 2000 | 未授权 |
| 3000 | 参数错误 |
| 4000 | 业务错误 |
| 5000 | 第三方服务错误 |

## 2. 认证接口

### 2.1 用户登录

```
POST /api/auth/login
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin",
    "userType": "ADMIN"
  }
}
```

### 2.2 用户登出

```
POST /api/auth/logout
```

**请求头**：

| 参数名 | 说明 |
|--------|------|
| Authorization | Bearer Token |

## 3. 用户服务 (edu-user)

### 3.1 获取用户信息

```
GET /api/user/{id}
```

**响应示例**：

```json
{
  "code": 0,
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "userType": "ADMIN"
  }
}
```

### 3.2 新增用户

```
POST /api/user
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| realName | string | 是 | 真实姓名 |
| userType | string | 是 | 用户类型 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |

## 4. 学生服务 (edu-student)

### 4.1 获取学生列表

```
GET /api/student/list
```

**查询参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentNo | string | 否 | 学号 |
| name | string | 否 | 姓名 |
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认10 |

### 4.2 获取学生详情

```
GET /api/student/{id}
```

### 4.3 新增学生

```
POST /api/student
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | long | 是 | 关联用户ID |
| studentNo | string | 是 | 学号 |
| departmentId | long | 否 | 院系ID |
| majorId | long | 否 | 专业ID |
| classId | long | 否 | 班级ID |
| enrollmentYear | string | 否 | 入学年份 |

## 5. 教师服务 (edu-teacher)

### 5.1 获取教师列表

```
GET /api/teacher/list
```

### 5.2 获取教师详情

```
GET /api/teacher/{id}
```

## 6. 课程服务 (edu-course)

### 6.1 获取课程列表

```
GET /api/course/list
```

**查询参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| courseName | string | 课程名称 |
| teacherId | long | 教师ID |
| status | string | 课程状态 |

### 6.2 获取课程详情

```
GET /api/course/{id}
```

### 6.3 新增课程

```
POST /api/course
```

## 7. 选课服务 (edu-selection)

### 7.1 学生选课

```
POST /api/selection/select
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | long | 是 | 学生ID |
| courseId | long | 是 | 课程ID |

### 7.2 学生退课

```
POST /api/selection/drop
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | long | 是 | 学生ID |
| courseId | long | 是 | 课程ID |

### 7.3 获取选课列表

```
GET /api/selection/list
```

**查询参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| studentId | long | 学生ID |
| courseId | long | 课程ID |

## 8. 成绩服务 (edu-grade)

### 8.1 录入成绩

```
POST /api/grade
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | long | 是 | 学生ID |
| courseId | long | 是 | 课程ID |
| regularScore | decimal | 否 | 平时成绩 |
| midtermScore | decimal | 否 | 期中成绩 |
| finalScore | decimal | 否 | 期末成绩 |

### 8.2 查询成绩

```
GET /api/grade/list
```

**查询参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| studentId | long | 学生ID |
| courseId | long | 课程ID |

## 9. 排课服务 (edu-schedule)

### 9.1 获取课程表

```
GET /api/schedule/list
```

**查询参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| teacherId | long | 教师ID |
| studentId | long | 学生ID |
| classroomId | long | 教室ID |

## 10. 考试服务 (edu-exam)

### 10.1 获取考试安排

```
GET /api/exam/list
```

### 10.2 新增考试安排

```
POST /api/exam
```

## 11. 教学评价服务 (edu-evaluation)

### 11.1 提交评价

```
POST /api/evaluation
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | long | 是 | 学生ID |
| courseId | long | 是 | 课程ID |
| teacherId | long | 是 | 教师ID |
| attitudeScore | decimal | 是 | 教学态度评分 |
| contentScore | decimal | 是 | 教学内容评分 |
| methodScore | decimal | 是 | 教学方法评分 |
| effectScore | decimal | 是 | 教学效果评分 |

## 12. 毕业设计服务 (edu-graduation)

### 12.1 获取毕设题目

```
GET /api/graduation/topic/list
```

### 12.2 选题

```
POST /api/graduation/select
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| topicId | long | 是 | 题目ID |
| studentId | long | 是 | 学生ID |
