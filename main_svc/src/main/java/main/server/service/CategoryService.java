package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Category;
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

    public void addCategory(NewCategoryDto newCategoryDto) {
        log.info("Adding new category: {}", newCategoryDto);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
    }
}
