import React, { useState } from 'react';
import { Layout, Menu, Avatar, Dropdown, Typography, theme, Space } from 'antd';
import {
  DashboardOutlined, UserOutlined, TeamOutlined, BookOutlined,
  HomeOutlined, FormOutlined, CalendarOutlined, TrophyOutlined,
  FileTextOutlined, MonitorOutlined, LogoutOutlined, SafetyCertificateOutlined,
  ExperimentOutlined, SoundOutlined
} from '@ant-design/icons';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '@/store/auth';

const { Header, Sider, Content } = Layout;
const { Text } = Typography;

const MainLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { user, clearAuth } = useAuthStore();
  const { token: { colorBgContainer, borderRadiusLG } } = theme.useToken();

  const menuItems = [
    { key: '/', icon: <DashboardOutlined />, label: '系统概览' },
    {
      key: 'system', icon: <SafetyCertificateOutlined />, label: '系统管理',
      children: [
        { key: '/system/user', icon: <UserOutlined />, label: '用户管理' },
      ],
    },
    {
      key: 'academic', icon: <BookOutlined />, label: '教务管理',
      children: [
        { key: '/academic/teacher', icon: <TeamOutlined />, label: '教师管理' },
        { key: '/academic/student', icon: <UserOutlined />, label: '学生管理' },
        { key: '/academic/course', icon: <BookOutlined />, label: '课程管理' },
        { key: '/academic/classroom', icon: <HomeOutlined />, label: '教室管理' },
        { key: '/academic/selection', icon: <FormOutlined />, label: '选课管理' },
        { key: '/academic/grade', icon: <FileTextOutlined />, label: '成绩管理' },
        { key: '/academic/exam', icon: <ExperimentOutlined />, label: '考试管理' },
        { key: '/academic/schedule', icon: <CalendarOutlined />, label: '排课管理' },
        { key: '/academic/graduation', icon: <TrophyOutlined />, label: '毕设管理' },
        { key: '/academic/evaluation', icon: <SoundOutlined />, label: '评价管理' },
      ],
    },
    {
      key: 'monitor', icon: <MonitorOutlined />, label: '监控中心',
      children: [
        { key: '/monitor/services', icon: <MonitorOutlined />, label: '服务监控' },
      ],
    },
  ];

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key);
  };

  const handleLogout = () => {
    clearAuth();
    navigate('/login');
  };

  const selectedKeys = [location.pathname];
  const openKeys = [location.pathname.split('/').slice(0, 2).join('/') || ''];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed} theme="dark">
        <div style={{ height: 48, margin: 12, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <SafetyCertificateOutlined style={{ fontSize: 24, color: '#fff' }} />
          {!collapsed && <Text strong style={{ color: '#fff', marginLeft: 8, fontSize: 16 }}>教务系统</Text>}
        </div>
        <Menu theme="dark" mode="inline" selectedKeys={selectedKeys} defaultOpenKeys={openKeys}
          items={menuItems} onClick={handleMenuClick} />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 24px', background: colorBgContainer, display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
          <Dropdown menu={{
            items: [
              { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', onClick: handleLogout },
            ]
          }}>
            <Space style={{ cursor: 'pointer' }}>
              <Avatar icon={<UserOutlined />} />
              <Text>{user?.realName || user?.username || '用户'}</Text>
            </Space>
          </Dropdown>
        </Header>
        <Content style={{ margin: 16 }}>
          <div style={{ padding: 24, minHeight: 360, background: colorBgContainer, borderRadius: borderRadiusLG }}>
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
