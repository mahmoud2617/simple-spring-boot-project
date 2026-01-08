package com.mahmoud.project.mapper;

import com.mahmoud.project.dto.RegisterUserRequest;
import com.mahmoud.project.dto.UpdateUserRequest;
import com.mahmoud.project.dto.UserDto;
import com.mahmoud.project.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest request, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void patch(UpdateUserRequest request, @MappingTarget User user);
}
