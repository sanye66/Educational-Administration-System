import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Table, Tag, Typography, Space, Badge, Progress, message, Button } from 'antd';
import {
  UserOutlined, TeamOutlined, BookOutlined, DashboardOutlined,
  CheckCircleOutlined, WarningOutlined, ClockCircleOutlined, ReloadOutlined
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { monitorApi } from '@/api';

const { Title } = Typography;

interface ServiceInfo {
  key: string;
  name: string;
  port: number;
  status: 'UP' | 'DOWN';
}

// 完整的微服务列表定义
const ALL_SERVICES: ServiceInfo[] = [
  { key: 'edu-gateway', name: 'API网关', port: 8080, status: 'DOWN' },
  { key: 'edu-auth', name: '认证服务', port: 8081, status: 'DOWN' },
  { key: 'edu-user', name: '用户服务', port: 8082, status: 'DOWN' },
  { key: 'edu-teacher', name: '教师服务', port: 8083, status: 'DOWN' },
  { key: 'edu-student', name: '学生服务', port: 8084, status: 'DOWN' },
  { key: 'edu-course', name: '课程服务', port: 8085, status: 'DOWN' },
  { key: 'edu-classroom', name: '教室服务', port: 8086, status: 'DOWN' },
  { key: 'edu-selection', name: '选课服务', port: 8087, status: 'DOWN' },
  { key: 'edu-grade', name: '成绩服务', port: 8088, status: 'DOWN' },
  { key: 'edu-exam', name: '考试服务', port: 8089, status: 'DOWN' },
  { key: 'edu-schedule', name: '排课服务', port: 8090, status: 'DOWN' },
  { key: 'edu-graduation', name: '毕设服务', port: 8091, status: 'DOWN' },
  { key: 'edu-evaluation', name: '评价服务', port: 8092, status: 'DOWN' },
  { key: 'edu-monitor', name: '监控服务', port: 8093, status: 'DOWN' },
];

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [services, setServices] = useState<ServiceInfo[]>(ALL_SERVICES);
  const [loading, setLoading] = useState(false);

  // 检查服务状态
  const checkServiceStatus = async () => {
    setLoading(true);
    try {
      const res = await monitorApi.getServices();
      if (res.data.code === 200) {
        const monitoredServices = res.data.data || [];
        
        // 创建运行中服务的映射表
        const runningServicesMap = new Map<string, string>();
        monitoredServices.forEach((service: any) => {
          runningServicesMap.set(service.serviceName, service.status);
        });
        
        // 更新所有服务的状态（保持完整列表）
        const updatedServices = ALL_SERVICES.map(service => {
          const monitoredStatus = runningServicesMap.get(service.key);
          return {
            ...service,
            status: monitoredStatus ? (monitoredStatus as 'UP' | 'DOWN') : 'DOWN',
          };
        });
        
        setServices(updatedServices);
      }
    } catch (error) {
      console.error('获取服务状态失败:', error);
      // 如果获取失败，保持所有服务为DOWN状态
      setServices(ALL_SERVICES);
    } finally {
      setLoading(false);
    }
  };

  // 初始加载和定时轮询
  useEffect(() => {
    // 立即执行一次
    checkServiceStatus();
    
    // 每10秒检查一次
    const interval = setInterval(checkServiceStatus, 10000);
    
    return () => clearInterval(interval);
  }, []);

  const serviceColumns = [
    { title: '服务名称', dataIndex: 'name', key: 'name' },
    { title: '端口', dataIndex: 'port', key: 'port' },
    {
      title: '状态', dataIndex: 'status', key: 'status',
      render: (status: string) => (
        <Badge status={status === 'UP' ? 'success' : 'error'} text={status === 'UP' ? '运行中' : '已停止'} />
      )
    },
  ];

  // 计算运行中的服务数量
  const runningServicesCount = services.filter(s => s.status === 'UP').length;
  const totalServicesCount = services.length;

  return (
    <div>
      <Title level={4}>系统概览</Title>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable onClick={() => navigate('/system/user')}>
            <Statistic title="用户总数" value={1286} prefix={<UserOutlined />} valueStyle={{ color: '#1890ff' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable onClick={() => navigate('/academic/teacher')}>
            <Statistic title="教师人数" value={156} prefix={<TeamOutlined />} valueStyle={{ color: '#52c41a' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable onClick={() => navigate('/academic/course')}>
            <Statistic title="课程数量" value={324} prefix={<BookOutlined />} valueStyle={{ color: '#722ed1' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable onClick={() => navigate('/monitor/services')}>
            <Statistic 
              title="服务实例" 
              value={`${runningServicesCount}/${totalServicesCount}`} 
              prefix={<DashboardOutlined />} 
              valueStyle={{ color: runningServicesCount === totalServicesCount ? '#52c41a' : '#fa8c16' }} 
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col xs={24} lg={16}>
          <Card 
            title="微服务状态" 
            extra={
              <Space>
                <Tag color="green"><CheckCircleOutlined /> Nacos 注册中心</Tag>
                <Button 
                  type="text" 
                  icon={<ReloadOutlined spin={loading} />} 
                  onClick={checkServiceStatus}
                  loading={loading}
                  size="small"
                >
                  刷新
                </Button>
              </Space>
            }
          >
            <Table columns={serviceColumns} dataSource={services} pagination={false} size="small" loading={loading} />
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="系统健康度">
            <Space direction="vertical" style={{ width: '100%' }} size="middle">
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>CPU 使用率</span><span>45%</span>
                </div>
                <Progress percent={45} strokeColor="#1890ff" showInfo={false} />
              </div>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>内存使用率</span><span>62%</span>
                </div>
                <Progress percent={62} strokeColor="#fa8c16" showInfo={false} />
              </div>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>磁盘使用率</span><span>38%</span>
                </div>
                <Progress percent={38} strokeColor="#52c41a" showInfo={false} />
              </div>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>服务可用率</span><span>{totalServicesCount > 0 ? Math.round((runningServicesCount / totalServicesCount) * 100) : 0}%</span>
                </div>
                <Progress 
                  percent={totalServicesCount > 0 ? Math.round((runningServicesCount / totalServicesCount) * 100) : 0} 
                  strokeColor={runningServicesCount === totalServicesCount ? '#52c41a' : '#faad14'} 
                  showInfo={false} 
                />
              </div>
            </Space>
          </Card>
          <Card title="监控入口" style={{ marginTop: 16 }}>
            <Space direction="vertical" style={{ width: '100%' }}>
              <a href="http://localhost:8093" target="_blank" rel="noreferrer">
                <Tag color="blue" style={{ fontSize: 14, padding: '4px 12px' }}>Spring Boot Admin</Tag>
              </a>
              <a href="http://localhost:3000" target="_blank" rel="noreferrer">
                <Tag color="orange" style={{ fontSize: 14, padding: '4px 12px' }}>Grafana 面板</Tag>
              </a>
              <a href="http://localhost:9411" target="_blank" rel="noreferrer">
                <Tag color="purple" style={{ fontSize: 14, padding: '4px 12px' }}>Zipkin 链路追踪</Tag>
              </a>
              <a href="http://localhost:8858" target="_blank" rel="noreferrer">
                <Tag color="green" style={{ fontSize: 14, padding: '4px 12px' }}>Sentinel 控制台</Tag>
              </a>
            </Space>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
