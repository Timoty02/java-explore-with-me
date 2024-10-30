package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Category;
import main.server.dto.CategoryDto;
import main.server.dto.NewCategoryDto;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
import main.server.mapper.CategoryMapper;
import main.server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        try {
            Category categoryUp = categoryRepository.save(category);
            log.info("Category added: {}", categoryUp);
            return CategoryMapper.toCategoryDto(categoryUp);
        } catch (Exception e) {
            log.error("Error adding category: {}", e.getMessage());
            throw new ConflictException(e.getMessage());
        }
    }

    public void deleteCategoryAdmin(int catId) {
        log.info("Deleting category with id: {}", catId);
        if (!categoryRepository.existsById(catId)) {
            log.error("Category not found with id: {}", catId);
            throw new NotFoundException("Category not found");
        }
        try {
            categoryRepository.deleteById(catId);
        } catch (Exception e) {
            log.error("Error deleting category: {}", e.getMessage());
            throw new ConflictException(e.getMessage());
        }
        log.info("Category deleted with id: {}", catId);
    }
    public CategoryDto updateCategoryAdmin(int catId, CategoryDto newCategoryDto) {
        log.info("Updating category: {}", newCategoryDto);
        if (!categoryRepository.existsById(catId)) {
            log.error("Category not found with id: {}", catId);
            throw new NotFoundException("Category not found");
        }
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(catId);
        try{
            Category categoryUp = categoryRepository.save(category);
            log.info("Category updated: {}", categoryUp);
            return CategoryMapper.toCategoryDto(categoryUp);
        } catch (Exception e) {
            log.error("Error updating category: {}", e.getMessage());
            throw new ConflictException(e.getMessage());
        }
    }

    public List<CategoryDto> getCategoriesPub(int from, int size) {
        log.info("Getting all categories");
        List<Category> categories = categoryRepository.findAll().stream().skip(from).limit(size).toList();
        log.info("Found {} categories", categories);
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
    public CategoryDto getCategoryByIdPub(int catId) {
        log.info("Getting category with id: {}", catId);
        Category category = categoryRepository.findById(catId).orElseThrow( () -> new NotFoundException("Category with id=" + catId +" was not found"));
        log.info("Found category: {}", category);
        return CategoryMapper.toCategoryDto(category);
    }
}
