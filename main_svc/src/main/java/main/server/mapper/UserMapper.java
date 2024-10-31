package main.server.mapper;

import main.server.dao.User;
import main.server.dto.UserDto;
import main.server.dto.UserShortDto;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static User toUser(UserShortDto userDto) {
        return new User(userDto.getId(), userDto.getName());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
