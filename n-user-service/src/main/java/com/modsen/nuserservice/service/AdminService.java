package com.modsen.nuserservice.service;

import com.modsen.nuserservice.exception.AppException;
import com.modsen.nuserservice.model.UserArchiveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;

    public Page<UserArchiveResponse> getAllUsers(Boolean enabled, Pageable pageable) {
        return userService.findAllByEnabled(enabled, pageable);
    }

    public void activateUserById(Long id) {
        userService.findNonActiveById(id).ifPresentOrElse(
                user -> userService.activateUserById(id),
                () -> {
                    throw new AppException("Not found user with id = " + id, HttpStatus.NOT_FOUND);
                }
        );
    }

    public void deactivateUserById(Long id) {
        if (id == 1L) throw new AppException("You can't deactivate yourself", HttpStatus.NOT_ACCEPTABLE);
        userService.findById(id).ifPresentOrElse(
                user -> userService.deactivateUserById(id),
                () -> {
                    throw new AppException("Not found user with id = " + id, HttpStatus.NOT_FOUND);
                }
        );
    }
}
