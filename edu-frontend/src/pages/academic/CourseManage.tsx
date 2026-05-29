import React from 'react';
import CrudPage, { type FormField } from '@/components/CrudPage';
import { courseApi } from '@/api';
import { Tag, Button, Modal, Descriptions, message } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import { useState } from 'react';

const courseColumns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '课程号', dataIndex: 'courseNo', key: 'courseNo', width: 120 },
  { title: '课程名称', dataIndex: 'courseName', key: 'courseName', width: 160 },
  { title: '授课教师ID', dataIndex: 'teacherId', key: 'teacherId', width: 100 },
  {
    title: '学分', dataIndex: 'credit', key: 'credit', width: 80,
    render: (v: number) => <Tag color="blue">{v}</Tag>,
  },
  { title: '学时', dataIndex: 'classHour', key: 'classHour', width: 80 },
  {
    title: '课程类型', dataIndex: 'courseType', key: 'courseType', width: 100,
    render: (t: string) => {
      const m: Record<string, { label: string; color: string }> = {
        REQUIRED: { label: '必修', color: 'red' },
        ELECTIVE: { label: '选修', color: 'green' },
        GENERAL: { label: '公选', color: 'blue' },
      };
      const item = m[t] || { label: t, color: 'default' };
      return <Tag color={item.color}>{item.label}</Tag>;
    },
  },
  {
    title: '容量', dataIndex: 'maxCapacity', key: 'maxCapacity', width: 100,
    render: (_: number, record: any) => `${record.currentCount || 0}/${record.maxCapacity || 0}`,
  },
  { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
  {
    title: '学期', dataIndex: 'semester', key: 'semester', width: 80,
    render: (v: number) => <Tag>{v === 1 ? '第一学期' : '第二学期'}</Tag>,
  },
  {
    title: '状态', dataIndex: 'status', key: 'status', width: 80,
    render: (s: string) => <Tag color={s === 'ACTIVE' ? 'green' : 'default'}>{s === 'ACTIVE' ? '开课中' : '已停开'}</Tag>,
  },
];

const formFields: FormField[] = [
  { name: 'courseNo', label: '课程号', type: 'input', required: true },
  { name: 'courseName', label: '课程名称', type: 'input', required: true },
  { name: 'teacherId', label: '授课教师ID', type: 'number' },
  { name: 'credit', label: '学分', type: 'number', min: 0.5, max: 10 },
  { name: 'classHour', label: '学时', type: 'number', min: 8, max: 120 },
  { name: 'courseType', label: '课程类型', type: 'select', options: [
    { value: 'REQUIRED', label: '必修' },
    { value: 'ELECTIVE', label: '选修' },
    { value: 'GENERAL', label: '公选' },
  ]},
  { name: 'maxCapacity', label: '最大容量', type: 'number', min: 1 },
  { name: 'academicYear', label: '学年', type: 'input' },
  { name: 'semester', label: '学期', type: 'select', options: [
    { value: '1', label: '第一学期' },
    { value: '2', label: '第二学期' },
  ]},
  { name: 'description', label: '课程描述', type: 'textarea' },
  { name: 'status', label: '状态', type: 'select', options: [
    { value: 'ACTIVE', label: '开课中' },
    { value: 'INACTIVE', label: '已停开' },
  ]},
];

const CourseManage: React.FC = () => {
  const [capModal, setCapModal] = useState(false);
  const [capData, setCapData] = useState<{ maxCapacity: number; currentCount: number; remaining: number } | null>(null);
  const [capLoading, setCapLoading] = useState(false);

  const showCapacity = async (id: number) => {
    setCapLoading(true);
    setCapModal(true);
    try {
      const res = await courseApi.getCapacity(id);
      setCapData(res.data.data);
    } catch {
      setCapData(null);
    }
    setCapLoading(false);
  };

  return (
    <>
      <CrudPage title="课程管理" columns={courseColumns} formFields={formFields}
        fetchData={async () => (await courseApi.list()).data.data || []}
        onCreate={async (v: any) => { await courseApi.create(v); }}
        onUpdate={async (v: any) => { await courseApi.update(v); }}
        onDelete={async (id: number) => { await courseApi.delete(id); }}
        extraActions={(record: any) => (
          <Button size="small" icon={<EyeOutlined />} onClick={() => showCapacity(record.id)}>
            容量
          </Button>
        )}
      />
      <Modal title="课程容量信息" open={capModal} onCancel={() => setCapModal(false)} footer={null} loading={capLoading}>
        {capData && (
          <Descriptions column={1} bordered size="small">
            <Descriptions.Item label="最大容量">{capData.maxCapacity}</Descriptions.Item>
            <Descriptions.Item label="已选人数">{capData.currentCount}</Descriptions.Item>
            <Descriptions.Item label="剩余容量">
              <Tag color={capData.remaining > 0 ? 'green' : 'red'}>{capData.remaining}</Tag>
            </Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </>
  );
};

export default CourseManage;
