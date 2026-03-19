package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.entity.Product;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    private Product savingProduct;
    private Product depositProduct;

    @BeforeEach
    void setUp() {
        paymentHistoryRepository.deleteAll();
        subscriptionRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        savingProduct = Product.builder()
                .name("적금 상품")
                .productType(ProductType.SAVINGS)
                .paymentAmount(100000L)
                .periodMonths(12)
                .maturityInterestRate(3.5)
                .earlyTerminationInterestRate(1.0)
                .isActive(true)
                .build();

        depositProduct = Product.builder()
                .name("예금 상품")
                .productType(ProductType.DEPOSIT)
                .paymentAmount(1000000L)
                .periodMonths(12)
                .maturityInterestRate(3.0)
                .earlyTerminationInterestRate(0.5)
                .isActive(false)
                .build();

        productRepository.save(savingProduct);
        productRepository.save(depositProduct);
    }

    @Test
    @DisplayName("전체 조건으로 상품 검색")
    void searchProducts_AllConditions() {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .name("적금")
                .productType(ProductType.SAVINGS)
                .isActive(true)
                .build();

        List<Product> results = productRepository.searchProducts(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("적금");
    }

    @Test
    @DisplayName("이름 조건으로 상품 검색")
    void searchProducts_Name() {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .name("예금")
                .build();

        List<Product> results = productRepository.searchProducts(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("예금");
    }

    @Test
    @DisplayName("상품 타입 조건으로 상품 검색")
    void searchProducts_ProductType() {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .productType(ProductType.DEPOSIT)
                .build();

        List<Product> results = productRepository.searchProducts(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getProductType()).isEqualTo(ProductType.DEPOSIT);
    }

    @Test
    @DisplayName("활성화 상태 조건으로 상품 검색")
    void searchProducts_IsActive() {
        ProductSearchRequest requestActive = ProductSearchRequest.builder()
                .isActive(true)
                .build();
        List<Product> activeResults = productRepository.searchProducts(requestActive);
        assertThat(activeResults).hasSize(1);
        assertThat(activeResults.get(0).getIsActive()).isTrue();

        ProductSearchRequest requestInactive = ProductSearchRequest.builder()
                .isActive(false)
                .build();
        List<Product> inactiveResults = productRepository.searchProducts(requestInactive);
        assertThat(inactiveResults).hasSize(1);
        assertThat(inactiveResults.get(0).getIsActive()).isFalse();
    }

    @Test
    @DisplayName("빈 조건으로 상품 검색")
    void searchProducts_EmptyRequest() {
        ProductSearchRequest request = ProductSearchRequest.builder().build();
        List<Product> results = productRepository.searchProducts(request);
        assertThat(results).hasSize(2);
    }
}
