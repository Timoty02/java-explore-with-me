package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CategoryDto;
import main.server.dto.NewCategoryDto;
import main.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Adding new category: {}", newCategoryDto);
        newCategoryDto.validate();
        CategoryDto categoryDto = categoryService.addCategoryAdmin(newCategoryDto);
        log.info("Category added: {}", categoryDto);
        return categoryDto;
    }

    @DeleteMapping("/{cat-Id}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("cat-id") int catId) {
        log.info("Deleting category with id: {}", catId);
        categoryService.deleteCategoryAdmin(catId);
        log.info("Category deleted");
    }

    @PatchMapping("/{cat-id}")
    public CategoryDto updateCategory(@PathVariable("cat-id") int catId, @RequestBody CategoryDto newCategoryDto) {
        log.info("Updating category with id: {}", catId);
        newCategoryDto.validate();
        CategoryDto categoryDto = categoryService.updateCategoryAdmin(catId, newCategoryDto);
        log.info("Category updated: {}", categoryDto);
        return categoryDto;
    }

}
