package com.mxt.eav.controller;

import com.mxt.eav.model.EntityType;
import com.mxt.eav.service.EntityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "EntityType", description = "实体类型管理接口")
public class EntityTypeController {

    private final EntityTypeService entityTypeService;

    /**
     * 获取所有实体类型
     */
    @GetMapping
    @Operation(summary = "获取所有实体类型", description = "查询系统中所有的实体类型列表")
    @ApiResponse(responseCode = "200", description = "成功获取实体类型列表")
    public ResponseEntity<List<EntityType>> getAll() {
        return ResponseEntity.ok(entityTypeService.getAll());
    }

    /**
     * 根据ID获取实体类型
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取实体类型", description = "通过实体类型ID查询单个实体类型详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取实体类型"),
            @ApiResponse(responseCode = "404", description = "实体类型不存在")
    })
    public ResponseEntity<EntityType> getById(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long id) {
        return entityTypeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建实体类型
     */
    @PostMapping
    @Operation(summary = "创建实体类型", description = "创建一个新的实体类型")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功创建实体类型"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    public ResponseEntity<EntityType> create(
            @Parameter(description = "实体类型信息", required = true) @RequestBody EntityType entityType) {
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
    @Operation(summary = "更新实体类型", description = "根据ID更新实体类型信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新实体类型"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "404", description = "实体类型不存在")
    })
    public ResponseEntity<EntityType> update(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long id,
            @Parameter(description = "更新的实体类型信息", required = true) @RequestBody EntityType entityType) {
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
    @Operation(summary = "删除实体类型", description = "根据ID删除实体类型")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "成功删除实体类型"),
            @ApiResponse(responseCode = "404", description = "实体类型不存在")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long id) {
        try {
            entityTypeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
