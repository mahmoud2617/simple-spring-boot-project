package com.mahmoud.project.service;

import com.mahmoud.project.dto.ProductDto;
import com.mahmoud.project.entity.Category;
import com.mahmoud.project.entity.Product;
import com.mahmoud.project.mapper.ProductMapper;
import com.mahmoud.project.repository.CategoryRepository;
import com.mahmoud.project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts(String categoryId) {
        if (isParseableToByte(categoryId)) {
            return productRepository.findAllByCategoryId(Byte.parseByte(categoryId))
                    .stream()
                    .map(productMapper::toDto)
                    .toList();
        }

        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null)
            return null;

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if (category == null)
            return null;

        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);

        return productDto;
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDtoRequest) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null)
            return null;

        productMapper.update(productDtoRequest, product);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDto patchProduct(Long id, ProductDto productDtoRequest) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null)
            return null;

        productMapper.patch(productDtoRequest, product);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null)
            return false;

        productRepository.delete(product);

        return true;
    }

    private static boolean isParseableToByte(String s) {
        try {
            Byte.parseByte(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
