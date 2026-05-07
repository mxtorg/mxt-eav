package com.mxt.eav.controller;

import com.mxt.eav.model.EntityType;
import com.mxt.eav.service.EntityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实体类型控制器
 */
@RestController
@RequestMapping("/api/entity-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EntityTypeController {

    private final EntityTypeService entityTypeService;

    /**
     * 获取所有实体类型
     */
    @GetMapping
    public ResponseEntity<List<EntityType>> getAll() {
        return ResponseEntity.ok(entityTypeService.getAll());
    }

    /**
     * 根据ID获取实体类型
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityType> getById(@PathVariable Long id) {
        return entityTypeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建实体类型
     */
    @PostMapping
    public ResponseEntity<EntityType> create(@RequestBody EntityType entityType) {
        try {
            EntityType created = entityTypeService.create(entityType);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新实体类型
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityType> update(@PathVariable Long id, @RequestBody EntityType entityType) {
        try {
            EntityType updated = entityTypeService.update(id, entityType);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除实体类型
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            entityTypeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
