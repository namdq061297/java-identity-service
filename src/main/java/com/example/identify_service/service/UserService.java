package com.example.identify_service.service;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.entity.User;
import com.example.identify_service.exception.UserExceptions;
import com.example.identify_service.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UserExceptions.UsernameAlreadyExistsException(req.getUsername());
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException(id));
    }

    public User updateUser(String id, @NonNull UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException(id));


        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException(id));
        userRepository.delete(user);
    }
}
