# Spring Boot 开发模板

本文档提供 Spring Boot 项目的开发模板，基于公司规范生成。

## 1. Controller 模板

```java
package com.edu.{module}.controller;

import com.edu.common.core.domain.R;
import com.edu.{module}.entity.{EntityName};
import com.edu.{module}.service.I{EntityName}Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * {业务名称} Controller
 *
 * @author author
 */
@Tag(name = "{业务名称}")
@RestController
@RequestMapping("/api/{module}")
@RequiredArgsConstructor
public class {EntityName}Controller {

    private final I{EntityName}Service {entityName}Service;

    @Operation(summary = "根据ID获取{业务名称}")
    @GetMapping("/{id}")
    public R<{EntityName}> getById(@PathVariable Long id) {
        return R.ok({entityName}Service.getById(id));
    }

    @Operation(summary = "获取{业务名称}列表")
    @GetMapping("/list")
    public R<List<{EntityName}>> list({EntityName} {entityName}) {
        return R.ok({entityName}Service.list(new QueryWrapper<{EntityName}>()));
    }

    @Operation(summary = "新增{业务名称}")
    @PostMapping
    public R<Boolean> save(@RequestBody {EntityName} {entityName}) {
        return R.ok({entityName}Service.save({entityName}));
    }

    @Operation(summary = "修改{业务名称}")
    @PutMapping
    public R<Boolean> update(@RequestBody {EntityName} {entityName}) {
        return R.ok({entityName}Service.updateById({entityName}));
    }

    @Operation(summary = "删除{业务名称}")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok({entityName}Service.removeById(id));
    }
}
```

## 2. Service 接口模板

```java
package com.edu.{module}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.{module}.entity.{EntityName};

/**
 * {业务名称} Service接口
 *
 * @author author
 */
public interface I{EntityName}Service extends IService<{EntityName}> {

    /**
     * 根据ID获取{业务名称}
     */
    {EntityName} getById(Long id);

    /**
     * 保存{业务名称}
     */
    boolean save{EntityName}({EntityName} {entityName});
}
```

## 3. Service 实现模板

```java
package com.edu.{module}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.{module}.entity.{EntityName};
import com.edu.{module}.mapper.{EntityName}Mapper;
import com.edu.{module}.service.I{EntityName}Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {业务名称} Service实现
 *
 * @author author
 */
@Service
@RequiredArgsConstructor
public class {EntityName}ServiceImpl extends ServiceImpl<{EntityName}Mapper, {EntityName}>
    implements I{EntityName}Service {

    @Override
    public {EntityName} getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save{EntityName}({EntityName} {entityName}) {
        // 业务逻辑
        return this.save({entityName});
    }
}
```

## 4. Mapper 模板

```java
package com.edu.{module}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.{module}.entity.{EntityName};
import org.apache.ibatis.annotations.Mapper;

/**
 * {业务名称} Mapper接口
 *
 * @author author
 */
@Mapper
public interface {EntityName}Mapper extends BaseMapper<{EntityName}> {

}
```

## 5. Entity 模板

```java
package com.edu.{module}.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {业务名称} 实体
 *
 * @author author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("{table_name}")
public class {EntityName} extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // 字段示例
    private String fieldName;

    @TableField(exist = false)
    private String extraField;
}
```

## 6. DTO 模板

```java
package com.edu.{module}.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * {业务名称}新增DTO
 *
 * @author author
 */
@Data
@Schema(description = "{业务名称}新增DTO")
public class {EntityName}SaveDTO implements Serializable {

    @Schema(description = "字段名称")
    @NotBlank(message = "不能为空")
    private String fieldName;
}
```

## 7. VO 模板

```java
package com.edu.{module}.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * {业务名称}详情VO
 *
 * @author author
 */
@Data
@Schema(description = "{业务名称}详情VO")
public class {EntityName}DetailVO implements Serializable {

    private Long id;

    private String fieldName;

    private LocalDateTime createTime;
}
```

## 8. 配置类模板

```java
package com.edu.{module}.config;

import org.springframework.context.annotation.Configuration;

/**
 * {模块名称}配置类
 *
 * @author author
 */
@Configuration
public class {ModuleName}Config {

    // 配置示例
    // @Value("${edu.{module}.property:default}")
    // private String property;
}
```
