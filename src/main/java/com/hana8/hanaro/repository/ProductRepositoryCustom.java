package com.hana8.hanaro.repository;

import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchProducts(ProductSearchRequest request);
}
