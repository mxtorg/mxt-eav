package com.mxt.eav.repository;

import com.mxt.eav.model.EavEntity;
import com.mxt.eav.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EAV实体Repository
 */
@Repository
public interface EavEntityRepository extends JpaRepository<EavEntity, Long> {
    
    Optional<EavEntity> findByCode(String code);
    
    List<EavEntity> findByEntityType(EntityType entityType);
    
    List<EavEntity> findByEntityTypeId(Long entityTypeId);
    
    boolean existsByCode(String code);
    
    /**
     * 使用MySQL JSON函数查询属性
     * 注意：这是一个示例，实际使用时需要根据具体查询条件构造
     */
    @Query(value = "SELECT e FROM EavEntity e WHERE e.entityType.id = :entityTypeId " +
           "AND JSON_EXTRACT(e.attributes, CONCAT('$.', :attributeKey)) = :attributeValue",
           nativeQuery = false)
    List<EavEntity> findByAttribute(@Param("entityTypeId") Long entityTypeId,
                              @Param("attributeKey") String attributeKey,
                              @Param("attributeValue") String attributeValue);
}
