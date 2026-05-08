package com.mxt.eav.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 属性定义
 * 定义实体的属性元数据，包括名称、类型、约束等
 */
@Data
@Entity
@Table(name = "eav_attribute", indexes = {
    @Index(name = "idx_attribute_entity_type", columnList = "entity_type_id"),
    @Index(name = "idx_attribute_code", columnList = "code")
})
public class Attribute {

    /**
     * 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属实体类型 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_type_id", nullable = false)
    @JsonIgnore
    private EntityType entityType;

    /**
     * 属性编码，在同一实体类型下唯一 */
    @Column(nullable = false, length = 100)
    private String code;

    /**
     * 属性名称 */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 属性描述 */
    @Column(length = 500)
    private String description;

    /**
     * 属性数据类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 50)
    private AttributeType dataType;

    /**
     * 是否必填 */
    @Column(name = "is_required", nullable = false)
    private Boolean required = false;

    /**
     * 默认值（JSON格式存储） */
    @Column(name = "default_value", columnDefinition = "json")
    private String defaultValue;

    /**
     * 最小值（用于数值类型） */
    @Column(name = "min_value")
    private Double minValue;

    /**
     * 最大值（用于数值类型） */
    @Column(name = "max_value")
    private Double maxValue;

    /**
     * 字符串最大长度 */
    @Column(name = "max_length")
    private Integer maxLength;

    /**
     * 属性排序顺序 */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
