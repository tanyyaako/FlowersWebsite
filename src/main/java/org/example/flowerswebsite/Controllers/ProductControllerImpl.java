package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.example.controllers.Product.ProductController;
import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.services.CategoryService;
import org.example.flowerswebsite.services.ProductService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.Category.CategoryView;
import org.example.viewModel.Product.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;

    @Autowired
    public ProductControllerImpl(ProductService productService, ModelMapper modelMapper,CategoryService categoryService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    @Override
    public BaseView createBaseViewModel(String logo, String aboutUsInformation) {
        return new BaseView(logo, aboutUsInformation);
    }
    @Override
    @GetMapping("/catalog")
    public String catalog(@ModelAttribute("form") CatalogPageViewForm form, Model model) {
        ProductSearchForm productSearchForm = form.productSearchForm();

        List<CategoryView> categoryViews = productSearchForm.categoryViews();
        List<CategoryDto> categoryDtos = categoryViews.stream()
                .map(categoryView -> modelMapper.map(categoryView, CategoryDto.class))
                .toList();
        Double priceFrom = productSearchForm.fromPrice();
        Double priceTo = productSearchForm.toPrice();
        List<ProductDto> productDtos = productService.getByCategoriesOrPrice(categoryDtos, priceFrom, priceTo);
        List<ProductView> productViews = productDtos.stream()
                .map(productDto -> new ProductView(
                        productDto.getId(),
                        productDto.getName(),
                        productDto.getPrice(),
                        productDto.getUrl(),
                        productDto.getCategoryId(),
                        productDto.getSalePrice()
                ))
                .toList();
        form = new CatalogPageViewForm(
                form.baseView(),
                form.categoryView(),
                productViews,
                productSearchForm
        );
        model.addAttribute("form", form);
        return "catalog.html";
    }

    @Override
    @GetMapping("/{id}")
    public String detailsProduct(@PathVariable("id") Long id, Model model) {
        ProductDto productDto = productService.getById(id);
        model.addAttribute("product", productDto);
        return "productDetails.html";
    }

    @Override
    @GetMapping("/list")
    public String listProducts(Model model) {
        List<CategoryDto> categories = categoryService.getAll();
        List<ProductDto> products = productService.getAllNotDeleted();
//        List<ProductView> productViews =products.stream()
//                .map(product -> new ProductView(
//                        product.getId(),
//                        product.getName(),
//                        product.getPrice(),
//                        product.getUrl(),
//                        product.getCategoryId()
//                ))
//                .toList();
        List<CategoryView> categoryViews = categories.stream()
                .map(category -> new CategoryView(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        products.stream()
                                .filter(product -> product.getCategoryId().equals(category.getId()))
                                .map(product -> new ProductView(
                                        product.getId(),
                                        product.getName(),
                                        product.getPrice(),
                                        product.getUrl(),
                                        product.getCategoryId(),
                                        product.getSalePrice()
                                ))
                                .toList()
                ))
                .toList();
        model.addAttribute("categories",categoryViews);
        return "ProductList.html";
    }

    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        List<CategoryDto> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("form", new ProductCreateForm());
        return "ProductCreate.html";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") ProductCreateForm form,
                         BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "list";
//        }
        System.out.println(form.getCategoryID());
        ProductDto productDto = new ProductDto();
        productDto.setName(form.getName());
        productDto.setPrice(form.getPrice());
        productDto.setQuantityProduct(form.getQuantityProduct());
        productDto.setCategoryId(form.getCategoryID());
        System.out.println(productDto.getCategoryId());
        productDto.setDescription(form.getDescription());
        productService.createProduct(productDto);
        return "redirect:/product/list";
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        List<CategoryDto> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        ProductDto productDto = productService.getById(id);
        Double sale=productService.getSaleOfProduct(productDto.getId());
        ProductView productView = new ProductView(
                productDto.getId(),
                productDto.getName(),
                productDto.getPrice(),
                productDto.getUrl(),
                productDto.getCategoryId(),
                productDto.getSalePrice()
        );
        ProductEditForm updateForm = new ProductEditForm(productView.getId(), productView.getNameOfProduct(),
                productView.getPrice(), productView.getUrl(), productView.getCategoryID(), sale);
        model.addAttribute("updateForm", updateForm);
        return "ProductEdit.html";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("updateForm") ProductEditForm updateForm,
                       BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "list";
//        }

        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(updateForm.getName());
        productDto.setPrice(updateForm.getPrice());
        productDto.setCategoryId(updateForm.getCategoryID());
        productDto.setUrl(updateForm.getUrl());
        productService.setSale(id, updateForm.getSale());
        productService.update(productDto);
        return "redirect:/product/list";
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/product/list";
    }
}
