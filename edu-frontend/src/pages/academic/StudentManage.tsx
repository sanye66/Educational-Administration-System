import React from 'react';
import CrudPage, { type FormField } from '@/components/CrudPage';
import { studentApi } from '@/api';
import { Tag } from 'antd';

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '学号', dataIndex: 'studentNo', key: 'studentNo' },
  { title: '关联用户ID', dataIndex: 'userId', key: 'userId' },
  { title: '院系ID', dataIndex: 'departmentId', key: 'departmentId' },
  { title: '专业ID', dataIndex: 'majorId', key: 'majorId' },
  { title: '班级ID', dataIndex: 'classId', key: 'classId' },
  { title: '入学年份', dataIndex: 'enrollmentYear', key: 'enrollmentYear' },
  { title: '状态', dataIndex: 'status', key: 'status', render: (s: string) => <Tag color={s === 'ACTIVE' ? 'green' : 'red'}>{s}</Tag> },
];
const formFields: FormField[] = [
  { name: 'userId', label: '关联用户ID', type: 'number', required: true },
  { name: 'studentNo', label: '学号', type: 'input', required: true },
  { name: 'departmentId', label: '院系ID', type: 'number' },
  { name: 'majorId', label: '专业ID', type: 'number' },
  { name: 'classId', label: '班级ID', type: 'number' },
  { name: 'enrollmentYear', label: '入学年份', type: 'input' },
  { name: 'schoolingLength', label: '学制年限', type: 'number', min: 2, max: 8 },
];
const StudentManage: React.FC = () => (
  <CrudPage title="学生管理" columns={columns} formFields={formFields}
    fetchData={async () => (await studentApi.list()).data.data || []}
    onCreate={async (v: any) => { await studentApi.create(v); }}
    onUpdate={async (v: any) => { await studentApi.update(v); }}
    onDelete={async (id: number) => { await studentApi.delete(id); }}
  />
);
export default StudentManage;
