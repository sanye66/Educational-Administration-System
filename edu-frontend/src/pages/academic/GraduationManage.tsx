import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Space, Tag, Modal, Form, Input, InputNumber, Select, message } from 'antd';
import { PlusOutlined, ReloadOutlined, EditOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';
import { graduationApi } from '@/api';

const GraduationManage: React.FC = () => {
  const [topics, setTopics] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectModal, setSelectModal] = useState(false);
  const [editRecord, setEditRecord] = useState<any>(null);
  const [form] = Form.useForm();
  const [selectForm] = Form.useForm();

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await graduationApi.list();
      setTopics(res.data.data || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { refresh(); }, []);

  const handleAdd = () => { setEditRecord(null); form.resetFields(); setModalOpen(true); };
  const handleEdit = (record: any) => { setEditRecord(record); form.setFieldsValue(record); setModalOpen(true); };

  const handleSubmit = async () => {
    const values = await form.validateFields();
    try {
      if (editRecord) {
        await graduationApi.update({ ...values, id: editRecord.id });
        message.success('更新成功');
      } else {
        await graduationApi.create(values);
        message.success('创建成功');
      }
      setModalOpen(false);
      form.resetFields();
      setEditRecord(null);
      refresh();
    } catch { /* handled */ }
  };

  const handleSelectTopic = async () => {
    const values = await selectForm.validateFields();
    try {
      const res = await graduationApi.selectTopic(values.studentId, values.topicId);
      message.success(res.data.data || '选题成功');
      setSelectModal(false);
      selectForm.resetFields();
      refresh();
    } catch { /* handled */ }
  };

  const columns: ColumnType<any>[] = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '题目', dataIndex: 'title', key: 'title', width: 200, ellipsis: true },
    { title: '指导教师ID', dataIndex: 'teacherId', key: 'teacherId', width: 110 },
    { title: '选题学生ID', dataIndex: 'studentId', key: 'studentId', width: 110 },
    { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
    {
      title: '难度', dataIndex: 'difficulty', key: 'difficulty', width: 80,
      render: (d: string) => {
        const m: Record<string, { label: string; color: string }> = {
          EASY: { label: '简单', color: 'green' },
          MEDIUM: { label: '中等', color: 'orange' },
          HARD: { label: '困难', color: 'red' },
        };
        const item = m[d] || { label: d, color: 'default' };
        return <Tag color={item.color}>{item.label}</Tag>;
      },
    },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 100,
      render: (s: string) => {
        const m: Record<string, { label: string; color: string }> = {
          AVAILABLE: { label: '可选', color: 'green' },
          SELECTED: { label: '已选', color: 'blue' },
          COMPLETED: { label: '已完成', color: 'default' },
        };
        const item = m[s] || { label: s, color: 'default' };
        return <Tag color={item.color}>{item.label}</Tag>;
      },
    },
    {
      title: '操作', key: 'action', width: 100,
      render: (_: any, record: any) => (
        <Button size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
      ),
    },
  ];

  return (
    <div>
      <Card title="毕设管理">
        <Space style={{ marginBottom: 16 }}>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>新增题目</Button>
          <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
          <Button onClick={() => setSelectModal(true)}>学生选题</Button>
        </Space>
        <Table columns={columns} dataSource={topics} rowKey="id" loading={loading} scroll={{ x: 900 }} pagination={{ pageSize: 10 }} />
      </Card>

      <Modal title={editRecord ? '编辑毕设题目' : '新增毕设题目'} open={modalOpen} onOk={handleSubmit} onCancel={() => { setModalOpen(false); form.resetFields(); setEditRecord(null); }} width={600}>
        <Form form={form} layout="vertical">
          <Form.Item name="title" label="题目" rules={[{ required: true, message: '请输入题目' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="teacherId" label="指导教师ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="academicYear" label="学年">
            <Input placeholder="如 2025-2026" />
          </Form.Item>
          <Form.Item name="difficulty" label="难度">
            <Select options={[
              { value: 'EASY', label: '简单' },
              { value: 'MEDIUM', label: '中等' },
              { value: 'HARD', label: '困难' },
            ]} />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select options={[
              { value: 'AVAILABLE', label: '可选' },
              { value: 'SELECTED', label: '已选' },
              { value: 'COMPLETED', label: '已完成' },
            ]} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="学生选题" open={selectModal} onOk={handleSelectTopic} onCancel={() => { setSelectModal(false); selectForm.resetFields(); }}>
        <Form form={selectForm} layout="vertical">
          <Form.Item name="studentId" label="学生ID" rules={[{ required: true, message: '请输入学生ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="topicId" label="毕设题目ID" rules={[{ required: true, message: '请输入题目ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default GraduationManage;
