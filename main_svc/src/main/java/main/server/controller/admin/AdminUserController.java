package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.NewUserRequest;
import main.server.dto.UserDto;
import main.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids, @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("getUsers");
        List<UserDto> users = userService.getUsers(ids, from, size);
        log.info("users: {}", users);
        return users;
    }

    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public UserDto createUser(@RequestBody NewUserRequest request) {
        log.info("createUser:{}", request);
        request.validate();
        UserDto userDto = userService.addUser(request);
        log.info("userDto:{}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public String deleteUser(@PathVariable int userId) {
        log.info("deleteUser with userId:{}", userId);
        userService.deleteUser(userId);
        return "deleteUser";
    }
}
