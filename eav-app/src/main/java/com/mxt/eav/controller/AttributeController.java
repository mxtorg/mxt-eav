package com.mxt.eav.controller;

import com.mxt.eav.model.Attribute;
import com.mxt.eav.service.AttributeService;
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
 * 属性控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Attribute", description = "属性管理接口")
public class AttributeController {

    private final AttributeService attributeService;

    /**
     * 获取实体类型的所有属性
     */
    @GetMapping("/entity-types/{entityTypeId}/attributes")
    @Operation(summary = "获取实体类型的所有属性", description = "查询指定实体类型下的所有属性列表")
    @ApiResponse(responseCode = "200", description = "成功获取属性列表")
    public ResponseEntity<List<Attribute>> getByEntityType(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long entityTypeId) {
        return ResponseEntity.ok(attributeService.getByEntityTypeId(entityTypeId));
    }

    /**
     * 根据ID获取属性
     */
    @GetMapping("/attributes/{id}")
    @Operation(summary = "根据ID获取属性", description = "通过属性ID查询单个属性详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取属性"),
            @ApiResponse(responseCode = "404", description = "属性不存在")
    })
    public ResponseEntity<Attribute> getById(
            @Parameter(description = "属性ID", required = true) @PathVariable Long id) {
        return attributeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建属性
     */
    @PostMapping("/entity-types/{entityTypeId}/attributes")
    @Operation(summary = "创建属性", description = "为指定实体类型创建一个新的属性")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功创建属性"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    public ResponseEntity<Attribute> create(
            @Parameter(description = "实体类型ID", required = true) @PathVariable Long entityTypeId,
            @Parameter(description = "属性信息", required = true) @RequestBody Attribute attribute) {
        try {
            Attribute created = attributeService.create(entityTypeId, attribute);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新属性
     */
    @PutMapping("/attributes/{id}")
    @Operation(summary = "更新属性", description = "根据ID更新属性信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新属性"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "404", description = "属性不存在")
    })
    public ResponseEntity<Attribute> update(
            @Parameter(description = "属性ID", required = true) @PathVariable Long id,
            @Parameter(description = "更新的属性信息", required = true) @RequestBody Attribute attribute) {
        try {
            Attribute updated = attributeService.update(id, attribute);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除属性
     */
    @DeleteMapping("/attributes/{id}")
    @Operation(summary = "删除属性", description = "根据ID删除属性")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "成功删除属性"),
            @ApiResponse(responseCode = "404", description = "属性不存在")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "属性ID", required = true) @PathVariable Long id) {
        try {
            attributeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
