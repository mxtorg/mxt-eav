package com.mxt.eav.controller;

import com.mxt.eav.model.EavEntity;
import com.mxt.eav.service.EavEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EAV实体控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EavEntityController {

    private final EavEntityService eavEntityService;

    /**
     * 获取实体类型的所有实体
     */
    @GetMapping("/entity-types/{entityTypeId}/entities")
    public ResponseEntity<List<EavEntity>> getByEntityType(@PathVariable Long entityTypeId) {
        return ResponseEntity.ok(eavEntityService.getByEntityTypeId(entityTypeId));
    }

    /**
     * 根据ID获取实体
     */
    @GetMapping("/entities/{id}")
    public ResponseEntity<EavEntity> getById(@PathVariable Long id) {
        return eavEntityService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建实体
     */
    @PostMapping("/entity-types/{entityTypeId}/entities")
    public ResponseEntity<EavEntity> create(@PathVariable Long entityTypeId, @RequestBody EavEntity entity) {
        try {
            EavEntity created = eavEntityService.create(entityTypeId, entity);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新实体
     */
    @PutMapping("/entities/{id}")
    public ResponseEntity<EavEntity> update(@PathVariable Long id, @RequestBody EavEntity entity) {
        try {
            EavEntity updated = eavEntityService.update(id, entity);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除实体
     */
    @DeleteMapping("/entities/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            eavEntityService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
