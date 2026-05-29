// 统一响应类型
export interface R<T = unknown> {
  code: number;
  msg: string;
  data: T;
}

// 分页请求
export interface PageRequest {
  pageNum: number;
  pageSize: number;
  orderBy?: string;
  orderDir?: 'asc' | 'desc';
}

// 分页响应
export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

// 登录用户
export interface LoginUser {
  userId: number;
  username: string;
  realName: string;
  userType: 'ADMIN' | 'TEACHER' | 'STUDENT';
  roleIds: number[];
  permissions: string[];
  token: string;
  loginTime: number;
  expireTime: number;
}

// 用户
export interface SysUser {
  id?: number;
  username: string;
  password?: string;
  realName: string;
  userType: string;
  email?: string;
  phone?: string;
  avatar?: string;
  gender?: number;
  status?: number;
  createTime?: string;
  updateTime?: string;
}

// 教师
export interface Teacher {
  id?: number;
  userId: number;
  teacherNo: string;
  departmentId?: number;
  title?: string;
  education?: string;
  researchDirection?: string;
  entryDate?: string;
  createTime?: string;
}

// 学生
export interface Student {
  id?: number;
  userId: number;
  studentNo: string;
  departmentId?: number;
  majorId?: number;
  classId?: number;
  enrollmentYear?: string;
  schoolingLength?: number;
  status?: string;
  createTime?: string;
}

// 课程
export interface Course {
  id?: number;
  courseNo: string;
  courseName: string;
  categoryId?: number;
  teacherId?: number;
  credit?: number;
  classHour?: number;
  courseType?: string;
  maxCapacity?: number;
  currentCount?: number;
  academicYear?: string;
  semester?: number;
  description?: string;
  status?: string;
  createTime?: string;
}

// 教室
export interface Classroom {
  id?: number;
  roomNo: string;
  roomName?: string;
  building?: string;
  floor?: number;
  capacity?: number;
  roomType?: string;
  equipment?: string;
  status?: string;
  createTime?: string;
}

// 选课记录
export interface SelectionRecord {
  id?: number;
  studentId: number;
  courseId: number;
  taskId?: number;
  academicYear?: string;
  semester?: number;
  status?: string;
  createTime?: string;
}

// 成绩
export interface Grade {
  id?: number;
  studentId: number;
  courseId: number;
  teacherId?: number;
  academicYear?: string;
  semester?: number;
  regularScore?: number;
  midtermScore?: number;
  finalScore?: number;
  totalScore?: number;
  gpa?: number;
  status?: string;
  createTime?: string;
}

// 考试安排
export interface ExamPlan {
  id?: number;
  courseId: number;
  classroomId?: number;
  academicYear?: string;
  semester?: number;
  examTime?: string;
  duration?: number;
  examType?: string;
  status?: string;
  createTime?: string;
}

// 排课结果
export interface ScheduleResult {
  id?: number;
  courseId: number;
  teacherId?: number;
  classroomId?: number;
  academicYear?: string;
  semester?: number;
  dayOfWeek?: number;
  startSection?: number;
  durationSection?: number;
  status?: string;
  createTime?: string;
}

// 毕设题目
export interface GraduationTopic {
  id?: number;
  title: string;
  description?: string;
  teacherId?: number;
  studentId?: number;
  academicYear?: string;
  difficulty?: string;
  status?: string;
  createTime?: string;
}

// 评价记录
export interface EvaluationRecord {
  id?: number;
  studentId: number;
  courseId: number;
  teacherId?: number;
  academicYear?: string;
  semester?: number;
  attitudeScore?: number;
  contentScore?: number;
  methodScore?: number;
  effectScore?: number;
  totalScore?: number;
  comment?: string;
  status?: string;
  createTime?: string;
}

// 监控指标
export interface MonitorMetrics {
  serviceName: string;
  status: 'UP' | 'DOWN' | 'UNKNOWN';
  cpuUsage?: number;
  memoryUsage?: number;
  qps?: number;
  responseTime?: number;
  errorRate?: number;
}
