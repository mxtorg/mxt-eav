package com.mxt.eav;

import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.AttributeType;
import com.mxt.eav.model.EavEntity;
import com.mxt.eav.model.EntityType;
import com.mxt.eav.service.AttributeService;
import com.mxt.eav.service.EavEntityService;
import com.mxt.eav.service.EntityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * EAV操作模板
 * 提供简洁的API用于EAV操作
 */
@Component
@RequiredArgsConstructor
public class EavTemplate {
    
    private final EntityTypeService entityTypeService;
    private final AttributeService attributeService;
    private final EavEntityService eavEntityService;
    
    /**
     * 定义实体类型
     * @param code 编码
     * @param name 名称
     * @param description 描述
     * @return 实体类型构建器
     */
    public EntityTypeBuilder defineEntityType(String code, String name, String description) {
        return new EntityTypeBuilder(code, name, description);
    }
    
    /**
     * 实体类型构建器
     */
    public class EntityTypeBuilder {
        private final String code;
        private final String name;
        private final String description;
        
        public EntityTypeBuilder(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
        
        public EntityType build() {
            EntityType entityType = new EntityType();
            entityType.setCode(code);
            entityType.setName(name);
            entityType.setDescription(description);
            return entityTypeService.create(entityType);
        }
    }
    
    /**
     * 定义属性
     * @param entityTypeId 实体类型ID
     * @param code 属性编码
     * @param name 属性名称
     * @param dataType 数据类型
     * @return 属性构建器
     */
    public AttributeBuilder defineAttribute(Long entityTypeId, String code, String name, AttributeType dataType) {
        return new AttributeBuilder(entityTypeId, code, name, dataType);
    }
    
    /**
     * 属性构建器
     */
    public class AttributeBuilder {
        private final Long entityTypeId;
        private final String code;
        private final String name;
        private final AttributeType dataType;
        private String description;
        private boolean required = false;
        private String defaultValue;
        private Double minValue;
        private Double maxValue;
        private Integer maxLength;
        private Integer sortOrder = 0;
        
        public AttributeBuilder(Long entityTypeId, String code, String name, AttributeType dataType) {
            this.entityTypeId = entityTypeId;
            this.code = code;
            this.name = name;
            this.dataType = dataType;
        }
        
        public AttributeBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public AttributeBuilder required(boolean required) {
            this.required = required;
            return this;
        }
        
        public AttributeBuilder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
        
        public AttributeBuilder minValue(Double minValue) {
            this.minValue = minValue;
            return this;
        }
        
        public AttributeBuilder maxValue(Double maxValue) {
            this.maxValue = maxValue;
            return this;
        }
        
        public AttributeBuilder maxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }
        
        public AttributeBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }
        
        public Attribute build() {
            Attribute attribute = new Attribute();
            attribute.setCode(code);
            attribute.setName(name);
            attribute.setDataType(dataType);
            attribute.setDescription(description);
            attribute.setRequired(required);
            attribute.setDefaultValue(defaultValue);
            attribute.setMinValue(minValue);
            attribute.setMaxValue(maxValue);
            attribute.setMaxLength(maxLength);
            attribute.setSortOrder(sortOrder);
            return attributeService.create(entityTypeId, attribute);
        }
    }
    
    /**
     * 创建实体
     * @param entityTypeId 实体类型ID
     * @param code 实体编码
     * @param name 实体名称
     * @return 实体构建器
     */
    public EntityBuilder createEntity(Long entityTypeId, String code, String name) {
        return new EntityBuilder(entityTypeId, code, name);
    }
    
    /**
     * 实体构建器
     */
    public class EntityBuilder {
        private final Long entityTypeId;
        private final String code;
        private final String name;
        private final Map<String, Object> attributes = new HashMap<>();
        
        public EntityBuilder(Long entityTypeId, String code, String name) {
            this.entityTypeId = entityTypeId;
            this.code = code;
            this.name = name;
        }
        
        public EntityBuilder attribute(String key, Object value) {
            attributes.put(key, value);
            return this;
        }
        
        public EavEntity build() {
            EavEntity entity = new EavEntity();
            entity.setCode(code);
            entity.setName(name);
            entity.setAttributeMap(attributes);
            return eavEntityService.create(entityTypeId, entity);
        }
    }
}
