package com.mxt.eav.service;

import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.EntityType;
import com.mxt.eav.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 属性定义服务
 */
@Service
@RequiredArgsConstructor
public class AttributeService {
    
    private final AttributeRepository attributeRepository;
    private final EntityTypeService entityTypeService;
    
    /**
     * 创建属性
     * @param entityTypeId 实体类型ID
     * @param attribute 属性定义
     * @return 创建后的属性
     */
    @Transactional
    @CacheEvict(value = "attributes", allEntries = true)
    public Attribute create(Long entityTypeId, Attribute attribute) {
        EntityType entityType = entityTypeService.getById(entityTypeId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("实体类型[id=%d]不存在", entityTypeId)
            ));
        
        Optional<Attribute> existing = attributeRepository
            .findByEntityTypeAndCode(entityType, attribute.getCode());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                String.format("属性编码[%s]在该实体类型下已存在", attribute.getCode())
            );
        }
        
        attribute.setEntityType(entityType);
        return attributeRepository.save(attribute);
    }
    
    /**
     * 更新属性
     * @param id 属性ID
     * @param attribute 属性数据
     * @return 更新后的属性
     */
    @Transactional
    @CacheEvict(value = "attributes", allEntries = true)
    public Attribute update(Long id, Attribute attribute) {
        Attribute existing = attributeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("属性[id=%d]不存在", id)
            ));
        
        if (!existing.getCode().equals(attribute.getCode())) {
            Optional<Attribute> duplicate = attributeRepository
                .findByEntityTypeAndCode(existing.getEntityType(), attribute.getCode());
            if (duplicate.isPresent()) {
                throw new IllegalArgumentException(
                    String.format("属性编码[%s]在该实体类型下已存在", attribute.getCode())
                );
            }
        }
        
        existing.setName(attribute.getName());
        existing.setDescription(attribute.getDescription());
        existing.setDataType(attribute.getDataType());
        existing.setRequired(attribute.getRequired());
        existing.setDefaultValue(attribute.getDefaultValue());
        existing.setMinValue(attribute.getMinValue());
        existing.setMaxValue(attribute.getMaxValue());
        existing.setMaxLength(attribute.getMaxLength());
        existing.setSortOrder(attribute.getSortOrder());
        
        return attributeRepository.save(existing);
    }
    
    /**
     * 根据ID获取属性
     * @param id 属性ID
     * @return 属性
     */
    @Cacheable(value = "attributes", key = "#id")
    public Optional<Attribute> getById(Long id) {
        return attributeRepository.findById(id);
    }
    
    /**
     * 获取实体类型的所有属性
     * @param entityTypeId 实体类型ID
     * @return 属性列表
     */
    @Cacheable(value = "attributes", key = "'entityType_' + #entityTypeId")
    public List<Attribute> getByEntityTypeId(Long entityTypeId) {
        return attributeRepository.findByEntityTypeIdOrderBySortOrderAsc(entityTypeId);
    }
    
    /**
     * 删除属性
     * @param id 属性ID
     */
    @Transactional
    @CacheEvict(value = "attributes", allEntries = true)
    public void delete(Long id) {
        if (!attributeRepository.existsById(id)) {
            throw new IllegalArgumentException(
                String.format("属性[id=%d]不存在", id)
            );
        }
        attributeRepository.deleteById(id);
    }
}
