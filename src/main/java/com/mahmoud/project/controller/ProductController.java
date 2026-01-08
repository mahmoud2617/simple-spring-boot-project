package com.mahmoud.project.controller;

import com.mahmoud.project.dto.ProductDto;
import com.mahmoud.project.mapper.ProductMapper;
import com.mahmoud.project.repository.ProductRepository;
import com.mahmoud.project.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    ProductService productService;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getProducts(
            @RequestParam(name = "categoryId", required = false) String categoryId
    ) {
        return productService.getAllProducts(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable(name = "id") Long id) {
        ProductDto productDto = productService.getProduct(id);

        if (productDto == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDtoRequest,
            UriComponentsBuilder uriBuilder
    ) {
        ProductDto productDto = productService.createProduct(productDtoRequest);

        if (productDto == null)
            return ResponseEntity.status(422).build();

        URI uri = uriBuilder.path("/products/{id}")
                .buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long id,
            @Validated @RequestBody ProductDto productDtoRequest
    ) {
        ProductDto productDto = productService.updateProduct(id, productDtoRequest);

        if (productDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(productDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDtoRequest
    ) {
        ProductDto productDto = productService.patchProduct(id, productDtoRequest);

        if (productDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
