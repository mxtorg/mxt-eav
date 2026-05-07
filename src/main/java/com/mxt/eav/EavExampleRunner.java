package com.mxt.eav;

import com.mxt.eav.model.AttributeType;
import com.mxt.eav.model.EavEntity;
import com.mxt.eav.model.EntityType;
import com.mxt.eav.service.EavEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * EAV示例数据初始化器
 * 仅在dev profile下运行
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class EavExampleRunner implements CommandLineRunner {
    
    private final EavTemplate eavTemplate;
    private final EavEntityService eavEntityService;
    
    @Override
    public void run(String... args) {
        log.info("开始初始化EAV示例数据...");
        
        try {
            // 1. 创建商品实体类型
            EntityType productType = eavTemplate.defineEntityType(
                "PRODUCT", "商品", "电商平台商品"
            ).build();
            log.info("创建实体类型: {}", productType.getName());
            
            // 2. 创建商品属性
            eavTemplate.defineAttribute(
                productType.getId(), "name", "商品名称", AttributeType.STRING
            )
                .description("商品的名称")
                .required(true)
                .maxLength(200)
                .sortOrder(1)
                .build();
            
            eavTemplate.defineAttribute(
                productType.getId(), "price", "价格", AttributeType.DECIMAL
            )
                .description("商品价格")
                .required(true)
                .minValue(0.0)
                .sortOrder(2)
                .build();
            
            eavTemplate.defineAttribute(
                productType.getId(), "stock", "库存", AttributeType.INTEGER
            )
                .description("商品库存数量")
                .required(true)
                .minValue(0.0)
                .sortOrder(3)
                .build();
            
            eavTemplate.defineAttribute(
                productType.getId(), "color", "颜色", AttributeType.STRING
            )
                .description("商品颜色")
                .maxLength(50)
                .sortOrder(4)
                .build();
            
            eavTemplate.defineAttribute(
                productType.getId(), "onSale", "是否在售", AttributeType.BOOLEAN
            )
                .description("商品是否在售")
                .defaultValue("true")
                .sortOrder(5)
                .build();
            
            log.info("创建商品属性完成");
            
            // 3. 创建示例商品
            EavEntity product1 = eavTemplate.createEntity(
                productType.getId(), "P001", "iPhone 15"
            )
                .attribute("name", "iPhone 15")
                .attribute("price", 6999.00)
                .attribute("stock", 100)
                .attribute("color", "黑色")
                .attribute("onSale", true)
                .build();
            log.info("创建商品: {}", product1.getName());
            
            EavEntity product2 = eavTemplate.createEntity(
                productType.getId(), "P002", "MacBook Pro"
            )
                .attribute("name", "MacBook Pro 14寸")
                .attribute("price", 14999.00)
                .attribute("stock", 50)
                .attribute("color", "深空灰色")
                .attribute("onSale", true)
                .build();
            log.info("创建商品: {}", product2.getName());
            
            // 4. 查询并验证
            EavEntity foundProduct = eavEntityService.getById(product1.getId()).orElse(null);
            if (foundProduct != null) {
                log.info("查询到商品: {}, 属性: {}", 
                    foundProduct.getName(), 
                    foundProduct.getAttributeMap());
            }
            
            log.info("EAV示例数据初始化完成！");
            
        } catch (Exception e) {
            log.error("初始化示例数据失败", e);
        }
    }
}
