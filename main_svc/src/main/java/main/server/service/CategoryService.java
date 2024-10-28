package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Category;
import main.server.dto.CategoryDto;
import main.server.dto.NewCategoryDto;
import main.server.mapper.CategoryMapper;
import main.server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addCategoryAdmin(NewCategoryDto newCategoryDto) {
        log.info("Adding new category: {}", newCategoryDto);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
        log.info("Category added: {}", category);
        return CategoryMapper.toCategoryDto(category);
    }

    public void deleteCategoryAdmin(int catId) {
        log.info("Deleting category with id: {}", catId);
        categoryRepository.deleteById(catId);
        log.info("Category deleted with id: {}", catId);
    }
    public CategoryDto updateCategoryAdmin(int catId, CategoryDto newCategoryDto) {
        log.info("Updating category: {}", newCategoryDto);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(catId);
        categoryRepository.save(category);
        log.info("Category updated: {}", category);
        return CategoryMapper.toCategoryDto(category);
    }
}
