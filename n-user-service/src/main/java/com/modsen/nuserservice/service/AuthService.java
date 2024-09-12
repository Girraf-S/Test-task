package com.modsen.nuserservice.service;

import com.modsen.nuserservice.entity.enums.Role;
import com.modsen.nuserservice.mapper.UserMapper;
import com.modsen.nuserservice.model.RegisterRequest;
import com.modsen.nuserservice.model.TokenResponse;
import com.modsen.nuserservice.entity.User;
import com.modsen.nuserservice.model.LoginRequest;
import com.modsen.nuserservice.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest loginRequest) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();

        return jwtService.generateToken(userDetails.getUser());
    }

    @Transactional
    public void readerRegistration(RegisterRequest registerRequest) {
        registerUser(registerRequest, Role.READER);
    }

    @Transactional
    public void librarianRegistration(RegisterRequest registerRequest) {
        User user = registerUser(registerRequest, Role.LIBRARIAN);
    }

    private User registerUser(RegisterRequest registerRequest, Role role) {
        checkRegisterData(registerRequest);

        User user = userMapper.registerRequestToUser(registerRequest, role, true);

        userService.save(user);

        return user;
    }

    private void checkRegisterData(RegisterRequest registerRequest) {
        if (userService.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email is exist");
        }
        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            throw new IllegalArgumentException("Password does not match");
        }
    }
}
