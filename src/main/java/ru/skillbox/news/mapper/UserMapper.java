package ru.skillbox.news.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.news.dto.user.UserRequest;
import ru.skillbox.news.dto.user.UserResponse;
import ru.skillbox.news.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(UserRequest userRequest);

}
