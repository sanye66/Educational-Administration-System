import requests

services = [
    'edu-user', 'edu-teacher', 'edu-student', 'edu-course', 'edu-classroom',
    'edu-selection', 'edu-grade', 'edu-exam', 'edu-schedule', 'edu-graduation', 'edu-evaluation'
]

for svc in services:
    url = 'http://localhost:8848/nacos/v1/cs/configs'
    params = {'dataId': f'{svc}.yml', 'group': 'DEFAULT_GROUP'}
    
    # 获取配置
    r = requests.get(url, params=params)
    content = r.text
    
    # 替换端口为 13306 (Docker MySQL)
    content = content.replace('localhost:3306', 'localhost:13306')
    
    # 更新配置
    requests.post(url, data={'dataId': f'{svc}.yml', 'group': 'DEFAULT_GROUP', 'content': content})
    print(f'✅ {svc}.yml 端口已改为 13306')

print('\n所有服务配置已更新完成!')
