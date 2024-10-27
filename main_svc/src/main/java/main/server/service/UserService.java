package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.NewUserRequest;
import main.server.dao.User;
import main.server.dto.UserDto;
import main.server.mapper.UserMapper;
import main.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDto addUser(NewUserRequest request) {
        log.info("addUser: {}", request);
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User userUp = userRepository.save(user);
        log.info("user saved: {}", userUp);
        return UserMapper.toUserDto(userUp);
    }

    public UserDto getUser(int id) {
        log.info("getUser: {}", id);
        User user = userRepository.findById(id).orElseThrow();
        return UserMapper.toUserDto(user);
    }

    public void deleteUser(int id) {
        log.info("deleteUser: {}", id);
        userRepository.deleteById(id);
    }

    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        log.info("getUsers");
        List<User> users = userRepository.findAllById(ids).stream().skip(from).limit(size).toList();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}