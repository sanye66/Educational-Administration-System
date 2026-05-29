-- 教学管理系统数据库脚本
-- 数据库: edu_user

CREATE DATABASE IF NOT EXISTS edu_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_teacher DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_student DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_course DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_classroom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_selection DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_grade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_exam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_schedule DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_graduation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS edu_evaluation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ===================== edu_user =====================
USE edu_user;

CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(100) NOT NULL COMMENT '密码',
    real_name   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    user_type   VARCHAR(20)  NOT NULL COMMENT '用户类型: ADMIN/TEACHER/STUDENT',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    avatar      VARCHAR(200) DEFAULT NULL COMMENT '头像',
    gender      TINYINT      DEFAULT 2 COMMENT '性别: 0-男 1-女 2-未知',
    status      TINYINT      DEFAULT 0 COMMENT '状态: 0-正常 1-禁用 2-锁定',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 初始化管理员账号 (密码: 123456 的BCrypt加密)
INSERT INTO sys_user (username, password, real_name, user_type, status) VALUES
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 'ADMIN', 0);

-- ===================== edu_teacher =====================
USE edu_teacher;

CREATE TABLE IF NOT EXISTS teacher (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    user_id            BIGINT       NOT NULL COMMENT '关联用户ID',
    teacher_no         VARCHAR(30)  NOT NULL COMMENT '教师工号',
    department_id      BIGINT       DEFAULT NULL COMMENT '院系ID',
    title              VARCHAR(30)  DEFAULT NULL COMMENT '职称',
    education          VARCHAR(20)  DEFAULT NULL COMMENT '学历',
    research_direction VARCHAR(200) DEFAULT NULL COMMENT '研究方向',
    entry_date         DATE         DEFAULT NULL COMMENT '入职日期',
    create_by          BIGINT       DEFAULT NULL,
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by          BIGINT       DEFAULT NULL,
    update_time        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted            TINYINT      DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_teacher_no (teacher_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- ===================== edu_student =====================
USE edu_student;

CREATE TABLE IF NOT EXISTS student (
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    user_id          BIGINT      NOT NULL COMMENT '关联用户ID',
    student_no       VARCHAR(30) NOT NULL COMMENT '学号',
    department_id    BIGINT      DEFAULT NULL COMMENT '院系ID',
    major_id         BIGINT      DEFAULT NULL COMMENT '专业ID',
    class_id         BIGINT      DEFAULT NULL COMMENT '班级ID',
    enrollment_year  VARCHAR(10) DEFAULT NULL COMMENT '入学年份',
    schooling_length INT         DEFAULT 4 COMMENT '学制年限',
    status           VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '学籍状态',
    create_by        BIGINT      DEFAULT NULL,
    create_time      DATETIME    DEFAULT CURRENT_TIMESTAMP,
    update_by        BIGINT      DEFAULT NULL,
    update_time      DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted          TINYINT     DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_no (student_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ===================== edu_course =====================
USE edu_course;

CREATE TABLE IF NOT EXISTS course (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    course_no     VARCHAR(30)  NOT NULL COMMENT '课程编号',
    course_name   VARCHAR(100) NOT NULL COMMENT '课程名称',
    category_id   BIGINT       DEFAULT NULL COMMENT '分类ID',
    teacher_id    BIGINT       DEFAULT NULL COMMENT '授课教师ID',
    credit        DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    class_hour    INT          DEFAULT NULL COMMENT '学时',
    course_type   VARCHAR(20)  DEFAULT NULL COMMENT '课程类型',
    max_capacity  INT          DEFAULT 0 COMMENT '最大容量',
    current_count INT          DEFAULT 0 COMMENT '当前选课人数',
    academic_year VARCHAR(10)  DEFAULT NULL COMMENT '学年',
    semester      INT          DEFAULT NULL COMMENT '学期',
    description   TEXT         DEFAULT NULL COMMENT '课程描述',
    status        VARCHAR(20)  DEFAULT 'DRAFT' COMMENT '状态',
    create_by     BIGINT       DEFAULT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by     BIGINT       DEFAULT NULL,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT      DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_no (course_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ===================== edu_classroom =====================
USE edu_classroom;

CREATE TABLE IF NOT EXISTS classroom (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    room_no    VARCHAR(30) NOT NULL COMMENT '教室编号',
    room_name  VARCHAR(50) DEFAULT NULL COMMENT '教室名称',
    building   VARCHAR(50) DEFAULT NULL COMMENT '所属建筑',
    floor      INT         DEFAULT NULL COMMENT '楼层',
    capacity   INT         DEFAULT NULL COMMENT '容量',
    room_type  VARCHAR(20) DEFAULT NULL COMMENT '教室类型',
    equipment  TEXT        DEFAULT NULL COMMENT '设备描述',
    status     VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态',
    create_by  BIGINT      DEFAULT NULL,
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP,
    update_by  BIGINT      DEFAULT NULL,
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted    TINYINT     DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_room_no (room_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室表';

-- ===================== edu_selection =====================
USE edu_selection;

CREATE TABLE IF NOT EXISTS selection_record (
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    student_id    BIGINT      NOT NULL COMMENT '学生ID',
    course_id     BIGINT      NOT NULL COMMENT '课程ID',
    task_id       BIGINT      DEFAULT NULL COMMENT '选课任务ID',
    academic_year VARCHAR(10) DEFAULT NULL COMMENT '学年',
    semester      INT         DEFAULT NULL COMMENT '学期',
    status        VARCHAR(20) DEFAULT 'SELECTED' COMMENT '状态',
    create_by     BIGINT      DEFAULT NULL,
    create_time   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    update_by     BIGINT      DEFAULT NULL,
    update_time   DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT     DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_course_id (course_id),
    UNIQUE KEY uk_student_course (student_id, course_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- ===================== edu_grade =====================
USE edu_grade;

CREATE TABLE IF NOT EXISTS grade (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    student_id    BIGINT       NOT NULL COMMENT '学生ID',
    course_id     BIGINT       NOT NULL COMMENT '课程ID',
    teacher_id    BIGINT       DEFAULT NULL COMMENT '教师ID',
    academic_year VARCHAR(10)  DEFAULT NULL,
    semester      INT          DEFAULT NULL,
    regular_score DECIMAL(5,2) DEFAULT NULL COMMENT '平时成绩',
    midterm_score DECIMAL(5,2) DEFAULT NULL COMMENT '期中成绩',
    final_score   DECIMAL(5,2) DEFAULT NULL COMMENT '期末成绩',
    total_score   DECIMAL(5,2) DEFAULT NULL COMMENT '总评成绩',
    gpa           DECIMAL(3,2) DEFAULT NULL COMMENT '绩点',
    status        VARCHAR(20)  DEFAULT 'DRAFT' COMMENT '状态',
    create_by     BIGINT       DEFAULT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by     BIGINT       DEFAULT NULL,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT      DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- ===================== edu_exam =====================
USE edu_exam;

CREATE TABLE IF NOT EXISTS exam_plan (
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    course_id     BIGINT      NOT NULL COMMENT '课程ID',
    classroom_id  BIGINT      DEFAULT NULL COMMENT '考场教室ID',
    academic_year VARCHAR(10) DEFAULT NULL,
    semester      INT         DEFAULT NULL,
    exam_time     DATETIME    DEFAULT NULL COMMENT '考试时间',
    duration      INT         DEFAULT NULL COMMENT '时长(分钟)',
    exam_type     VARCHAR(20) DEFAULT NULL COMMENT '考试类型',
    status        VARCHAR(20) DEFAULT 'SCHEDULED' COMMENT '状态',
    create_by     BIGINT      DEFAULT NULL,
    create_time   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    update_by     BIGINT      DEFAULT NULL,
    update_time   DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT     DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试安排表';

-- ===================== edu_schedule =====================
USE edu_schedule;

CREATE TABLE IF NOT EXISTS schedule_result (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    course_id       BIGINT      NOT NULL COMMENT '课程ID',
    teacher_id      BIGINT      DEFAULT NULL COMMENT '教师ID',
    classroom_id    BIGINT      DEFAULT NULL COMMENT '教室ID',
    academic_year   VARCHAR(10) DEFAULT NULL,
    semester        INT         DEFAULT NULL,
    day_of_week     INT         DEFAULT NULL COMMENT '星期几',
    start_section   INT         DEFAULT NULL COMMENT '开始节次',
    duration_section INT        DEFAULT NULL COMMENT '持续节次',
    status          VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
    create_by       BIGINT      DEFAULT NULL,
    create_time     DATETIME    DEFAULT CURRENT_TIMESTAMP,
    update_by       BIGINT      DEFAULT NULL,
    update_time     DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT     DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排课结果表';

-- ===================== edu_graduation =====================
USE edu_graduation;

CREATE TABLE IF NOT EXISTS graduation_topic (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(200) NOT NULL COMMENT '毕设题目',
    description   TEXT         DEFAULT NULL COMMENT '描述',
    teacher_id    BIGINT       DEFAULT NULL COMMENT '指导教师ID',
    student_id    BIGINT       DEFAULT NULL COMMENT '学生ID',
    academic_year VARCHAR(10)  DEFAULT NULL,
    difficulty    VARCHAR(20)  DEFAULT 'MEDIUM' COMMENT '难度',
    status        VARCHAR(20)  DEFAULT 'AVAILABLE' COMMENT '状态',
    create_by     BIGINT       DEFAULT NULL,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by     BIGINT       DEFAULT NULL,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT      DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='毕设题目表';

-- ===================== edu_evaluation =====================
USE edu_evaluation;

CREATE TABLE IF NOT EXISTS evaluation_record (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    student_id     BIGINT       NOT NULL COMMENT '学生ID',
    course_id      BIGINT       NOT NULL COMMENT '课程ID',
    teacher_id     BIGINT       DEFAULT NULL COMMENT '教师ID',
    academic_year  VARCHAR(10)  DEFAULT NULL,
    semester       INT          DEFAULT NULL,
    attitude_score DECIMAL(3,2) DEFAULT NULL COMMENT '教学态度',
    content_score  DECIMAL(3,2) DEFAULT NULL COMMENT '教学内容',
    method_score   DECIMAL(3,2) DEFAULT NULL COMMENT '教学方法',
    effect_score   DECIMAL(3,2) DEFAULT NULL COMMENT '教学效果',
    total_score    DECIMAL(3,2) DEFAULT NULL COMMENT '总评',
    comment        TEXT         DEFAULT NULL COMMENT '文字评价',
    status         VARCHAR(20)  DEFAULT 'DRAFT' COMMENT '状态',
    create_by      BIGINT       DEFAULT NULL,
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by      BIGINT       DEFAULT NULL,
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted        TINYINT      DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学评价表';

