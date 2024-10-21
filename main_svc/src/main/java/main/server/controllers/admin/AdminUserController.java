package main.server.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {
    @GetMapping
    public String getUsers() {
        log.info("getUsers");
        return "getUsers";
    }

    @PostMapping
    public String createUser() {
        log.info("createUser");
        return "createUser";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        log.info("deleteUser");
        return "deleteUser";
    }
}
