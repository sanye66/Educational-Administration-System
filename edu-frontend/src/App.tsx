import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import MainLayout from '@/layouts/MainLayout';
import Login from '@/pages/Login';
import { useAuthStore } from '@/store/auth';

// 懒加载页面组件
import Dashboard from '@/pages/Dashboard';
import UserManage from '@/pages/system/UserManage';
import TeacherManage from '@/pages/academic/TeacherManage';
import StudentManage from '@/pages/academic/StudentManage';
import CourseManage from '@/pages/academic/CourseManage';
import ClassroomManage from '@/pages/academic/ClassroomManage';
import SelectionManage from '@/pages/academic/SelectionManage';
import GradeManage from '@/pages/academic/GradeManage';
import ExamManage from '@/pages/academic/ExamManage';
import ScheduleManage from '@/pages/academic/ScheduleManage';
import GraduationManage from '@/pages/academic/GraduationManage';
import EvaluationManage from '@/pages/academic/EvaluationManage';
import MonitorServices from '@/pages/monitor/MonitorServices';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { token } = useAuthStore();
  if (!token) return <Navigate to="/login" replace />;
  return <>{children}</>;
};

const App: React.FC = () => (
  <ConfigProvider locale={zhCN} theme={{
    token: {
      colorPrimary: '#1890ff',
      borderRadius: 6,
    },
  }}>
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }>
          <Route index element={<Dashboard />} />
          <Route path="system/user" element={<UserManage />} />
          <Route path="academic/teacher" element={<TeacherManage />} />
          <Route path="academic/student" element={<StudentManage />} />
          <Route path="academic/course" element={<CourseManage />} />
          <Route path="academic/classroom" element={<ClassroomManage />} />
          <Route path="academic/selection" element={<SelectionManage />} />
          <Route path="academic/grade" element={<GradeManage />} />
          <Route path="academic/exam" element={<ExamManage />} />
          <Route path="academic/schedule" element={<ScheduleManage />} />
          <Route path="academic/graduation" element={<GraduationManage />} />
          <Route path="academic/evaluation" element={<EvaluationManage />} />
          <Route path="monitor/services" element={<MonitorServices />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  </ConfigProvider>
);

export default App;
