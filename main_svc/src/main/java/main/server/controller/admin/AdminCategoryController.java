package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CategoryDto;
import main.server.dto.NewCategoryDto;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @PostMapping
    public String addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return "categoryAdded";
    }

    @DeleteMapping("/{catId}")
    public String deleteCategory(@PathVariable int catId) {
        return "categoryDeleted";
    }

    @PatchMapping("/{catId}")
    public String updateCategory(@PathVariable int catId, @RequestBody CategoryDto newCategoryDto) {
        return "categoryUpdated";
    }

}
