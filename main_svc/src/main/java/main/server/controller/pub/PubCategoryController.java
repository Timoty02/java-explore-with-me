package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CategoryDto;
import main.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class PubCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public PubCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all categories");
        List<CategoryDto> categories = categoryService.getCategoriesPub(from, size);
        log.info("Categories found: {}", categories.size());
        return categories;
    }

    @GetMapping("/{cat-id}")
    public CategoryDto getCategoryById(@PathVariable("cat-id") int catId) {
        log.info("Getting category by id: {}", catId);
        CategoryDto categoryDto = categoryService.getCategoryByIdPub(catId);
        log.info("Category found: {}", categoryDto);
        return categoryDto;
    }
}
