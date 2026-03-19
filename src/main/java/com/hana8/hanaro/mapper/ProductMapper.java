package com.hana8.hanaro.mapper;

import com.hana8.hanaro.dto.product.ProductCreateRequest;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    Product toEntity(ProductResponse dto);

    Product toEntity(ProductCreateRequest dto);

    List<ProductResponse> toResponseList(List<Product> products);
}
