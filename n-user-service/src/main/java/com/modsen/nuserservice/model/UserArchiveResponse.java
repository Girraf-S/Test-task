package com.modsen.nuserservice.model;

import com.modsen.nuserservice.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserArchiveResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean enabled;

}
