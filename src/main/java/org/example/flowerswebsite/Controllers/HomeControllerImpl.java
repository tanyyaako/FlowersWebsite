package org.example.flowerswebsite.Controllers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.Home.HomeController;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.services.ProductService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.BestSellerModel.BestSellerView;
import org.example.viewModel.Product.ProductView;
import org.example.viewModel.SalesOfMonth.SalesOfMonthsView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeControllerImpl implements HomeController {

    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private final ProductService productService;

    public HomeControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public BaseView createBaseViewModel(String logo, String aboutUsInformation) {
        return new BaseView(logo, aboutUsInformation);
    }

    @Override
    @GetMapping("/home")
    public String homePage(Principal principal, Model model) {
        LOG.log(Level.INFO,"Home page request from "+ (principal != null ? principal.getName() : "not_authenticated"));
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);

        List<ProductDto> topSellingProducts = productService.getTopSelling();
        List<BestSellerView> bestsellers = topSellingProducts.stream()
                .map(productDto -> new BestSellerView(
                        new ProductView(
                                productDto.getId(),
                                productDto.getName(),
                                productDto.getPrice(),
                                "/images/products/" + productDto.getId() + ".jpg",
                                productDto.getCategoryId(),
                                productDto.getSalePrice(),
                                productDto.getQuantityProduct()
                        ),
                        baseView
                ))
                .toList();

        List<ProductDto> saleProductDTOs = productService.getSaleProducts();
        System.out.println(saleProductDTOs);
        List<ProductView> sales = saleProductDTOs.stream()
                .map(saleProductDTO -> new ProductView(
                        saleProductDTO.getId(),
                        saleProductDTO.getName(),
                        saleProductDTO.getPrice(),
                        "/images/products/" + saleProductDTO.getId() + ".jpg",
                        saleProductDTO.getCategoryId(),
                        saleProductDTO.getSalePrice(),
                        saleProductDTO.getQuantityProduct()

                ))
                .toList();

        System.out.println(sales);
        model.addAttribute("bestsellers",bestsellers);
        model.addAttribute("sales", sales);
        return "home";
    }
}
