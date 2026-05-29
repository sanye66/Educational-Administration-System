import request from '@/utils/request';
import type { R, LoginUser, SysUser, PageResult } from '@/types';

// 认证相关
export const authApi = {
  login: (data: { username: string; password: string }) =>
    request.post<R<LoginUser>>('/auth/login', data),
  register: (data: Record<string, string>) =>
    request.post<R<void>>('/auth/register', data),
  refresh: () =>
    request.post<R<LoginUser>>('/auth/refresh'),
  logout: () =>
    request.post<R<void>>('/auth/logout'),
};

// 用户管理
export const userApi = {
  list: (params?: Record<string, unknown>) =>
    request.get<R<PageResult<SysUser>>>('/user/list', { params }),
  getById: (id: number) =>
    request.get<R<SysUser>>(`/user/${id}`),
  create: (data: SysUser) =>
    request.post<R<void>>('/user', data),
  update: (data: SysUser) =>
    request.put<R<void>>('/user', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/user/${id}`),
};

// 教师管理
export const teacherApi = {
  list: () =>
    request.get<R<any[]>>('/teacher/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/teacher/${id}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/teacher', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/teacher', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/teacher/${id}`),
};

// 学生管理
export const studentApi = {
  list: () =>
    request.get<R<any[]>>('/student/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/student/${id}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/student', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/student', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/student/${id}`),
};

// 课程管理
export const courseApi = {
  list: () =>
    request.get<R<any[]>>('/course/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/course/${id}`),
  getCapacity: (id: number) =>
    request.get<R<{ maxCapacity: number; currentCount: number; remaining: number }>>(`/course/${id}/capacity`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/course', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/course', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/course/${id}`),
};

// 教室管理
export const classroomApi = {
  list: () =>
    request.get<R<any[]>>('/classroom/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/classroom/${id}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/classroom', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/classroom', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/classroom/${id}`),
};

// 选课管理
export const selectionApi = {
  selectCourse: (studentId: number, courseId: number) =>
    request.post<R<string>>('/selection/select', null, { params: { studentId, courseId } }),
  dropCourse: (studentId: number, courseId: number) =>
    request.post<R<string>>('/selection/drop', null, { params: { studentId, courseId } }),
  getStudentSelections: (studentId: number) =>
    request.get<R<any[]>>(`/selection/student/${studentId}`),
  getCourseSelections: (courseId: number) =>
    request.get<R<any[]>>(`/selection/course/${courseId}`),
  list: () =>
    request.get<R<any[]>>('/selection/list'),
};

// 成绩管理
export const gradeApi = {
  list: () =>
    request.get<R<any[]>>('/grade/list'),
  getStudentGrades: (studentId: number) =>
    request.get<R<any[]>>(`/grade/student/${studentId}`),
  getCourseGrades: (courseId: number) =>
    request.get<R<any[]>>(`/grade/course/${courseId}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/grade', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/grade', data),
};

// 考试管理
export const examApi = {
  list: () =>
    request.get<R<any[]>>('/exam/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/exam/${id}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/exam', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/exam', data),
  delete: (id: number) =>
    request.delete<R<void>>(`/exam/${id}`),
};

// 排课管理
export const scheduleApi = {
  generate: (academicYear: string, semester: number) =>
    request.post<R<string>>('/schedule/generate', null, { params: { academicYear, semester } }),
  list: () =>
    request.get<R<any[]>>('/schedule/list'),
  getTeacherSchedule: (teacherId: number) =>
    request.get<R<any[]>>(`/schedule/teacher/${teacherId}`),
  getClassroomSchedule: (classroomId: number) =>
    request.get<R<any[]>>(`/schedule/classroom/${classroomId}`),
  delete: (id: number) =>
    request.delete<R<void>>(`/schedule/${id}`),
};

// 毕设管理
export const graduationApi = {
  list: () =>
    request.get<R<any[]>>('/graduation/list'),
  getById: (id: number) =>
    request.get<R<any>>(`/graduation/${id}`),
  create: (data: Record<string, unknown>) =>
    request.post<R<void>>('/graduation', data),
  update: (data: Record<string, unknown>) =>
    request.put<R<void>>('/graduation', data),
  selectTopic: (studentId: number, topicId: number) =>
    request.post<R<string>>('/graduation/select', null, { params: { studentId, topicId } }),
};

// 评价管理
export const evaluationApi = {
  list: () =>
    request.get<R<any[]>>('/evaluation/list'),
  getStudentEvaluations: (studentId: number) =>
    request.get<R<any[]>>(`/evaluation/student/${studentId}`),
  getTeacherEvaluations: (teacherId: number) =>
    request.get<R<any[]>>(`/evaluation/teacher/${teacherId}`),
  submit: (data: Record<string, unknown>) =>
    request.post<R<void>>('/evaluation', data),
};

// 监控管理
export const monitorApi = {
  getServices: () =>
    request.get<R<any[]>>('http://localhost:8093/api/services'),
  getInfrastructure: () =>
    request.get<R<any[]>>('http://localhost:8093/api/infrastructure'),
};
