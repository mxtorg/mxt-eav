package com.mxt.eav.repository;

import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 属性定义Repository
 */
@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    
    List<Attribute> findByEntityTypeOrderBySortOrderAsc(EntityType entityType);
    
    Optional<Attribute> findByEntityTypeAndCode(EntityType entityType, String code);
    
    List<Attribute> findByEntityTypeIdOrderBySortOrderAsc(Long entityTypeId);
}
