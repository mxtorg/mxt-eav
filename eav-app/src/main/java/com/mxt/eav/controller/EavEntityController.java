package com.mxt.eav.controller;

import com.mxt.eav.model.EavEntity;
import com.mxt.eav.service.EavEntityService;
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
 * EAV实体控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "EavEntity", description = "EAV实体管理接口")
public class EavEntityController {

    private final EavEntityService eavEntityService;

    /**
     * 获取实体类型的所有实体
     */
    @GetMapping("/entity-types/{entityTypeId}/entities")
    @Operation(summary = "获取实体类型的所有实体", description = "查询指定实体类型下的所有实体列表")
    @ApiResponse(responseCode = "200", description = "成功获取实体列表")
    public ResponseEntity<List<EavEntity>> getByEntityType(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long entityTypeId) {
        return ResponseEntity.ok(eavEntityService.getByEntityTypeId(entityTypeId));
    }

    /**
     * 根据ID获取实体
     */
    @GetMapping("/entities/{id}")
    @Operation(summary = "根据ID获取实体", description = "通过实体ID查询单个实体详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取实体"),
            @ApiResponse(responseCode = "404", description = "实体不存在")
    })
    public ResponseEntity<EavEntity> getById(
            @Parameter(description = "实体ID", required = true) @PathVariable Long id) {
        return eavEntityService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建实体
     */
    @PostMapping("/entity-types/{entityTypeId}/entities")
    @Operation(summary = "创建实体", description = "为指定实体类型创建一个新的实体")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功创建实体"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    public ResponseEntity<EavEntity> create(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long entityTypeId,
            @Parameter(description = "实体信息", required = true) @RequestBody EavEntity entity) {
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
    @Operation(summary = "更新实体", description = "根据ID更新实体信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新实体"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "404", description = "实体不存在")
    })
    public ResponseEntity<EavEntity> update(
            @Parameter(description = "实体ID", required = true) @PathVariable Long id,
            @Parameter(description = "更新的实体信息", required = true) @RequestBody EavEntity entity) {
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
    @Operation(summary = "删除实体", description = "根据ID删除实体")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "成功删除实体"),
            @ApiResponse(responseCode = "404", description = "实体不存在")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "实体ID", required = true) @PathVariable Long id) {
        try {
            eavEntityService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
