import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, message, Space } from 'antd';
import { UserOutlined, LockOutlined, SafetyCertificateOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { authApi } from '@/api';
import { useAuthStore } from '@/store/auth';

const { Title, Text } = Typography;

const Login: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const setAuth = useAuthStore((s) => s.setAuth);

  const onFinish = async (values: { username: string; password: string }) => {
    setLoading(true);
    try {
      const { data } = await authApi.login(values);
      setAuth(data.data);
      message.success('登录成功');
      navigate('/');
    } catch {
      // 错误已在拦截器中处理
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      display: 'flex', justifyContent: 'center', alignItems: 'center',
      minHeight: '100vh', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <Card style={{ width: 420, borderRadius: 12, boxShadow: '0 8px 40px rgba(0,0,0,0.12)' }}>
        <Space direction="vertical" size="large" style={{ width: '100%', textAlign: 'center' }}>
          <div>
            <SafetyCertificateOutlined style={{ fontSize: 48, color: '#667eea' }} />
            <Title level={3} style={{ marginBottom: 4 }}>教学管理系统</Title>
            <Text type="secondary">Educational Administration System</Text>
          </div>
          <Form name="login" onFinish={onFinish} size="large" autoComplete="off">
            <Form.Item name="username" rules={[{ required: true, message: '请输入用户名' }]}>
              <Input prefix={<UserOutlined />} placeholder="用户名" />
            </Form.Item>
            <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
              <Input.Password prefix={<LockOutlined />} placeholder="密码" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading} block>
                登 录
              </Button>
            </Form.Item>
          </Form>
          <Text type="secondary" style={{ fontSize: 12 }}>
            基于Spring Cloud Alibaba微服务架构
          </Text>
        </Space>
      </Card>
    </div>
  );
};

export default Login;
