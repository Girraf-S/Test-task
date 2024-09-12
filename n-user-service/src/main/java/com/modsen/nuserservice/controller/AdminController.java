package com.modsen.nuserservice.controller;

import com.modsen.nuserservice.model.UserArchiveResponse;
import com.modsen.nuserservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Return all active users",
            description = "Roles with access: Admin"
    )
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('activate:users')")
    public Page<UserArchiveResponse> getAllUsers(Pageable pageable, @RequestParam(required = false) Boolean isActive){
        return adminService.getAllUsers(isActive, pageable);
    }

    @Operation(
            summary = "Activate user by id",
            description = "Roles with access: Admin"
    )
    @PreAuthorize("hasAuthority('activate:users')")
    @PutMapping("/activate/{id}")
    public void activateUser(@PathVariable Long id){
        adminService.activateUserById(id);
    }

    @Operation(
            summary = "Activate user by id",
            description = "Roles with access: Admin"
    )
    @PreAuthorize("hasAuthority('activate:users')")
    @PutMapping("/deactivate/{id}")
    public void deactivateUser(@PathVariable Long id){
        adminService.deactivateUserById(id);
    }
}
