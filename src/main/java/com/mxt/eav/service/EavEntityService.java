package com.mxt.eav.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.EavEntity;
import com.mxt.eav.model.EntityType;
import com.mxt.eav.repository.EavEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EAV实体服务
 */
@Service
@RequiredArgsConstructor
public class EavEntityService {
    
    private final EavEntityRepository eavEntityRepository;
    private final EntityTypeService entityTypeService;
    private final AttributeService attributeService;
    private final AttributeValidationService validationService;
    private final ObjectMapper objectMapper;
    
    /**
     * 创建EAV实体
     * @param entityTypeId 实体类型ID
     * @param eavEntity EAV实体数据
     * @return 创建后的实体
     */
    @Transactional
    public EavEntity create(Long entityTypeId, EavEntity eavEntity) {
        EntityType entityType = entityTypeService.getById(entityTypeId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("实体类型[id=%d]不存在", entityTypeId)
            ));
        
        if (eavEntityRepository.existsByCode(eavEntity.getCode())) {
            throw new IllegalArgumentException(
                String.format("实体编码[%s]已存在", eavEntity.getCode())
            );
        }
        
        eavEntity.setEntityType(entityType);
        
        // 验证属性
        validateAttributes(entityType, eavEntity.getAttributeMap());
        
        // 转换为JSON
        eavEntity.convertToJsonNode(objectMapper);
        
        return eavEntityRepository.save(eavEntity);
    }
    
    /**
     * 更新EAV实体
     * @param id 实体ID
     * @param eavEntity 实体数据
     * @return 更新后的实体
     */
    @Transactional
    public EavEntity update(Long id, EavEntity eavEntity) {
        EavEntity existing = eavEntityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("实体[id=%d]不存在", id)
            ));
        
        // 先加载现有属性
        existing.convertFromJsonNode(objectMapper);
        
        if (!existing.getCode().equals(eavEntity.getCode()) 
            && eavEntityRepository.existsByCode(eavEntity.getCode())) {
            throw new IllegalArgumentException(
                String.format("实体编码[%s]已存在", eavEntity.getCode())
            );
        }
        
        existing.setName(eavEntity.getName());
        existing.setCode(eavEntity.getCode());
        
        // 合并新属性
        Map<String, Object> newAttributes = eavEntity.getAttributeMap();
        if (newAttributes != null) {
            existing.getAttributeMap().putAll(newAttributes);
        }
        
        // 验证属性
        validateAttributes(existing.getEntityType(), existing.getAttributeMap());
        
        // 转换为JSON
        existing.convertToJsonNode(objectMapper);
        
        return eavEntityRepository.save(existing);
    }
    
    /**
     * 根据ID获取实体
     * @param id 实体ID
     * @return 实体
     */
    public Optional<EavEntity> getById(Long id) {
        Optional<EavEntity> entity = eavEntityRepository.findById(id);
        entity.ifPresent(e -> e.convertFromJsonNode(objectMapper));
        return entity;
    }
    
    /**
     * 根据编码获取实体
     * @param code 实体编码
     * @return 实体
     */
    public Optional<EavEntity> getByCode(String code) {
        Optional<EavEntity> entity = eavEntityRepository.findByCode(code);
        entity.ifPresent(e -> e.convertFromJsonNode(objectMapper));
        return entity;
    }
    
    /**
     * 获取实体类型的所有实体
     * @param entityTypeId 实体类型ID
     * @return 实体列表
     */
    public List<EavEntity> getByEntityTypeId(Long entityTypeId) {
        List<EavEntity> entities = eavEntityRepository.findByEntityTypeId(entityTypeId);
        entities.forEach(e -> e.convertFromJsonNode(objectMapper));
        return entities;
    }
    
    /**
     * 删除实体
     * @param id 实体ID
     */
    @Transactional
    public void delete(Long id) {
        if (!eavEntityRepository.existsById(id)) {
            throw new IllegalArgumentException(
                String.format("实体[id=%d]不存在", id)
            );
        }
        eavEntityRepository.deleteById(id);
    }
    
    /**
     * 验证实体属性
     */
    private void validateAttributes(EntityType entityType, Map<String, Object> attributeValues) {
        if (attributeValues == null || attributeValues.isEmpty()) {
            return;
        }
        
        List<Attribute> attributes = attributeService.getByEntityTypeId(entityType.getId());
        
        for (Attribute attribute : attributes) {
            Object value = attributeValues.get(attribute.getCode());
            validationService.validate(attribute, value);
        }
    }
}
