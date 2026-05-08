package com.mxt.eav.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * EAV实体
 * 存储实际的实体数据，动态属性存储在JSON字段中
 */
@Data
@Entity
@Table(name = "eav_entity", indexes = {
    @Index(name = "idx_eav_entity_type", columnList = "entity_type_id"),
    @Index(name = "idx_eav_entity_code", columnList = "code")
})
public class EavEntity {
    
    /**
     * 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 所属实体类型 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_type_id", nullable = false)
    private EntityType entityType;
    
    /**
     * 实体编码，唯一标识 */
    @Column(nullable = false, length = 100)
    private String code;
    
    /**
     * 实体名称 */
    @Column(nullable = false, length = 200)
    private String name;
    
    /**
     * 动态属性值，JSON格式存储 */
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private JsonNode attributes;
    
    /**
     * 用于操作属性的Map（非持久化） */
    @Transient
    private Map<String, Object> attributeMap = new HashMap<>();
    
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
    
    /**
     * 设置属性值
     * @param key 属性编码
     * @param value 属性值
     */
    @JsonAnySetter
    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }
    
    /**
     * 获取属性值
     * @param key 属性编码
     * @return 属性值
     */
    @JsonAnyGetter
    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }
    
    /**
     * 获取所有属性
     * @return 属性Map
     */
    public Map<String, Object> getAttributeMap() {
        return attributeMap;
    }
    
    /**
     * 设置所有属性
     * @param attributeMap 属性Map
     */
    public void setAttributeMap(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }
    
    /**
     * 将attributeMap转换为JsonNode（持久化前调用）
     * @param objectMapper Jackson ObjectMapper
     */
    public void convertToJsonNode(ObjectMapper objectMapper) {
        this.attributes = objectMapper.valueToTree(attributeMap);
    }
    
    /**
     * 将JsonNode转换为attributeMap（加载后调用）
     * @param objectMapper Jackson ObjectMapper
     */
    public void convertFromJsonNode(ObjectMapper objectMapper) {
        if (attributes != null) {
            this.attributeMap = objectMapper.convertValue(attributes, Map.class);
        } else {
            this.attributeMap = new HashMap<>();
        }
    }
}
