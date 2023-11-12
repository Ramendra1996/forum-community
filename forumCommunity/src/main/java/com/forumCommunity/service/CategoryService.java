package com.forumCommunity.service;

import com.forumCommunity.entity.Category;
import com.forumCommunity.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
//create category
    public Category  createCategory(Category category){
        category.setDateOfCreation(new Date());
        category.setDateOfModification(new Date());
        return categoryRepository.save(category);
    }
    //update category

    public Category updateCategory(Category category){
        Optional<Category> categoryObj = categoryRepository.findById(category.getCategoryId());
        categoryObj.get().setCategoryName(category.getCategoryName());
        categoryObj.get().setDateOfModification(new Date());
        return categoryRepository.save(category);
    }
//get All Category
    public List<Category> getAllCategory(){
        List<Category> allCategory = categoryRepository.findAll();
        return allCategory;
    }
}
