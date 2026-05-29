import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Space, Tag, Modal, Form, InputNumber, Input, Select, message, Tabs, Descriptions } from 'antd';
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';
import { gradeApi } from '@/api';

const GradeManage: React.FC = () => {
  const [grades, setGrades] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [addModal, setAddModal] = useState(false);
  const [queryModal, setQueryModal] = useState(false);
  const [queryData, setQueryData] = useState<any[]>([]);
  const [queryLoading, setQueryLoading] = useState(false);
  const [editRecord, setEditRecord] = useState<any>(null);
  const [addForm] = Form.useForm();
  const [queryForm] = Form.useForm();

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await gradeApi.list();
      setGrades(res.data.data || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { refresh(); }, []);

  const handleAdd = () => { setEditRecord(null); addForm.resetFields(); setAddModal(true); };
  const handleEdit = (record: any) => {
    setEditRecord(record);
    addForm.setFieldsValue(record);
    setAddModal(true);
  };

  const handleSubmit = async () => {
    const values = await addForm.validateFields();
    try {
      if (editRecord) {
        await gradeApi.update({ ...values, id: editRecord.id });
        message.success('更新成功');
      } else {
        await gradeApi.create(values);
        message.success('录入成功');
      }
      setAddModal(false);
      addForm.resetFields();
      setEditRecord(null);
      refresh();
    } catch { /* handled */ }
  };

  const handleQuery = async (type: 'student' | 'course') => {
    const values = await queryForm.validateFields();
    setQueryLoading(true);
    try {
      const res = type === 'student'
        ? await gradeApi.getStudentGrades(values.id)
        : await gradeApi.getCourseGrades(values.id);
      setQueryData(res.data.data || []);
    } catch { /* handled */ }
    setQueryLoading(false);
  };

  const scoreToGrade = (score: number) => {
    if (score >= 90) return { label: '优秀', color: 'green' };
    if (score >= 80) return { label: '良好', color: 'blue' };
    if (score >= 70) return { label: '中等', color: 'orange' };
    if (score >= 60) return { label: '及格', color: 'gold' };
    return { label: '不及格', color: 'red' };
  };

  const columns: ColumnType<any>[] = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '学生ID', dataIndex: 'studentId', key: 'studentId', width: 100 },
    { title: '课程ID', dataIndex: 'courseId', key: 'courseId', width: 100 },
    { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
    { title: '学期', dataIndex: 'semester', key: 'semester', width: 80 },
    { title: '平时成绩', dataIndex: 'regularScore', key: 'regularScore', width: 90 },
    { title: '期中成绩', dataIndex: 'midtermScore', key: 'midtermScore', width: 90 },
    { title: '期末成绩', dataIndex: 'finalScore', key: 'finalScore', width: 90 },
    {
      title: '总评', dataIndex: 'totalScore', key: 'totalScore', width: 80,
      render: (v: number) => {
        const g = scoreToGrade(v);
        return <Tag color={g.color}>{v} ({g.label})</Tag>;
      },
    },
    {
      title: 'GPA', dataIndex: 'gpa', key: 'gpa', width: 80,
      render: (v: number) => <Tag color="blue">{v?.toFixed(1)}</Tag>,
    },
    {
      title: '操作', key: 'action', width: 80,
      render: (_: any, record: any) => (
        <Button size="small" onClick={() => handleEdit(record)}>编辑</Button>
      ),
    },
  ];

  return (
    <div>
      <Card title="成绩管理">
        <Space style={{ marginBottom: 16 }}>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>录入成绩</Button>
          <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
          <Button onClick={() => setQueryModal(true)}>成绩查询</Button>
        </Space>
        <Table columns={columns} dataSource={grades} rowKey="id" loading={loading} scroll={{ x: 1100 }} pagination={{ pageSize: 10 }} />
      </Card>

      <Modal title={editRecord ? '编辑成绩' : '录入成绩'} open={addModal} onOk={handleSubmit} onCancel={() => { setAddModal(false); addForm.resetFields(); setEditRecord(null); }} width={600}>
        <Form form={addForm} layout="vertical">
          <Form.Item name="studentId" label="学生ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="courseId" label="课程ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="teacherId" label="教师ID">
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="academicYear" label="学年">
            <Input placeholder="如 2025-2026" />
          </Form.Item>
          <Form.Item name="semester" label="学期">
            <Select options={[{ value: 1, label: '第一学期' }, { value: 2, label: '第二学期' }]} />
          </Form.Item>
          <Form.Item name="regularScore" label="平时成绩">
            <InputNumber min={0} max={100} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="midtermScore" label="期中成绩">
            <InputNumber min={0} max={100} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="finalScore" label="期末成绩">
            <InputNumber min={0} max={100} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="成绩查询" open={queryModal} onCancel={() => { setQueryModal(false); setQueryData([]); queryForm.resetFields(); }} footer={null} width={900}>
        <Tabs items={[
          {
            key: 'student', label: '按学生查询',
            children: (
              <div>
                <Space style={{ marginBottom: 16 }}>
                  <Form form={queryForm} layout="inline">
                    <Form.Item name="id" rules={[{ required: true }]}>
                      <InputNumber placeholder="学生ID" />
                    </Form.Item>
                  </Form>
                  <Button type="primary" onClick={() => handleQuery('student')} loading={queryLoading}>查询</Button>
                </Space>
                <Table columns={columns.filter(c => c.key !== 'action')} dataSource={queryData} rowKey="id" size="small" pagination={{ pageSize: 5 }} />
              </div>
            ),
          },
          {
            key: 'course', label: '按课程查询',
            children: (
              <div>
                <Space style={{ marginBottom: 16 }}>
                  <Form form={queryForm} layout="inline">
                    <Form.Item name="id" rules={[{ required: true }]}>
                      <InputNumber placeholder="课程ID" />
                    </Form.Item>
                  </Form>
                  <Button type="primary" onClick={() => handleQuery('course')} loading={queryLoading}>查询</Button>
                </Space>
                <Table columns={columns.filter(c => c.key !== 'action')} dataSource={queryData} rowKey="id" size="small" pagination={{ pageSize: 5 }} />
              </div>
            ),
          },
        ]} />
      </Modal>
    </div>
  );
};

export default GradeManage;
