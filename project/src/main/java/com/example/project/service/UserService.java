package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveEditedUser(User userOldInfo, User editedUser) {
        userOldInfo.setUsername(editedUser.getUsername());
        userOldInfo.setPassword(passwordEncoder.encode(editedUser.getPassword()));
        userOldInfo.setName(editedUser.getName());
        userOldInfo.setLastname(editedUser.getLastname());
        userOldInfo.setPhoneNumber(editedUser.getPhoneNumber());
        userOldInfo.setEmail(editedUser.getEmail());

        userRepository.save(userOldInfo);
    }

    public Boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }
}

