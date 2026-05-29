import React from 'react';
import CrudPage, { type FormField } from '@/components/CrudPage';
import { classroomApi } from '@/api';
import { Tag } from 'antd';

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '教室编号', dataIndex: 'roomNo', key: 'roomNo', width: 120 },
  { title: '教室名称', dataIndex: 'roomName', key: 'roomName', width: 140 },
  { title: '所在楼栋', dataIndex: 'building', key: 'building', width: 120 },
  { title: '楼层', dataIndex: 'floor', key: 'floor', width: 80 },
  { title: '容量', dataIndex: 'capacity', key: 'capacity', width: 80 },
  {
    title: '教室类型', dataIndex: 'roomType', key: 'roomType', width: 100,
    render: (t: string) => {
      const m: Record<string, { label: string; color: string }> = {
        LECTURE: { label: '普通教室', color: 'blue' },
        LAB: { label: '实验室', color: 'green' },
        MULTIMEDIA: { label: '多媒体教室', color: 'purple' },
        COMPUTER: { label: '机房', color: 'orange' },
      };
      const item = m[t] || { label: t, color: 'default' };
      return <Tag color={item.color}>{item.label}</Tag>;
    },
  },
  { title: '设备', dataIndex: 'equipment', key: 'equipment', ellipsis: true },
  {
    title: '状态', dataIndex: 'status', key: 'status', width: 80,
    render: (s: string) => <Tag color={s === 'AVAILABLE' ? 'green' : 'red'}>{s === 'AVAILABLE' ? '可用' : '停用'}</Tag>,
  },
];

const formFields: FormField[] = [
  { name: 'roomNo', label: '教室编号', type: 'input', required: true },
  { name: 'roomName', label: '教室名称', type: 'input' },
  { name: 'building', label: '所在楼栋', type: 'input' },
  { name: 'floor', label: '楼层', type: 'number', min: -2, max: 30 },
  { name: 'capacity', label: '容量', type: 'number', min: 1 },
  { name: 'roomType', label: '教室类型', type: 'select', options: [
    { value: 'LECTURE', label: '普通教室' },
    { value: 'LAB', label: '实验室' },
    { value: 'MULTIMEDIA', label: '多媒体教室' },
    { value: 'COMPUTER', label: '机房' },
  ]},
  { name: 'equipment', label: '设备信息', type: 'textarea' },
  { name: 'status', label: '状态', type: 'select', options: [
    { value: 'AVAILABLE', label: '可用' },
    { value: 'DISABLED', label: '停用' },
  ]},
];

const ClassroomManage: React.FC = () => (
  <CrudPage title="教室管理" columns={columns} formFields={formFields}
    fetchData={async () => (await classroomApi.list()).data.data || []}
    onCreate={async (v: any) => { await classroomApi.create(v); }}
    onUpdate={async (v: any) => { await classroomApi.update(v); }}
    onDelete={async (id: number) => { await classroomApi.delete(id); }}
  />
);

export default ClassroomManage;
