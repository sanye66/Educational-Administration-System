import React from 'react';
import CrudPage, { type FormField } from '@/components/CrudPage';
import { examApi } from '@/api';
import { Tag } from 'antd';

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '课程ID', dataIndex: 'courseId', key: 'courseId', width: 100 },
  { title: '教室ID', dataIndex: 'classroomId', key: 'classroomId', width: 100 },
  { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
  {
    title: '学期', dataIndex: 'semester', key: 'semester', width: 80,
    render: (v: number) => <Tag>{v === 1 ? '第一学期' : '第二学期'}</Tag>,
  },
  { title: '考试时间', dataIndex: 'examTime', key: 'examTime', width: 180 },
  { title: '时长(分钟)', dataIndex: 'duration', key: 'duration', width: 100 },
  {
    title: '考试类型', dataIndex: 'examType', key: 'examType', width: 100,
    render: (t: string) => {
      const m: Record<string, { label: string; color: string }> = {
        FINAL: { label: '期末考试', color: 'red' },
        MIDTERM: { label: '期中考试', color: 'orange' },
        MAKEUP: { label: '补考', color: 'blue' },
      };
      const item = m[t] || { label: t, color: 'default' };
      return <Tag color={item.color}>{item.label}</Tag>;
    },
  },
  {
    title: '状态', dataIndex: 'status', key: 'status', width: 100,
    render: (s: string) => {
      const m: Record<string, { label: string; color: string }> = {
        SCHEDULED: { label: '已安排', color: 'blue' },
        IN_PROGRESS: { label: '进行中', color: 'green' },
        COMPLETED: { label: '已完成', color: 'default' },
        CANCELLED: { label: '已取消', color: 'red' },
      };
      const item = m[s] || { label: s, color: 'default' };
      return <Tag color={item.color}>{item.label}</Tag>;
    },
  },
];

const formFields: FormField[] = [
  { name: 'courseId', label: '课程ID', type: 'number', required: true },
  { name: 'classroomId', label: '教室ID', type: 'number' },
  { name: 'academicYear', label: '学年', type: 'input' },
  { name: 'semester', label: '学期', type: 'select', options: [
    { value: '1', label: '第一学期' },
    { value: '2', label: '第二学期' },
  ]},
  { name: 'examTime', label: '考试时间', type: 'input' },
  { name: 'duration', label: '时长(分钟)', type: 'number', min: 30, max: 300 },
  { name: 'examType', label: '考试类型', type: 'select', options: [
    { value: 'FINAL', label: '期末考试' },
    { value: 'MIDTERM', label: '期中考试' },
    { value: 'MAKEUP', label: '补考' },
  ]},
  { name: 'status', label: '状态', type: 'select', options: [
    { value: 'SCHEDULED', label: '已安排' },
    { value: 'IN_PROGRESS', label: '进行中' },
    { value: 'COMPLETED', label: '已完成' },
    { value: 'CANCELLED', label: '已取消' },
  ]},
];

const ExamManage: React.FC = () => (
  <CrudPage title="考试管理" columns={columns} formFields={formFields}
    fetchData={async () => (await examApi.list()).data.data || []}
    onCreate={async (v: any) => { await examApi.create(v); }}
    onUpdate={async (v: any) => { await examApi.update(v); }}
    onDelete={async (id: number) => { await examApi.delete(id); }}
  />
);

export default ExamManage;
