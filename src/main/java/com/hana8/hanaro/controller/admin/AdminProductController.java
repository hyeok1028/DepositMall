package com.hana8.hanaro.controller.admin;

import com.hana8.hanaro.dto.product.ProductCreateRequest;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductUpdateRequest;
import com.hana8.hanaro.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService service;

    @PostMapping({"", "/"})
    public ProductResponse registProduct(@Valid @RequestBody ProductCreateRequest dto) {
        return service.registProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductResponse editProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest dto) {
        dto.setId(id);
        return service.editProduct(dto);
    }

    @DeleteMapping("/{id}")
    public int removeProduct(@PathVariable Long id) {
        return service.removeProduct(id);
    }
}
