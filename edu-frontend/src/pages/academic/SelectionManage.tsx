import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Space, Tag, Modal, Form, InputNumber, message, Alert, Descriptions, Tabs } from 'antd';
import { ReloadOutlined, SelectOutlined, SwapOutlined, ThunderboltOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';
import { selectionApi, courseApi } from '@/api';

const SelectionManage: React.FC = () => {
  const [records, setRecords] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectModal, setSelectModal] = useState(false);
  const [dropModal, setDropModal] = useState(false);
  const [queryModal, setQueryModal] = useState(false);
  const [queryData, setQueryData] = useState<any[]>([]);
  const [queryLoading, setQueryLoading] = useState(false);
  const [selectForm] = Form.useForm();
  const [dropForm] = Form.useForm();
  const [queryForm] = Form.useForm();

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await selectionApi.list();
      setRecords(res.data.data || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { refresh(); }, []);

  const handleSelect = async () => {
    const values = await selectForm.validateFields();
    try {
      const res = await selectionApi.selectCourse(values.studentId, values.courseId);
      message.success(res.data.data || '选课成功');
      setSelectModal(false);
      selectForm.resetFields();
      refresh();
    } catch { /* handled by interceptor */ }
  };

  const handleDrop = async () => {
    const values = await dropForm.validateFields();
    try {
      const res = await selectionApi.dropCourse(values.studentId, values.courseId);
      message.success(res.data.data || '退课成功');
      setDropModal(false);
      dropForm.resetFields();
      refresh();
    } catch { /* handled */ }
  };

  const handleQuery = async (type: 'student' | 'course') => {
    const values = await queryForm.validateFields();
    setQueryLoading(true);
    try {
      const res = type === 'student'
        ? await selectionApi.getStudentSelections(values.id)
        : await selectionApi.getCourseSelections(values.id);
      setQueryData(res.data.data || []);
    } catch { /* handled */ }
    setQueryLoading(false);
  };

  const columns: ColumnType<any>[] = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '学生ID', dataIndex: 'studentId', key: 'studentId', width: 100 },
    { title: '课程ID', dataIndex: 'courseId', key: 'courseId', width: 100 },
    { title: '学年', dataIndex: 'academicYear', key: 'academicYear', width: 100 },
    { title: '学期', dataIndex: 'semester', key: 'semester', width: 80 },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 100,
      render: (s: string) => {
        const m: Record<string, { label: string; color: string }> = {
          SELECTED: { label: '已选', color: 'green' },
          DROPPED: { label: '已退', color: 'red' },
          COMPLETED: { label: '已完成', color: 'blue' },
        };
        const item = m[s] || { label: s, color: 'default' };
        return <Tag color={item.color}>{item.label}</Tag>;
      },
    },
    { title: '选课时间', dataIndex: 'createTime', key: 'createTime', width: 180 },
  ];

  return (
    <div>
      <Alert
        message="高并发选课服务"
        description="选课服务采用 Redisson 分布式锁 + Redis 库存预扣减 + Seata AT 分布式事务，保障高并发场景下的数据一致性。"
        type="info" showIcon icon={<ThunderboltOutlined />}
        style={{ marginBottom: 16 }}
      />
      <Card title="选课记录管理">
        <Space style={{ marginBottom: 16 }}>
          <Button type="primary" icon={<SelectOutlined />} onClick={() => setSelectModal(true)}>学生选课</Button>
          <Button danger icon={<SwapOutlined />} onClick={() => setDropModal(true)}>学生退课</Button>
          <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
          <Button onClick={() => setQueryModal(true)}>查询选课</Button>
        </Space>
        <Table columns={columns} dataSource={records} rowKey="id" loading={loading} scroll={{ x: 900 }} pagination={{ pageSize: 10 }} />
      </Card>

      <Modal title="学生选课" open={selectModal} onOk={handleSelect} onCancel={() => { setSelectModal(false); selectForm.resetFields(); }}>
        <Form form={selectForm} layout="vertical">
          <Form.Item name="studentId" label="学生ID" rules={[{ required: true, message: '请输入学生ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="courseId" label="课程ID" rules={[{ required: true, message: '请输入课程ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="学生退课" open={dropModal} onOk={handleDrop} onCancel={() => { setDropModal(false); dropForm.resetFields(); }}>
        <Form form={dropForm} layout="vertical">
          <Form.Item name="studentId" label="学生ID" rules={[{ required: true, message: '请输入学生ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="courseId" label="课程ID" rules={[{ required: true, message: '请输入课程ID' }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="查询选课记录" open={queryModal} onCancel={() => { setQueryModal(false); setQueryData([]); queryForm.resetFields(); }} footer={null} width={800}>
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
                <Table columns={columns} dataSource={queryData} rowKey="id" size="small" pagination={{ pageSize: 5 }} />
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
                <Table columns={columns} dataSource={queryData} rowKey="id" size="small" pagination={{ pageSize: 5 }} />
              </div>
            ),
          },
        ]} />
      </Modal>
    </div>
  );
};

export default SelectionManage;
