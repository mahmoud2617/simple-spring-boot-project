package com.mahmoud.project.service;

import com.mahmoud.project.dto.ChangePasswordRequest;
import com.mahmoud.project.dto.RegisterUserRequest;
import com.mahmoud.project.dto.UpdateUserRequest;
import com.mahmoud.project.dto.UserDto;
import com.mahmoud.project.entity.Profile;
import com.mahmoud.project.entity.User;
import com.mahmoud.project.mapper.UserMapper;
import com.mahmoud.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public List<UserDto> getAllUsers(String sort) {
        if (sort.isEmpty()) {
            return userRepository.findAllWithProfile()
                    .stream()
                    .map(userMapper::toDto)
                    .toList();
        }

        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return userRepository.findAllWithProfile(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long id) {
        return userRepository.findByIdWithProfile(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public UserDto addUserWithProfile(RegisterUserRequest registerUserRequest) {
        Profile profile = new Profile();

        User user = userMapper.toEntity(registerUserRequest);

        profile.setUser(user);
        user.setProfile(profile);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        userMapper.update(userRequest, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public UpdateUserPasswordStatus updateUserPassword(Long id, ChangePasswordRequest passwordequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return UpdateUserPasswordStatus.NOT_FOUND;
        }

        if (!user.getPassword().equals(passwordequest.getOldPassword())) {
            return UpdateUserPasswordStatus.INCORRECT_CURRENT_PASSWORD;
        }

        user.setPassword(passwordequest.getNewPassword());
        userRepository.save(user);

        return UpdateUserPasswordStatus.UPDATED;
    }

    @Transactional
    public UserDto patchUser(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            return null;

        userMapper.patch(userRequest, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            return false;

        userRepository.delete(user);

        return true;
    }
}
