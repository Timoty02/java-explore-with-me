package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CategoryDto;
import main.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<CategoryDto> getCategories() {
        log.info("Getting all categories");
        List<CategoryDto> categories = categoryService.getCategoriesPub();
        log.info("Categories found: {}", categories.size());
        return categories;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable int id) {
        log.info("Getting category by id: {}", id);
        CategoryDto categoryDto = categoryService.getCategoryByIdPub(id);
        log.info("Category found: {}", categoryDto);
        return categoryDto;
    }
}
