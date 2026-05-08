package com.mxt.eav.repository;

import com.mxt.eav.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 实体类型Repository
 */
@Repository
public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {
    
    Optional<EntityType> findByCode(String code);
    
    boolean existsByCode(String code);
}
