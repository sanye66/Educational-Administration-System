import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, Tag, message, Popconfirm, Card } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons';
import { userApi } from '@/api';

const UserManage: React.FC = () => {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editRecord, setEditRecord] = useState<any>(null);
  const [form] = Form.useForm();

  const fetchData = async () => {
    setLoading(true);
    try {
      const { data: res } = await userApi.list();
      setData(res.data?.records || []);
    } catch { /* handled */ }
    setLoading(false);
  };

  useEffect(() => { fetchData(); }, []);

  const handleAdd = () => {
    setEditRecord(null);
    form.resetFields();
    setModalOpen(true);
  };

  const handleEdit = (record: any) => {
    setEditRecord(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    await userApi.delete(id);
    message.success('删除成功');
    fetchData();
  };

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (editRecord) {
      // 编辑时，如果密码字段为空，则不传递密码字段
      const updateData = { ...values, id: editRecord.id };
      if (!values.password || values.password.trim() === '') {
        delete updateData.password;
      }
      await userApi.update(updateData);
      message.success('更新成功');
    } else {
      await userApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false);
    fetchData();
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
    { title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '真实姓名', dataIndex: 'realName', key: 'realName' },
    {
      title: '用户类型', dataIndex: 'userType', key: 'userType',
      render: (type: string) => {
        const map: Record<string, { color: string; text: string }> = {
          ADMIN: { color: 'red', text: '管理员' },
          TEACHER: { color: 'blue', text: '教师' },
          STUDENT: { color: 'green', text: '学生' },
        };
        const item = map[type] || { color: 'default', text: type };
        return <Tag color={item.color}>{item.text}</Tag>;
      }
    },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '手机', dataIndex: 'phone', key: 'phone' },
    {
      title: '状态', dataIndex: 'status', key: 'status',
      render: (s: number) => s === 0 ? <Tag color="green">正常</Tag> : s === 1 ? <Tag color="red">禁用</Tag> : <Tag color="orange">锁定</Tag>
    },
    {
      title: '操作', key: 'action', width: 180,
      render: (_: any, record: any) => (
        <Space>
          <Button size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除?" onConfirm={() => handleDelete(record.id)}>
            <Button size="small" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      )
    },
  ];

  return (
    <Card>
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>新增用户</Button>
        <Button icon={<ReloadOutlined />} onClick={fetchData}>刷新</Button>
      </Space>
      <Table columns={columns} dataSource={data} rowKey="id" loading={loading} pagination={{ pageSize: 10 }} />

      <Modal title={editRecord ? '编辑用户' : '新增用户'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input disabled={!!editRecord} />
          </Form.Item>
          {!editRecord && (
            <Form.Item name="password" label="密码" rules={[{ required: true }]}>
              <Input.Password />
            </Form.Item>
          )}
          {editRecord && (
            <Form.Item name="password" label="新密码（留空则不修改）">
              <Input.Password placeholder="留空表示不修改密码" />
            </Form.Item>
          )}
          <Form.Item name="realName" label="真实姓名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="userType" label="用户类型" rules={[{ required: true }]}>
            <Select options={[
              { value: 'ADMIN', label: '管理员' },
              { value: 'TEACHER', label: '教师' },
              { value: 'STUDENT', label: '学生' },
            ]} />
          </Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="phone" label="手机"><Input /></Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default UserManage;
