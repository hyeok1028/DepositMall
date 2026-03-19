package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchProducts(ProductSearchRequest request) {
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(product)
                .where(
                        nameContains(request.getName()),
                        productTypeEq(request.getProductType()),
                        isActiveEq(request.getIsActive())
                )
                .fetch();
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? QProduct.product.name.contains(name) : null;
    }

    private BooleanExpression productTypeEq(ProductType productType) {
        return productType != null ? QProduct.product.productType.eq(productType) : null;
    }

    private BooleanExpression isActiveEq(Boolean isActive) {
        return isActive != null ? QProduct.product.isActive.eq(isActive) : null;
    }
}
