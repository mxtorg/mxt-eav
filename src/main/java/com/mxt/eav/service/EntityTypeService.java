package com.mxt.eav.service;

import com.mxt.eav.model.EntityType;
import com.mxt.eav.repository.EntityTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 实体类型服务
 */
@Service
@RequiredArgsConstructor
public class EntityTypeService {
    
    private final EntityTypeRepository entityTypeRepository;
    
    /**
     * 创建实体类型
     * @param entityType 实体类型
     * @return 创建后的实体类型
     */
    @Transactional
    @CacheEvict(value = "entityTypes", allEntries = true)
    public EntityType create(EntityType entityType) {
        if (entityTypeRepository.existsByCode(entityType.getCode())) {
            throw new IllegalArgumentException(
                String.format("实体类型编码[%s]已存在", entityType.getCode())
            );
        }
        return entityTypeRepository.save(entityType);
    }
    
    /**
     * 更新实体类型
     * @param id 实体类型ID
     * @param entityType 实体类型数据
     * @return 更新后的实体类型
     */
    @Transactional
    @CacheEvict(value = "entityTypes", allEntries = true)
    public EntityType update(Long id, EntityType entityType) {
        EntityType existing = entityTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("实体类型[id=%d]不存在", id)
            ));
        
        if (!existing.getCode().equals(entityType.getCode()) 
            && entityTypeRepository.existsByCode(entityType.getCode())) {
            throw new IllegalArgumentException(
                String.format("实体类型编码[%s]已存在", entityType.getCode())
            );
        }
        
        existing.setName(entityType.getName());
        existing.setDescription(entityType.getDescription());
        
        return entityTypeRepository.save(existing);
    }
    
    /**
     * 根据ID获取实体类型
     * @param id 实体类型ID
     * @return 实体类型
     */
    @Cacheable(value = "entityTypes", key = "#id")
    public Optional<EntityType> getById(Long id) {
        return entityTypeRepository.findById(id);
    }
    
    /**
     * 根据编码获取实体类型
     * @param code 实体类型编码
     * @return 实体类型
     */
    @Cacheable(value = "entityTypes", key = "#code")
    public Optional<EntityType> getByCode(String code) {
        return entityTypeRepository.findByCode(code);
    }
    
    /**
     * 获取所有实体类型
     * @return 实体类型列表
     */
    public List<EntityType> getAll() {
        return entityTypeRepository.findAll();
    }
    
    /**
     * 删除实体类型
     * @param id 实体类型ID
     */
    @Transactional
    @CacheEvict(value = "entityTypes", allEntries = true)
    public void delete(Long id) {
        if (!entityTypeRepository.existsById(id)) {
            throw new IllegalArgumentException(
                String.format("实体类型[id=%d]不存在", id)
            );
        }
        entityTypeRepository.deleteById(id);
    }
}
