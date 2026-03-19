package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByNameContaining(String name);

    List<Product> findByProductType(ProductType productType);

    List<Product> findByIsActive(Boolean isActive);

    List<Product> findByNameContainingAndIsActive(String name, Boolean isActive);

    List<Product> findByProductTypeAndIsActive(ProductType productType, Boolean isActive);
}
