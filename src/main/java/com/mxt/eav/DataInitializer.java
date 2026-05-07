package com.mxt.eav;

import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.AttributeType;
import com.mxt.eav.model.EavEntity;
import com.mxt.eav.model.EntityType;
import com.mxt.eav.service.AttributeService;
import com.mxt.eav.service.EavEntityService;
import com.mxt.eav.service.EntityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化示例数据
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EntityTypeService entityTypeService;
    private final AttributeService attributeService;
    private final EavEntityService eavEntityService;

    @Override
    public void run(String... args) {
        initProductData();
    }

    /**
     * 初始化产品数据
     */
    private void initProductData() {
        try {
            // 创建产品实体类型
            EntityType productType = new EntityType();
            productType.setCode("PRODUCT");
            productType.setName("商品");
            productType.setDescription("电商平台商品");
            EntityType savedProductType = entityTypeService.create(productType);
            System.out.println("Created EntityType: " + savedProductType.getName());

            // 创建属性
            createAttribute(savedProductType.getId(), "name", "商品名称", AttributeType.STRING, true, null, null, null, 200, 1);
            createAttribute(savedProductType.getId(), "price", "价格", AttributeType.DECIMAL, true, null, 0.0, null, null, 2);
            createAttribute(savedProductType.getId(), "stock", "库存", AttributeType.INTEGER, true, null, 0.0, null, null, 3);
            createAttribute(savedProductType.getId(), "color", "颜色", AttributeType.STRING, false, null, null, null, 50, 4);
            createAttribute(savedProductType.getId(), "onSale", "是否在售", AttributeType.BOOLEAN, false, "true", null, null, null, 5);

            // 创建示例产品
            createProduct(savedProductType.getId(), "P001", "iPhone 15", 6999.0, 100, "黑色", true);
            createProduct(savedProductType.getId(), "P002", "MacBook Pro 14寸", 14999.0, 50, "深空灰色", true);
            createProduct(savedProductType.getId(), "P003", "AirPods Pro", 1899.0, 200, "白色", true);

            System.out.println("Sample data initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建属性
     */
    private void createAttribute(Long entityTypeId, String code, String name, AttributeType dataType,
                                  boolean required, String defaultValue, Double minValue, Double maxValue, Integer maxLength, int sortOrder) {
        Attribute attr = new Attribute();
        attr.setCode(code);
        attr.setName(name);
        attr.setDescription(name);
        attr.setDataType(dataType);
        attr.setRequired(required);
        attr.setDefaultValue(defaultValue);
        attr.setMinValue(minValue);
        attr.setMaxValue(maxValue);
        attr.setMaxLength(maxLength);
        attr.setSortOrder(sortOrder);
        Attribute savedAttr = attributeService.create(entityTypeId, attr);
        System.out.println("Created Attribute: " + savedAttr.getName());
    }

    /**
     * 创建产品实体
     */
    private void createProduct(Long entityTypeId, String code, String name, Double price, Integer stock, String color, Boolean onSale) {
        EavEntity entity = new EavEntity();
        entity.setCode(code);
        entity.setName(name);
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", name);
        attributes.put("price", price);
        attributes.put("stock", stock);
        attributes.put("color", color);
        attributes.put("onSale", onSale);
        entity.setAttributeMap(attributes);
        
        EavEntity savedEntity = eavEntityService.create(entityTypeId, entity);
        System.out.println("Created EavEntity: " + savedEntity.getName());
    }
}
