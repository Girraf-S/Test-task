package com.modsen.nuserservice.mapper;

import com.modsen.nuserservice.entity.enums.Role;
import com.modsen.nuserservice.entity.User;
import com.modsen.nuserservice.entity.UserArchive;
import com.modsen.nuserservice.model.RegisterRequest;
import com.modsen.nuserservice.model.UserArchiveResponse;
import com.modsen.nuserservice.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "registerRequest.password", qualifiedByName = "encode")
    User registerRequestToUser(RegisterRequest registerRequest, Role role, boolean enabled);

    @Mapping(target = "username", source = "user.email")
    @Mapping(target = "authorities", source = "user.role", qualifiedByName = "getAuthorities")
    UserResponse userToUserResponse(User user);

    UserArchiveResponse userToUserArchiveResponse(User user);

    UserArchiveResponse userArchiveToUserArchiveResponse(UserArchive userArchive);
}
