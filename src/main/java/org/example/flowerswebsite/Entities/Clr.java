package org.example.flowerswebsite.Entities;

import org.example.flowerswebsite.Repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
//public class Clr implements CommandLineRunner {
//
//    private final CategoryRepository categoryRepository;
//
//    public Clr(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        CategoryEntity roseCategory = new CategoryEntity();
//        roseCategory.setName("Roses");
//        roseCategory.setDescription("Beautiful roses");
//        CategoryEntity bucketCategory = new CategoryEntity();
//        bucketCategory.setName("Buckets");
//        bucketCategory.setDescription("Beautiful buckets");
//        categoryRepository.save(roseCategory);
//        categoryRepository.save(bucketCategory);
//        System.out.println("Categories saved");
//    }
//}