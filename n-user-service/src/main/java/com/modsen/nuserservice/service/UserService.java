package com.modsen.nuserservice.service;

import com.modsen.nuserservice.entity.User;
import com.modsen.nuserservice.entity.UserArchive;
import com.modsen.nuserservice.exception.AppException;
import com.modsen.nuserservice.mapper.UserMapper;
import com.modsen.nuserservice.model.UserArchiveResponse;
import com.modsen.nuserservice.repository.UserArchiveRepository;
import com.modsen.nuserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserArchiveRepository userArchiveRepository;
    private final UserMapper userMapper;

    @Transactional
    public Optional<UserArchive> findNonActiveById(Long id) {
        return userArchiveRepository.findById(id);
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void activateUserById(Long id) {
        userArchiveRepository.findById(id).orElseThrow(
                () -> new AppException("Not found user with id " + id, HttpStatus.NOT_FOUND)
        );
        userRepository.activateUserById(id);
    }

    @Transactional
    public void deactivateUserById(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new AppException("Not found user with id " + id, HttpStatus.NOT_FOUND)
        );
        userRepository.deactivateUserById(id);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<UserArchiveResponse> findAllByEnabled(Boolean enabled, Pageable pageable) {
        return enabled == null ? findAll(pageable)
                .map(userMapper::userToUserArchiveResponse) :
                enabled ? userRepository.findAll(pageable).map(userMapper::userToUserArchiveResponse)
                        : userArchiveRepository.findAll(pageable).map(userMapper::userArchiveToUserArchiveResponse);
    }
}
