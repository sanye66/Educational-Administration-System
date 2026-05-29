import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Space, Tag, Modal, Form, Input, InputNumber, message, Popconfirm } from 'antd';
import { ReloadOutlined, ThunderboltOutlined, DeleteOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';
import { scheduleApi } from '@/api';

const ScheduleManage: React.FC = () => {
  const [schedules, setSchedules] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [genModal, setGenModal] = useState(false);
  const [genForm] = Form.useForm();

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await scheduleApi.list();
      setSchedules(res.data.data || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { refresh(); }, []);

  const handleGenerate = async () => {
    const values = await genForm.validateFields();
    try {
      const res = await scheduleApi.generate(values.academicYear, values.semester);
      message.success(res.data.data || '排课生成成功');
      setGenModal(false);
      genForm.resetFields();
      refresh();
    } catch { /* handled */ }
  };

  const handleDelete = async (id: number) => {
    try {
      await scheduleApi.delete(id);
      message.success('删除成功');
      refresh();
    } catch { /* handled */ }
  };

  const dayMap: Record<number, string> = { 1: '周一', 2: '周二', 3: '周三', 4: '周四', 5: '周五', 6: '周六', 7: '周日' };

  const columns: ColumnType<any>[] = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '课程ID', dataIndex: 'courseId', key: 'courseId', width: 100 },
    { title: '教师ID', dataIndex: 'teacherId', key: 'teacherId', width: 100 },
    { title: '教室ID', dataIndex: 'classroomId', key: 'classroomId', width: 100 },
    { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
    { title: '学期', dataIndex: 'semester', key: 'semester', width: 80 },
    {
      title: '星期', dataIndex: 'dayOfWeek', key: 'dayOfWeek', width: 80,
      render: (v: number) => <Tag color="blue">{dayMap[v] || v}</Tag>,
    },
    { title: '起始节次', dataIndex: 'startSection', key: 'startSection', width: 90 },
    { title: '持续节次', dataIndex: 'durationSection', key: 'durationSection', width: 90 },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 100,
      render: (s: string) => {
        const m: Record<string, { label: string; color: string }> = {
          ACTIVE: { label: '生效中', color: 'green' },
          CANCELLED: { label: '已取消', color: 'red' },
        };
        const item = m[s] || { label: s, color: 'default' };
        return <Tag color={item.color}>{item.label}</Tag>;
      },
    },
    {
      title: '操作', key: 'action', width: 80, fixed: 'right' as const,
      render: (_: any, record: any) => (
        <Popconfirm title="确定删除此排课记录?" onConfirm={() => handleDelete(record.id)}>
          <Button size="small" danger icon={<DeleteOutlined />}>删除</Button>
        </Popconfirm>
      ),
    },
  ];

  return (
    <div>
      <Card title="排课管理">
        <Space style={{ marginBottom: 16 }}>
          <Button type="primary" icon={<ThunderboltOutlined />} onClick={() => setGenModal(true)}>智能排课</Button>
          <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
        </Space>
        <Table columns={columns} dataSource={schedules} rowKey="id" loading={loading} scroll={{ x: 1100 }} pagination={{ pageSize: 10 }} />
      </Card>

      <Modal title="智能排课生成" open={genModal} onOk={handleGenerate} onCancel={() => { setGenModal(false); genForm.resetFields(); }}>
        <Form form={genForm} layout="vertical">
          <Form.Item name="academicYear" label="学年" rules={[{ required: true, message: '请输入学年' }]}>
            <Input placeholder="如 2025-2026" />
          </Form.Item>
          <Form.Item name="semester" label="学期" rules={[{ required: true, message: '请选择学期' }]}>
            <InputNumber min={1} max={2} style={{ width: '100%' }} placeholder="1-第一学期 2-第二学期" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ScheduleManage;
