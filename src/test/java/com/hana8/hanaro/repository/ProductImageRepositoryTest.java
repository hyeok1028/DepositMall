package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.ProductImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class ProductImageRepositoryTest {

    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .name("Test Product")
                .productType(ProductType.SAVINGS)
                .paymentAmount(1000L)
                .periodMonths(12)
                .maturityInterestRate(3.0)
                .earlyTerminationInterestRate(1.0)
                .isActive(true)
                .build();
        productRepository.save(testProduct);
    }

    @Test
    @DisplayName("상품 이미지 저장 및 상품 ID별 조회 테스트")
    void saveAndFindByProductId() {
        ProductImage image = ProductImage.builder()
                .product(testProduct)
                .originalName("test.jpg")
                .storedName("stored.jpg")
                .storedPath("/path/to/stored.jpg")
                .isRepresentative(true)
                .build();
        productImageRepository.save(image);

        List<ProductImage> images = productImageRepository.findByProductId(testProduct.getId());
        assertThat(images).hasSize(1);
        assertThat(images.get(0).getOriginalName()).isEqualTo("test.jpg");
    }
}
