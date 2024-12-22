package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

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
            Principal principal,
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
        LOG.log(Level.INFO,"GET/product/catalog request from "+ principal.getName());

        return "catalog.html";
    }

    @Override
    @PostMapping("/catalog")
    public String catalog(
                          @ModelAttribute("form") CatalogPageViewForm form,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Principal principal,
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
        LOG.log(Level.INFO,"POST/product/catalog request from "+ principal.getName());

        return "catalog.html";
    }

    @Override
    @GetMapping("/{id}/details")
    public String detailsProduct(@PathVariable("id") Long id,
                                 @ModelAttribute("form") CatalogPageViewForm form,
                                 Principal principal,
                                 Model model) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        ProductDto productDto = productService.getById(id);
        ProductView productView=new ProductView(productDto.getId(),productDto.getName(),productDto.getPrice(),
                productDto.getUrl(),productDto.getCategoryId(),productDto.getSalePrice(),productDto.getQuantityProduct());
        ProductSearchForm productSearchForm = new ProductSearchForm();
        String categoryType = form.categoryType();
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("form", new CatalogPageViewForm(baseView, new ArrayList<>(), new ArrayList<>(), productSearchForm, categoryType));
        model.addAttribute("product", productView);
        LOG.log(Level.INFO,"GET/product/details request from "+ principal.getName()+ "product with id "+id);
        return "productDetails.html";
    }

    @Override
    @GetMapping("/list")
    public String listProducts(Model model,
                               Principal principal,
                               @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
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
        LOG.log(Level.INFO,"GET/product/list request from "+ principal.getName());
        return "ProductList.html";
    }
    @Override
    @PostMapping("/list")
    public String listProducts(Model model,Principal principal,
                               @ModelAttribute("form") AdminSearchForm form,
                               @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                               @RequestParam(value = "clearForm", required = false) String clearForm) {
        List<CategoryDto> categories = categoryService.getAll();
        List<ProductDto> products = productService.getAllNotDeleted();
        ProductSearchForm searchForm = form.productSearchForm();

        List<CategoryView> categoryViews = categories.stream()
                .map(category -> modelMapper.map(category,CategoryView.class))
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
        List<ProductDto> productList = filteredProducts.getContent();
        List<ProductView> productViews =productList.stream()
                .map(product -> modelMapper.map(product,ProductView.class))
                .toList();

        List<Long> productIds = new ArrayList<>();
        for (ProductDto productDto: productList){
            productIds.add(productDto.getId());
        }
        form = new AdminSearchForm(
                categoryIds,
                productIds,
                searchForm
        );
        model.addAttribute("form", form);
        model.addAttribute("categories",categoryViews);
        model.addAttribute("products",productViews);
        LOG.log(Level.INFO,"POST/product/catalog request from "+ principal.getName());
        return "ProductList.html";
    }


    @Override
    @GetMapping("/create")
    public String createForm(Principal principal,Model model) {
        List<CategoryDto> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("form", new ProductCreateForm());
        LOG.log(Level.INFO,"GET/product/create request from "+ principal.getName());
        return "ProductCreate.html";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") ProductCreateForm form,
                         BindingResult bindingResult,
                         Principal principal,Model model) {
        if (bindingResult.hasErrors()) {
            return "ProductCreate.html";
        }

        System.out.println(form.getCategoryID());
        ProductDto productDto = new ProductDto();
        productDto.setName(form.getName());
        productDto.setPrice(form.getPrice());
        productDto.setQuantityProduct(form.getQuantityProduct());
        productDto.setCategoryId(form.getCategoryID());
        productDto.setDescription(form.getDescription());
        productDto.setQuantityProduct(form.getQuantityProduct());
        productService.createProduct(productDto);
        LOG.log(Level.INFO,"POST/product/create request from "+ principal.getName());
        return "redirect:/product/list";
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           Principal principal,Model model) {
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
                productDto.getSalePrice(),
                productDto.getQuantityProduct()
        );
        ProductEditForm updateForm = new ProductEditForm(productView.getId(), productView.getName(),
                productView.getPrice(), productView.getUrl(), productView.getCategoryID(), sale,productView.getQuantityProduct());
        model.addAttribute("updateForm", updateForm);
        LOG.log(Level.INFO,"GET/product/edit request from "+ principal.getName()+" for product with id: "+id);
        return "ProductEdit.html";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("updateForm") ProductEditForm updateForm,
                       BindingResult bindingResult,
                       Principal principal,Model model) {
        if (bindingResult.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            return "ProductEdit.html";
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(updateForm.getName());
        productDto.setPrice(updateForm.getPrice());
        productDto.setCategoryId(updateForm.getCategoryID());
        productDto.setUrl(updateForm.getUrl());
        productDto.setQuantityProduct(updateForm.getQuantityProduct());
        productService.setSale(id, updateForm.getSale());
        productService.update(productDto);
        LOG.log(Level.INFO,"POST/product/edit request from "+ principal.getName()+" for product with id: "+id);
        return "redirect:/product/list";
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,Principal principal) {
        LOG.log(Level.INFO,"GET/product/delete request from "+ principal.getName()+" for product with id: "+id);
        productService.delete(id);
        return "redirect:/product/list";
    }
}
