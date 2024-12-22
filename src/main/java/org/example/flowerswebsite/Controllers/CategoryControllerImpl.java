package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.Category.CategoryController;
import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryType;
import org.example.flowerswebsite.services.CategoryService;
import org.example.flowerswebsite.services.ProductService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.Category.CategoryCreateForm;
import org.example.viewModel.Category.CategoryEditForm;
import org.example.viewModel.Category.CategoryView;
import org.example.viewModel.Product.ProductView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public CategoryControllerImpl(ModelMapper modelMapper, CategoryService categoryService, ProductService productService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.productService=productService;
    }

    @Override
    public BaseView createBaseViewModel(String logo, String aboutUsInformation) {
        return new BaseView(logo, aboutUsInformation);
    }

    @Override
    @GetMapping("/listCategory")
    public String listProducts(Principal principal, Model model){
        LOG.log(Level.INFO,"listCategory request from "+ principal.getName());
        List<CategoryDto> categoryDtos = categoryService.getAll();
        List<CategoryView> categoryViews = categoryDtos.stream()
                .map(categoryDto -> modelMapper.map(categoryDto,CategoryView.class))
                .toList();
        model.addAttribute("categories",categoryViews);
        return "CategoryList.html";
    }
    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model){
        LOG.log(Level.INFO,"GET/create/category request from "+ principal.getName());
        model.addAttribute("form", new CategoryCreateForm("",""));
        model.addAttribute("categoryTypes", List.of("Flowers", "Gifts", "Celebration"));
        return "CategoryList.html";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") CategoryCreateForm form,
                         BindingResult bindingResult,Principal principal,Model model){
        LOG.log(Level.INFO,"POST/create/category request from "+ principal.getName());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(form.name());
        categoryDto.setType(CategoryType.valueOf(form.type().toUpperCase()));
        categoryService.createCategory(categoryDto);
        return "redirect:/category/listCategory";
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,Principal principal,Model model){
        CategoryDto categoryDto = categoryService.findById(id);
        List<ProductDto> products = productService.getAllByCategory(categoryDto.getId());
        CategoryView categoryView = new CategoryView(
                categoryDto.getId(),
                categoryDto.getName(),
                categoryDto.getDescription(),
                categoryDto.getProductIds(),
                categoryDto.getType().toString()
        );
        LOG.log(Level.INFO,"GET/edit/category request from "+ principal.getName()+" product with id "+ categoryDto.getId());
        CategoryEditForm categoryEditForm = new CategoryEditForm(categoryView.getId(),categoryView.getName(),categoryView.getDescription(), categoryView.getType());
        model.addAttribute("updateCategory",categoryEditForm);
        model.addAttribute("categoryTypes", List.of("Flowers", "Gifts", "Celebration"));
        return "CategoryList.html";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("updateCategory") CategoryEditForm form,
                       BindingResult bindingResult,Principal principal, Model model){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        System.out.println(categoryDto.getId());
        categoryDto.setName(form.name());
        categoryDto.setType(CategoryType.valueOf(form.type().toUpperCase()));
        categoryService.updateCategory(categoryDto);
        LOG.log(Level.INFO,"POST/edit/category request from "+ principal.getName()+" product with id "+ categoryDto.getId());
        return "redirect:/category/listCategory";
    }
}
