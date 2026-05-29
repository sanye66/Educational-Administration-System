import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Table, Tag, Badge, Progress, Space, Button, Spin, Typography, Alert, message } from 'antd';
import { ReloadOutlined, MonitorOutlined, CheckCircleOutlined, WarningOutlined } from '@ant-design/icons';
import type { MonitorMetrics } from '@/types';
import { monitorApi } from '@/api';

const { Title, Text } = Typography;

const MonitorServices: React.FC = () => {
  const [services, setServices] = useState<MonitorMetrics[]>([]);
  const [infrastructure, setInfrastructure] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchServices = async () => {
    setLoading(true);
    try {
      const res = await monitorApi.getServices();
      if (res.data.code === 200) {
        setServices(res.data.data || []);
      } else {
        message.error('获取监控数据失败');
      }
    } catch (error) {
      console.error('获取监控数据失败:', error);
      message.error('获取监控数据失败，请检查监控服务是否启动');
    } finally {
      setLoading(false);
    }
  };

  const fetchInfrastructure = async () => {
    try {
      const res = await monitorApi.getInfrastructure();
      if (res.data.code === 200) {
        setInfrastructure(res.data.data || []);
      }
    } catch (error) {
      console.error('获取基础设施状态失败:', error);
    }
  };

  useEffect(() => { 
    fetchServices(); 
    fetchInfrastructure();
    
    // 每10秒自动刷新一次
    const interval = setInterval(() => {
      fetchServices();
      fetchInfrastructure();
    }, 10000);
    
    return () => clearInterval(interval);
  }, []);

  const columns = [
    {
      title: '服务名称', dataIndex: 'serviceName', key: 'serviceName', width: 160,
      render: (name: string) => <Text strong>{name}</Text>,
    },
    {
      title: '状态', dataIndex: 'status', key: 'status', width: 100,
      render: (status: string) => (
        <Badge status={status === 'UP' ? 'success' : 'error'} text={status === 'UP' ? '运行中' : '已停止'} />
      ),
    },
    {
      title: 'CPU', dataIndex: 'cpuUsage', key: 'cpuUsage', width: 150,
      render: (v: number) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Progress percent={v} size="small" strokeColor={v > 80 ? '#ff4d4f' : v > 60 ? '#faad14' : '#52c41a'} showInfo={false} style={{ width: 80 }} />
          <Text>{v}%</Text>
        </div>
      ),
    },
    {
      title: '内存', dataIndex: 'memoryUsage', key: 'memoryUsage', width: 150,
      render: (v: number) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Progress percent={v} size="small" strokeColor={v > 80 ? '#ff4d4f' : v > 60 ? '#faad14' : '#1890ff'} showInfo={false} style={{ width: 80 }} />
          <Text>{v}%</Text>
        </div>
      ),
    },
    {
      title: 'QPS', dataIndex: 'qps', key: 'qps', width: 100,
      render: (v: number) => <Tag color="blue">{v}</Tag>,
    },
    {
      title: '响应时间', dataIndex: 'responseTime', key: 'responseTime', width: 100,
      render: (v: number) => <Tag color={v > 200 ? 'red' : v > 100 ? 'orange' : 'green'}>{v}ms</Tag>,
    },
    {
      title: '错误率', dataIndex: 'errorRate', key: 'errorRate', width: 100,
      render: (v: number) => <Tag color={v > 1 ? 'red' : v > 0.1 ? 'orange' : 'green'}>{v}%</Tag>,
    },
  ];

  const upCount = services.filter((s: MonitorMetrics) => s.status === 'UP').length;
  const downCount = services.filter((s: MonitorMetrics) => s.status !== 'UP').length;
  const avgCpu = services.length ? Math.round(services.reduce((a: number, s: MonitorMetrics) => a + (s.cpuUsage || 0), 0) / services.length) : 0;
  const avgMem = services.length ? Math.round(services.reduce((a: number, s: MonitorMetrics) => a + (s.memoryUsage || 0), 0) / services.length) : 0;

  return (
    <div>
      <Title level={4}>
        <MonitorOutlined style={{ marginRight: 8 }} />
        系统监控中心
      </Title>
      <Alert
        message="监控数据来源：Spring Boot Admin + Prometheus + Micrometer"
        description="实时采集各微服务健康指标、JVM指标、HTTP指标。点击外部监控入口可跳转至 Grafana / Zipkin / Sentinel 控制台。"
        type="info" showIcon style={{ marginBottom: 16 }}
      />
      <Row gutter={[16, 16]}>
        <Col xs={12} sm={6}>
          <Card>
            <div style={{ textAlign: 'center' }}>
              <CheckCircleOutlined style={{ fontSize: 32, color: '#52c41a' }} />
              <div style={{ marginTop: 8, fontSize: 24, fontWeight: 600 }}>{upCount}</div>
              <div style={{ color: '#999' }}>服务运行中</div>
            </div>
          </Card>
        </Col>
        <Col xs={12} sm={6}>
          <Card>
            <div style={{ textAlign: 'center' }}>
              <WarningOutlined style={{ fontSize: 32, color: '#ff4d4f' }} />
              <div style={{ marginTop: 8, fontSize: 24, fontWeight: 600 }}>{downCount}</div>
              <div style={{ color: '#999' }}>服务异常</div>
            </div>
          </Card>
        </Col>
        <Col xs={12} sm={6}>
          <Card>
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 24, fontWeight: 600 }}>{avgCpu}%</div>
              <Progress percent={avgCpu} strokeColor={avgCpu > 80 ? '#ff4d4f' : '#1890ff'} showInfo={false} />
              <div style={{ color: '#999' }}>平均 CPU</div>
            </div>
          </Card>
        </Col>
        <Col xs={12} sm={6}>
          <Card>
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 24, fontWeight: 600 }}>{avgMem}%</div>
              <Progress percent={avgMem} strokeColor={avgMem > 80 ? '#ff4d4f' : '#52c41a'} showInfo={false} />
              <div style={{ color: '#999' }}>平均内存</div>
            </div>
          </Card>
        </Col>
      </Row>

      <Card
        title="微服务实时状态"
        style={{ marginTop: 16 }}
        extra={<Button icon={<ReloadOutlined />} onClick={fetchServices} loading={loading}>刷新</Button>}
      >
        <Table columns={columns} dataSource={services} rowKey="serviceName" loading={loading} pagination={false} size="small" />
      </Card>

      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col xs={24} md={12}>
          <Card title="外部监控入口">
            <Space direction="vertical" style={{ width: '100%' }} size="middle">
              <a href="http://localhost:8093" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="blue" style={{ fontSize: 14, padding: '4px 12px' }}>Spring Boot Admin</Tag>
                <Text type="secondary">应用健康监控</Text>
              </a>
              <a href="http://localhost:3000" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="orange" style={{ fontSize: 14, padding: '4px 12px' }}>Grafana</Tag>
                <Text type="secondary">指标可视化面板</Text>
              </a>
              <a href="http://localhost:9411" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="purple" style={{ fontSize: 14, padding: '4px 12px' }}>Zipkin</Tag>
                <Text type="secondary">分布式链路追踪</Text>
              </a>
              <a href="http://localhost:8858" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="green" style={{ fontSize: 14, padding: '4px 12px' }}>Sentinel</Tag>
                <Text type="secondary">流控与熔断降级</Text>
              </a>
              <a href="http://localhost:8848/nacos" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="cyan" style={{ fontSize: 14, padding: '4px 12px' }}>Nacos</Tag>
                <Text type="secondary">注册中心 & 配置中心</Text>
              </a>
              <a href="http://localhost:5601" target="_blank" rel="noreferrer" style={{ fontSize: 16 }}>
                <Tag color="default" style={{ fontSize: 14, padding: '4px 12px' }}>Kibana</Tag>
                <Text type="secondary">日志检索分析</Text>
              </a>
            </Space>
          </Card>
        </Col>
        <Col xs={24} md={12}>
          <Card title="基础设施状态">
            <Space direction="vertical" style={{ width: '100%' }} size="middle">
              {infrastructure.map(item => (
                <div key={item.name} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Text>{item.name} <Text type="secondary">(:{item.port})</Text></Text>
                  <Badge status={item.status === 'UP' ? 'success' : 'error'} text={item.status === 'UP' ? '正常' : '异常'} />
                </div>
              ))}
            </Space>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default MonitorServices;
