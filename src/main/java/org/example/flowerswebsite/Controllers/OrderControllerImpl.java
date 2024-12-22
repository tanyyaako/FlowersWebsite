package org.example.flowerswebsite.Controllers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.Order.OrderController;
import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Entities.UserEntity;
import org.example.flowerswebsite.Repositories.ProductRepository;
import org.example.flowerswebsite.services.CartService;
import org.example.flowerswebsite.services.Impl.AuthService;
import org.example.flowerswebsite.services.Impl.OrderServiceImpl;
import org.example.flowerswebsite.services.Impl.ProductServiceImpl;
import org.example.flowerswebsite.services.OrderService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.BasketAndLikeItModel.CartItemView;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final AuthService authService;
    private final ProductServiceImpl productServiceImpl;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    public OrderControllerImpl(OrderService orderService, CartService cartService, AuthService authService, ProductServiceImpl productServiceImpl) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.authService = authService;
        this.productServiceImpl = productServiceImpl;
    }

    @Override
    public BaseView createBaseViewModel(String logo, String aboutUsInformation) {
        return new BaseView(logo, aboutUsInformation);
    }

    @Override
    @GetMapping("/list")
    public String showOrders(Principal principal, Model model) {
        List<OrderDto> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        LOG.log(Level.INFO,"GET/order/list request from "+ principal.getName());
        return "ordersList.html";
    }

    @Override
    @GetMapping("/cart")
    public String showCart(@AuthenticationPrincipal User user, Model model) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        String username = user.getUsername();
        UserEntity userEntity = authService.getUser(username);
        Object cart = cartService.getCart(String.valueOf(userEntity.getId()));
        List<CartItemView> itemsView = new ArrayList<>();
        Double totalPrice=0D;
        if (cart==null){
            model.addAttribute("message", "Корзина пуста");
        }else{
            Map<String,Long> items = (Map<String,Long>) cart;
            for(Map.Entry<String,Long> entry : items.entrySet()){
                Long productId = Long.valueOf(entry.getKey());
                Long quantity = entry.getValue();
                ProductDto productDto= productServiceImpl.getById(productId);
                if(productDto.getSalePrice()!=null && productDto.getSalePrice()!=0){
                    totalPrice+=quantity*productDto.getSalePrice();
                    itemsView.add(new CartItemView(String.valueOf(productId),productDto.getName(),quantity,productDto.getSalePrice()));
                }else{
                    totalPrice+=quantity*productDto.getPrice();
                    itemsView.add(new CartItemView(String.valueOf(productId),productDto.getName(),quantity,productDto.getPrice()));
                }
            }
        }

        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("cartItems",itemsView);
        LOG.log(Level.INFO,"GET/order/cart request from "+ user.getUsername());
        return "cart.html";
    }

    @Override
    @PostMapping("/cart/add")
    public String addCart(@AuthenticationPrincipal User user,
                          @RequestParam  Long productId,@RequestParam(required = false) Integer quantity,
                          Model model, @RequestParam(required = false)   String categoryType,
                          @RequestParam(required = false) String returnUrl) {
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        String username = user.getUsername();
        UserEntity userEntity = authService.getUser(username);
        cartService.addToCart(String.valueOf(userEntity.getId()), String.valueOf(productId), quantity);
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        if (categoryType == null || categoryType.isEmpty()) {
            return "redirect:/order/cart?";
        }
        LOG.log(Level.INFO,"POST/order/cart/add request from "+ user.getUsername()+ "for product with id: "+productId);
        return "redirect:/product/catalog?categoryType=" + categoryType;
    }

    @Override
    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal User user) {
        String username = user.getUsername();
        UserEntity userEntity = authService.getUser(username);
        cartService.clearCart(String.valueOf(userEntity.getId()));
        LOG.log(Level.INFO,"GET/order/clear request from "+ user.getUsername());

        return "redirect:/order/cart?";
    }
    @Override
    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user) {
        String username = user.getUsername();
        UserEntity userEntity = authService.getUser(username);
        String userId= String.valueOf(userEntity.getId());
        LOG.log(Level.INFO,"POST/order/checkout request from "+ user.getUsername());
        try {
            System.out.println("created");
            orderService.createOrder(userId);
            return "succesOrder.html";
        } catch (Exception e) {
            System.out.println("not created"+ e.getMessage());
            return "redirect:/cart?";
        }
    }
    @Override
    @PostMapping("/cart/decrease")
    public String decreaseQuantity(@AuthenticationPrincipal User user, @RequestParam Long productId,Model model){
        String username = user.getUsername();
        UserEntity userEntity = authService.getUser(username);
        String userId= String.valueOf(userEntity.getId());
        cartService.decreaseQuantity(userId, String.valueOf(productId));
        LOG.log(Level.INFO,"POST/order/cart/decrease request from "+ user.getUsername()+ "for product with id: "+productId);
        return "redirect:/order/cart?";
    }
}
