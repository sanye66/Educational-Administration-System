# Bean 注入规范

本文档定义了教学管理系统中 Spring Bean 的注入规范，确保代码质量与可维护性。

---

## 1. 注入方式选择

### 1.1 推荐：构造器注入

构造器注入是 **最推荐** 的方式，可以保证依赖不可变性和完全初始化。

```java
@Service
public class UserServiceImpl implements IUserService {

    private final IUserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(IUserMapper userMapper, 
                          RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }
}
```

### 1.2 可选：Setter 注入

适用于可选依赖或动态配置场景。

```java
@Service
public class NotificationService {

    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

### 1.3 禁止：字段注入

**禁止** 使用字段注入方式，缺陷：
- 无法保证依赖非空
- 无法保证依赖完全初始化
- 难以测试

```java
// ❌ 禁止使用
@Service
public class UserServiceImpl {

    @Autowired
    private IUserMapper userMapper;
}
```

---

## 2. Bean 作用域

### 2.1 默认：Singleton

单例作用域是 Spring 默认作用域，适用于无状态的 Bean。

```java
@Service
public class UserServiceImpl implements IUserService {
    // 单例作用域（默认）
}
```

### 2.2 Prototype：多例

适用于有状态的 Bean，或每次需要新实例的场景。

```java
@Service
@Scope("prototype")
public class OrderServiceImpl implements IOrderService {
    // 每次注入创建新实例
}
```

### 2.3 Request：请求级

适用于 Web 应用的请求级别共享。

```java
@RestController
@RequestScope
public class RequestContextController {
    // 同一请求内共享
}
```

---

## 3. 配置类规范

### 3.1 配置类声明

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // 配置序列化器
        return template;
    }
}
```

### 3.2 配置属性绑定

使用 `@ConfigurationProperties` 绑定配置属性：

```java
@Component
@ConfigurationProperties(prefix = "edu.datasource")
public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
```

---

## 4. 依赖管理

### 4.1 依赖查找原则

- 优先使用接口类型注入
- 避免使用具体实现类
- 使用 `@Qualifier` 明确指定 Bean 名称

```java
@Service
public class CourseServiceImpl implements ICourseService {

    private final ICourseMapper courseMapper;

    public CourseServiceImpl(@Qualifier("courseMapper") ICourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }
}
```

### 4.2 循环依赖处理

Spring 默认支持构造器循环依赖，但应 **尽量避免**：

```java
// ❌ 产生循环依赖
@Service
public class ServiceA {
    private final ServiceB serviceB;
    public ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    public ServiceB(ServiceA serviceA) { this.serviceA = serviceA; }
}
```

**解决方案**：重构代码，使用 setter 注入或提取公共服务。

---

## 5. 事务管理

### 5.1 事务传播行为

| 传播行为 | 说明 |
|----------|------|
| REQUIRED | 默认，当前有事务则加入，否则新建 |
| REQUIRES_NEW | 总是新建事务，挂起当前事务 |
| SUPPORTS | 有事务则加入，否则非事务执行 |
| NOT_SUPPORTED | 非事务执行，挂起当前事务 |
| NEVER | 非事务执行，存在事务则抛异常 |
| MANDATORY | 必须有事务，否则抛异常 |
| NESTED | 嵌套事务（需数据库支持） |

### 5.2 事务使用示例

```java
@Service
public class StudentServiceImpl implements IStudentService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveStudent(Student student) {
        // 业务逻辑
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(OperationLog log) {
        // 独立事务日志记录
    }
}
```

---

## 6. 常见问题

### 6.1 Bean 初始化顺序

- 使用 `@PostConstruct` 进行初始化
- 使用 `@PreDestroy` 进行销毁清理

```java
@Service
public class CacheService {

    private Map<String, Object> localCache;

    @PostConstruct
    public void init() {
        localCache = new ConcurrentHashMap<>();
    }

    @PreDestroy
    public void destroy() {
        localCache.clear();
    }
}
```

### 6.2 延迟加载问题

- 使用 `@Lazy` 解决循环依赖
- 注意延迟加载可能导致空指针

```java
@Service
public class ServiceA {
    private final ServiceB serviceB;

    public ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
```

### 6.3 静态方法注入

静态方法无法直接注入 Bean，使用以下方式：

```java
@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
```
