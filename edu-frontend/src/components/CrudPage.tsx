import React, { useEffect, useState, useCallback } from 'react';
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, Tag, message, Popconfirm, Card, DatePicker, Rate } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons';
import type { ColumnType } from 'antd/es/table';

interface CrudPageProps {
  title: string;
  columns: ColumnType<any>[];
  formFields: FormField[];
  fetchData: () => Promise<any[]>;
  onCreate: (values: any) => Promise<void>;
  onUpdate: (values: any) => Promise<void>;
  onDelete: (id: number) => Promise<void>;
  extraActions?: (record: any) => React.ReactNode;
}

export interface FormField {
  name: string;
  label: string;
  type: 'input' | 'select' | 'number' | 'date' | 'rate' | 'textarea';
  required?: boolean;
  options?: { value: string; label: string }[];
  disabled?: boolean;
  min?: number;
  max?: number;
}

const CrudPage: React.FC<CrudPageProps> = ({
  title, columns, formFields, fetchData, onCreate, onUpdate, onDelete, extraActions
}) => {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editRecord, setEditRecord] = useState<any>(null);
  const [form] = Form.useForm();

  const refresh = useCallback(async () => {
    setLoading(true);
    try {
      const list = await fetchData();
      setData(list);
    } catch { /* handled */ }
    setLoading(false);
  }, [fetchData]);

  useEffect(() => { refresh(); }, [refresh]);

  const handleAdd = () => { setEditRecord(null); form.resetFields(); setModalOpen(true); };
  const handleEdit = (record: any) => { setEditRecord(record); form.setFieldsValue(record); setModalOpen(true); };
  const handleDelete = async (id: number) => { await onDelete(id); message.success('删除成功'); refresh(); };

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (editRecord) {
      await onUpdate({ ...values, id: editRecord.id });
      message.success('更新成功');
    } else {
      await onCreate(values);
      message.success('创建成功');
    }
    setModalOpen(false);
    refresh();
  };

  const renderFormField = (field: FormField) => {
    switch (field.type) {
      case 'select': return <Select options={field.options} />;
      case 'number': return <InputNumber min={field.min} max={field.max} style={{ width: '100%' }} />;
      case 'date': return <DatePicker style={{ width: '100%' }} />;
      case 'rate': return <Rate />;
      case 'textarea': return <Input.TextArea rows={3} />;
      default: return <Input />;
    }
  };

  const allColumns = [
    ...columns,
    {
      title: '操作', key: 'action', width: 200, fixed: 'right' as const,
      render: (_: any, record: any) => (
        <Space>
          <Button size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除?" onConfirm={() => handleDelete(record.id)}>
            <Button size="small" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
          {extraActions && extraActions(record)}
        </Space>
      ),
    },
  ];

  return (
    <Card title={title}>
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>新增</Button>
        <Button icon={<ReloadOutlined />} onClick={refresh}>刷新</Button>
      </Space>
      <Table columns={allColumns} dataSource={data} rowKey="id" loading={loading} scroll={{ x: 1200 }} pagination={{ pageSize: 10 }} />
      <Modal title={editRecord ? `编辑${title}` : `新增${title}`} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} width={600} destroyOnClose>
        <Form form={form} layout="vertical">
          {formFields.map((field) => (
            <Form.Item key={field.name} name={field.name} label={field.label}
              rules={field.required ? [{ required: true, message: `请输入${field.label}` }] : undefined}>
              {renderFormField(field)}
            </Form.Item>
          ))}
        </Form>
      </Modal>
    </Card>
  );
};

export default CrudPage;
