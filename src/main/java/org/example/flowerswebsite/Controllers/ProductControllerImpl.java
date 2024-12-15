package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.example.controllers.Product.ProductController;
import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.PageDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.CategoryType;
import org.example.flowerswebsite.services.CategoryService;
import org.example.flowerswebsite.services.ProductService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.Category.CategoryCreateForm;
import org.example.viewModel.Category.CategoryView;
import org.example.viewModel.Product.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public String catalog(
            @RequestParam(required = false) String categoryType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        List<CategoryDto> categoryDtos = categoryService.getByCategoryType(CategoryType.valueOf(categoryType.toUpperCase()));
        PageDto<ProductDto> productPage = productService.getPageProductsByCategories(
                categoryDtos.stream().map(CategoryDto::getId).toList(), page, size,categoryType);
        List<CategoryView> categoryViews = categoryDtos.stream()
                .map(categoryDto -> modelMapper.map(categoryDto,CategoryView.class))
                .toList();
        ProductSearchForm productSearchForm = new ProductSearchForm();
        model.addAttribute("form", new CatalogPageViewForm(baseView,new ArrayList<>(),new ArrayList<>(),productSearchForm,categoryType));
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",productPage);
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("currentPage", productPage.getPageNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "catalog.html";
    }

    @Override
    @PostMapping("/catalog")
    public String catalog(
                          @ModelAttribute("form") CatalogPageViewForm form,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model){
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        ProductSearchForm searchForm = form.productSearchForm();
        List<CategoryDto> categoryDtos = categoryService.getByCategoryType(
                CategoryType.valueOf(form.categoryType().toUpperCase())
        );
        List<CategoryView> categoryViews = categoryDtos.stream()
                .map(categoryDto -> modelMapper.map(categoryDto,CategoryView.class))
                .toList();
        List<Long> categoryIds = new ArrayList<>();
        for (CategoryView categoryView: categoryViews) {
            categoryIds.add(categoryView.getId());
        }
        List<CategoryDto> selectedCategoryDtos= new ArrayList<>();
        if (searchForm.getCategoryIds() != null && !searchForm.getCategoryIds().isEmpty()) {
             selectedCategoryDtos = form.productSearchForm().getCategoryIds().stream()
                    .map(categoryId -> categoryService.findById(categoryId))
                    .toList();
        }
        Page<ProductDto> filteredProducts = productService.getByCategoriesOrPrice(
                selectedCategoryDtos,
                searchForm.getFromPrice(),
                searchForm.getToPrice(),
                searchForm.getSearchProduct(),
                page,
                size
        );

        List<Long> productIds = new ArrayList<>();
        for (ProductDto productDto: filteredProducts){
            productIds.add(productDto.getId());
        }
        form = new CatalogPageViewForm(
                form.baseView(),
                categoryIds,
                productIds,
                searchForm,
                form.categoryType()
        );

        model.addAttribute("form", form);
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",filteredProducts);
        model.addAttribute("currentPage", filteredProducts.getNumber());
        model.addAttribute("totalPages", filteredProducts.getTotalPages());

        return "catalog.html";
    }

    @Override
    @GetMapping("/{id}/details")
    public String detailsProduct(@PathVariable("id") Long id,
                                 @ModelAttribute("form") CatalogPageViewForm form,
                                 Model model) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        ProductDto productDto = productService.getById(id);
        ProductView productView=new ProductView(productDto.getId(),productDto.getName(),productDto.getPrice(),
                productDto.getUrl(),productDto.getCategoryId(),productDto.getSalePrice());
        ProductSearchForm productSearchForm = new ProductSearchForm();
        String categoryType = form.categoryType();
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("form", new CatalogPageViewForm(baseView, new ArrayList<>(), new ArrayList<>(), productSearchForm, categoryType));
        model.addAttribute("product", productView);
        return "productDetails.html";
    }

    @Override
    @GetMapping("/list")
    public String listProducts(Model model) {
        List<CategoryDto> categories = categoryService.getAll();
        List<ProductDto> products = productService.getAllNotDeleted();
        List<ProductView> productViews =products.stream()
                .map(product -> modelMapper.map(product,ProductView.class))
                .toList();
        List<CategoryView> categoryViews = categories.stream()
                .map(category -> modelMapper.map(category,CategoryView.class))
                .toList();
        ProductSearchForm productSearchForm = new ProductSearchForm();
        model.addAttribute("form", new AdminSearchForm(new ArrayList<>(),new ArrayList<>(),productSearchForm));
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",productViews);
        return "ProductList.html";
    }
    @Override
    @PostMapping("/list")
    public String listProducts(Model model,
                               @ModelAttribute("form") AdminSearchForm form) {
        List<CategoryDto> categories = categoryService.getAll();
        List<ProductDto> products = productService.getAllNotDeleted();
        List<ProductView> productViews =products.stream()
                .map(product -> modelMapper.map(product,ProductView.class))
                .toList();
        List<CategoryView> categoryViews = categories.stream()
                .map(category -> modelMapper.map(category,CategoryView.class))
                .toList();
        ProductSearchForm productSearchForm = new ProductSearchForm();
        model.addAttribute("form", new AdminSearchForm(new ArrayList<>(),new ArrayList<>(),productSearchForm));
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",productViews);
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
        ProductEditForm updateForm = new ProductEditForm(productView.getId(), productView.getName(),
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
