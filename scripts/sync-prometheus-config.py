#!/usr/bin/env python3
"""
从 Nacos 获取微服务列表并生成 Prometheus 配置文件
支持服务自动发现
"""

import json
import requests
import yaml
import time
import os
from pathlib import Path

# Nacos 配置
NACOS_SERVER = os.getenv('NACOS_SERVER', 'localhost:8848')
NAMESPACE_ID = os.getenv('NAMESPACE_ID', '')  # 默认公共命名空间

# Prometheus 配置
PROMETHEUS_CONFIG_DIR = os.getenv('PROM_CONFIG_DIR', './config')
PROMETHEUS_YML = os.path.join(PROMETHEUS_CONFIG_DIR, 'prometheus.yml')
PROMETHEUS_RELOAD_URL = os.getenv('PROM_RELOAD_URL', 'http://localhost:9090/-/reload')

# 已知服务的端口映射（用于匹配）
SERVICE_PORTS = {
    'edu-gateway': 8080,
    'edu-auth': 8081,
    'edu-user': 8082,
    'edu-teacher': 8083,
    'edu-student': 8084,
    'edu-course': 8085,
    'edu-classroom': 8086,
    'edu-selection': 8087,
    'edu-grade': 8088,
    'edu-exam': 8089,
    'edu-schedule': 8090,
    'edu-graduation': 8091,
    'edu-evaluation': 8092,
    'edu-monitor': 8093,
}


def get_services_from_nacos():
    """从 Nacos 获取所有服务列表"""
    try:
        url = f"http://{NACOS_SERVER}/nacos/v1/ns/catalog/services"
        params = {
            'pageNo': 1,
            'pageSize': 100,
        }
        if NAMESPACE_ID:
            params['namespaceId'] = NAMESPACE_ID
        
        response = requests.get(url, params=params, timeout=5)
        response.raise_for_status()
        
        data = response.json()
        services = []
        
        for service in data.get('serviceList', []):
            service_name = service.get('name', '')
            # 只处理 edu- 开头的服务
            if service_name.startswith('edu-'):
                services.append(service_name)
        
        return services
    except Exception as e:
        print(f"从 Nacos 获取服务列表失败: {e}")
        return list(SERVICE_PORTS.keys())  # 失败时返回默认列表


def generate_prometheus_config(services):
    """生成 Prometheus 配置文件"""
    config = {
        'global': {
            'scrape_interval': '15s',
            'evaluation_interval': '15s',
        },
        'scrape_configs': []
    }
    
    for service_name in services:
        port = SERVICE_PORTS.get(service_name, 0)
        if port == 0:
            print(f"警告: 未找到服务 {service_name} 的端口配置")
            continue
        
        job_config = {
            'job_name': service_name,
            'metrics_path': '/actuator/prometheus',
            'static_configs': [
                {
                    'targets': [f'{service_name}:{port}']
                }
            ]
        }
        config['scrape_configs'].append(job_config)
    
    return config


def write_config(config, filepath):
    """写入配置文件"""
    # 确保目录存在
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        yaml.dump(config, f, default_flow_style=False, allow_unicode=True)
    
    print(f"配置文件已更新: {filepath}")


def reload_prometheus():
    """热重载 Prometheus 配置"""
    try:
        response = requests.post(PROMETHEUS_RELOAD_URL, timeout=5)
        if response.status_code == 200:
            print("Prometheus 配置重载成功")
        else:
            print(f"Prometheus 配置重载失败: HTTP {response.status_code}")
    except Exception as e:
        print(f"重载 Prometheus 失败: {e}")


def main():
    print("=" * 60)
    print("Prometheus Nacos 服务发现同步工具")
    print("=" * 60)
    
    # 获取服务列表
    print("\n正在从 Nacos 获取服务列表...")
    services = get_services_from_nacos()
    print(f"发现 {len(services)} 个服务: {', '.join(services)}")
    
    # 生成配置
    print("\n正在生成 Prometheus 配置...")
    config = generate_prometheus_config(services)
    
    # 写入配置
    write_config(config, PROMETHEUS_YML)
    
    # 重载 Prometheus
    print("\n正在重载 Prometheus...")
    reload_prometheus()
    
    print("\n✅ 同步完成!")
    print("=" * 60)


if __name__ == '__main__':
    main()
