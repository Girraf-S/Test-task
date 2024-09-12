package com.modsen.nuserservice.service;

import com.modsen.nuserservice.entity.User;
import com.modsen.nuserservice.exception.AppException;
import com.modsen.nuserservice.mapper.UserMapper;
import com.modsen.nuserservice.model.BookResponse;
import com.modsen.nuserservice.model.UserResponse;
import com.modsen.nuserservice.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${jwt.bearer}")
    private String bearer;
    @Value("${service.books-domain}")
    private String booksDomain;

    private final UserService userService;
    private final UserMapper userMapper;
    private final RestTemplate restTemplate;

    @Transactional(readOnly = true)
    public UserResponse getCurrentProfileInfo() {
        Long id = getIdFromSecurityContext();
        User user = userService.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND)
        );
        return userMapper.userToUserResponse(user);
    }

    private Long getIdFromSecurityContext() {
        return Long.parseLong(AuthUtil.extractClaimStringValue(
                SecurityContextHolder.getContext().getAuthentication(), "id")
        );
    }

    public List<BookResponse> getTakenBooks() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer +
                AuthUtil.extractClaimStringValue(SecurityContextHolder.getContext().getAuthentication(), "jwt")
                );
        BookResponse[] body = restTemplate.exchange(booksDomain + "/api/books/get", HttpMethod.GET,
                new HttpEntity<>(headers), BookResponse[].class).getBody();
        Objects.requireNonNull(body);
        return List.of(body);
    }
}
