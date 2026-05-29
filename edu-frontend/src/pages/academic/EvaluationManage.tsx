import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Space, Tag, Modal, Form, Input, InputNumber, Rate, message } from 'antd';
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';
import { evaluationApi } from '@/api';

const EvaluationManage: React.FC = () => {
  const [records, setRecords] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitModal, setSubmitModal] = useState(false);
  const [form] = Form.useForm();

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await evaluationApi.list();
      setRecords(res.data.data || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { refresh(); }, []);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    try {
      await evaluationApi.submit(values);
      message.success('评价提交成功');
      setSubmitModal(false);
      form.resetFields();
      refresh();
    } catch { /* handled */ }
  };

  const columns: ColumnType<any>[] = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '学生ID', dataIndex: 'studentId', key: 'studentId', width: 100 },
    { title: '课程ID', dataIndex: 'courseId', key: 'courseId', width: 100 },
    { title: '教师ID', dataIndex: 'teacherId', key: 'teacherId', width: 100 },
    { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
    { title: '学期', dataIndex: 'semester', key: 'semester', width: 80 },
    {
      title: '态度评分', dataIndex: 'attitudeScore', key: 'attitudeScore', width: 100,
      render: (v: number) => <Rate disabled value={v} />,
    },
    {
      title: '内容评分', dataIndex: 'contentScore', key: 'contentScore', width: 100,
      render: (v: number) => <Rate disabled value={v} />,
    },
    {
      title: '方法评分', dataIndex: 'methodScore', key: 'methodScore', width: 100,
      render: (v: number) => <Rate disabled value={v} />,
    },
    {
      title: '效果评分', dataIndex: 'effectScore', key: 'effectScore', width: 100,
      render: (v: number) => <Rate disabled value={v} />,
    },
    {
      title: '总分', dataIndex: 'totalScore', key: 'totalScore', width: 80,
      render: (v: number) => {
        const color = v >= 4 ? 'green' : v >= 3 ? 'blue' : v >= 2 ? 'orange' : 'red';
        return <Tag color={color}>{v?.toFixed(1)}</Tag>;
      },
    },
    { title: '评语', dataIndex: 'comment', key: 'comment', ellipsis: true },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 80,
      render: (s: string) => <Tag color={s === 'SUBMITTED' ? 'green' : 'default'}>{s === 'SUBMITTED' ? '已提交' : s}</Tag>,
    },
  ];

  return (
    <div>
      <Card title="评价管理">
        <Space style={{ marginBottom: 16 }}>
          <Button type="primary" icon={<PlusOutlined />} onClick={() => setSubmitModal(true)}>提交评价</Button>
          <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
        </Space>
        <Table columns={columns} dataSource={records} rowKey="id" loading={loading} scroll={{ x: 1300 }} pagination={{ pageSize: 10 }} />
      </Card>

      <Modal title="提交教学评价" open={submitModal} onOk={handleSubmit} onCancel={() => { setSubmitModal(false); form.resetFields(); }} width={600}>
        <Form form={form} layout="vertical">
          <Form.Item name="studentId" label="学生ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="courseId" label="课程ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="teacherId" label="教师ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="academicYear" label="学年">
            <Input placeholder="如 2025-2026" />
          </Form.Item>
          <Form.Item name="semester" label="学期">
            <InputNumber min={1} max={2} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="attitudeScore" label="教学态度评分" rules={[{ required: true }]}>
            <Rate />
          </Form.Item>
          <Form.Item name="contentScore" label="教学内容评分" rules={[{ required: true }]}>
            <Rate />
          </Form.Item>
          <Form.Item name="methodScore" label="教学方法评分" rules={[{ required: true }]}>
            <Rate />
          </Form.Item>
          <Form.Item name="effectScore" label="教学效果评分" rules={[{ required: true }]}>
            <Rate />
          </Form.Item>
          <Form.Item name="comment" label="评语">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default EvaluationManage;
