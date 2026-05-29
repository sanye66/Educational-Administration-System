import React from 'react';
import CrudPage, { type FormField } from '@/components/CrudPage';
import { teacherApi } from '@/api';
import { Tag } from 'antd';

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '工号', dataIndex: 'teacherNo', key: 'teacherNo' },
  { title: '关联用户ID', dataIndex: 'userId', key: 'userId' },
  { title: '院系ID', dataIndex: 'departmentId', key: 'departmentId' },
  {
    title: '职称', dataIndex: 'title', key: 'title',
    render: (t: string) => {
      const m: Record<string, string> = { PROFESSOR: '教授', ASSOCIATE_PROFESSOR: '副教授', LECTURER: '讲师', ASSISTANT: '助教' };
      return <Tag color="blue">{m[t] || t}</Tag>;
    }
  },
  { title: '学历', dataIndex: 'education', key: 'education' },
  { title: '研究方向', dataIndex: 'researchDirection', key: 'researchDirection', ellipsis: true },
];

const formFields: FormField[] = [
  { name: 'userId', label: '关联用户ID', type: 'number', required: true },
  { name: 'teacherNo', label: '教师工号', type: 'input', required: true },
  { name: 'departmentId', label: '院系ID', type: 'number' },
  { name: 'title', label: '职称', type: 'select', options: [
    { value: 'PROFESSOR', label: '教授' }, { value: 'ASSOCIATE_PROFESSOR', label: '副教授' },
    { value: 'LECTURER', label: '讲师' }, { value: 'ASSISTANT', label: '助教' },
  ]},
  { name: 'education', label: '学历', type: 'select', options: [
    { value: 'DOCTOR', label: '博士' }, { value: 'MASTER', label: '硕士' }, { value: 'BACHELOR', label: '本科' },
  ]},
  { name: 'researchDirection', label: '研究方向', type: 'textarea' },
];

const TeacherManage: React.FC = () => (
  <CrudPage title="教师管理" columns={columns} formFields={formFields}
    fetchData={async () => (await teacherApi.list()).data.data || []}
    onCreate={async (v: any) => { await teacherApi.create(v); }}
    onUpdate={async (v: any) => { await teacherApi.update(v); }}
    onDelete={async (id: number) => { await teacherApi.delete(id); }}
  />
);
export default TeacherManage;
