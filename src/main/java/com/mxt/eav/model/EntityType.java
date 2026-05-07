package com.mxt.eav.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类型定义
 * 用于定义不同类型的实体，如商品、用户等
 */
@Data
@Entity
@Table(name = "eav_entity_type", indexes = {
    @Index(name = "idx_entity_type_code", columnList = "code", unique = true)
})
public class EntityType {
    
    /**
     * 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 实体类型编码，唯一标识 */
    @Column(nullable = false, unique = true, length = 100)
    private String code;
    
    /**
     * 实体类型名称 */
    @Column(nullable = false, length = 200)
    private String name;
    
    /**
     * 实体类型描述 */
    @Column(length = 500)
    private String description;
    
    /**
     * 该类型下的所有属性定义 */
    @OneToMany(mappedBy = "entityType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();
    
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
