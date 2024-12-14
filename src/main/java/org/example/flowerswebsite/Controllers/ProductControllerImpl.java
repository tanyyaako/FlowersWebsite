package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.example.controllers.Product.ProductController;
import org.example.flowerswebsite.DTO.CategoryDto;
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
            Model model) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);

        List<CategoryDto> categoryDtos = categoryService.getByCategoryType(CategoryType.valueOf(categoryType.toUpperCase()));
        List<ProductView> allProductViews = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtos) {
            List<ProductDto> productDtos = productService.getAllByCategory(categoryDto.getId());
            List<ProductView> productViews = productDtos.stream()
                    .map(productDto -> modelMapper.map(productDto,ProductView.class))
                    .toList();
            allProductViews.addAll(productViews);
        }
        List<CategoryView> categoryViews = categoryDtos.stream()
                .map(categoryDto -> modelMapper.map(categoryDto,CategoryView.class))
                .toList();
        ProductSearchForm productSearchForm = new ProductSearchForm();
        model.addAttribute("form", new CatalogPageViewForm(baseView,new ArrayList<>(),new ArrayList<>(),productSearchForm,categoryType));
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",allProductViews);
        model.addAttribute("categoryType", categoryType);

        return "catalog.html";
    }

    @Override
    @PostMapping("/catalog")
    public String catalog(
                          @ModelAttribute("form") CatalogPageViewForm form,
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
        List<CategoryView> selectedCategoryViews = new ArrayList<>();
        if (searchForm.getCategoryViews() != null && !searchForm.getCategoryViews().isEmpty()) {
            selectedCategoryViews = searchForm.getCategoryViews();
        }

        List<CategoryDto> selectedCategoryDtos = selectedCategoryViews.stream()
                .map(selectedCategoryView -> modelMapper.map(selectedCategoryView,CategoryDto.class))
                .toList();
        List<ProductDto> filteredProducts = productService.getByCategoriesOrPrice(
                selectedCategoryDtos,
                searchForm.getFromPrice(),
                searchForm.getToPrice(),
                searchForm.getSearchProduct()
        );

        List<ProductView> productViews = filteredProducts.stream()
                .map(filteredProduct-> modelMapper.map(filteredProduct,ProductView.class))
                .toList();
        List<Long> productIds = new ArrayList<>();
        for (ProductView productView: productViews){
            productIds.add(productView.getId());
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
        model.addAttribute("products",productViews);

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

        System.out.println("categoryType from form: " + form.categoryType());

        ProductDto productDto = productService.getById(id);
        ProductView productView=new ProductView(productDto.getId(),productDto.getName(),productDto.getPrice(),
                productDto.getUrl(),productDto.getCategoryId(),productDto.getSalePrice());
        System.out.println(form.categoryType());
        ProductSearchForm productSearchForm = new ProductSearchForm();
        String categoryType = form.categoryType();
        System.out.println("type in details "+ categoryType);
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

    private ProductView convertToProductView(ProductDto productDto) {
        return new ProductView(
                productDto.getId(),
                productDto.getName(),
                productDto.getPrice(),
                productDto.getUrl(),
                productDto.getCategoryId(),
                productDto.getSalePrice()
        );
    }
}
