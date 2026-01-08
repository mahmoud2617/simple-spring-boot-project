package com.mahmoud.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {
    @JsonIgnore
    Long id;

    String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String description;

    BigDecimal price;
    Byte categoryId;
}
