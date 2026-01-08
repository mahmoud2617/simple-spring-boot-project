package com.mahmoud.project.controller;

import com.mahmoud.project.dto.ChangePasswordRequest;
import com.mahmoud.project.dto.RegisterUserRequest;
import com.mahmoud.project.dto.UpdateUserRequest;
import com.mahmoud.project.dto.UserDto;
import com.mahmoud.project.service.UpdateUserPasswordStatus;
import com.mahmoud.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(name = "sort", required = false, defaultValue = "") String sort
    ) {
        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Long id) {
        UserDto userDto = userService.getUser(id);

        if (userDto == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest registerUserRequest,
            UriComponentsBuilder uriBuilder
    ) {
        UserDto userDto = userService.addUserWithProfile(registerUserRequest);

        var uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @Validated @RequestBody UpdateUserRequest userRequest
    ) {
        UserDto userDto = userService.updateUser(id, userRequest);

        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordRequest passwordRequest
    ) {
        UpdateUserPasswordStatus status = userService.updateUserPassword(id, passwordRequest);

        return switch (status) {
            case UPDATED -> ResponseEntity.status(200).build();
            case NOT_FOUND -> ResponseEntity.notFound().build();
            case INCORRECT_CURRENT_PASSWORD -> ResponseEntity.badRequest().build();
        };
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(
        @PathVariable(name = "id") Long id,
        @RequestBody UpdateUserRequest userRequest
    ) {
        UserDto userDto = userService.patchUser(id, userRequest);

        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
