package com.mahmoud.project.mapper;

import com.mahmoud.project.dto.ProductDto;
import com.mahmoud.project.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto productDto);

    @Mapping(source = "categoryId", target = "category.id")
    void update(ProductDto productDtoRequest, @MappingTarget Product product);

    @Mapping(source = "categoryId", target = "category.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(ProductDto productDtoRequest, @MappingTarget Product product);
}
