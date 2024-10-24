package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/categories")
public class PubCategoryController {
    @GetMapping
    public String getCategories() {
        return "getCategories";
    }

    @GetMapping("/{catId}")
    public String getCategoryById(@PathVariable int id) {
        return "getCategoryById";
    }
}
