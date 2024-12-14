package org.example.flowerswebsite.services.Impl;

import jakarta.transaction.Transactional;
import org.example.flowerswebsite.DTO.OrderContentDTO;
import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.Entities.*;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Exceptions.QuantityLessException;
import org.example.flowerswebsite.Repositories.*;
import org.example.flowerswebsite.services.CartService;
import org.example.flowerswebsite.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository,
                            ModelMapper modelMapper, ProductRepository productRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public void createOrder(String userId) {
        Map<String, Long> cart = cartService.getCart(userId);
        if(cart == null || cart.isEmpty()){
            throw new IllegalArgumentException("Корзина пуста");
        }
        UserEntity userEntity = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + Long.valueOf(userId) + " not found"));
        OrderEntity orderEntity = new OrderEntity(userEntity);
        for (Map.Entry<String,Long> entry : cart.entrySet()) {
            Long productId = Long.valueOf(entry.getKey());
            Long quantity = entry.getValue();
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
            if(productEntity.getQuantityProduct()>=quantity){
                orderEntity.setOrderContent(productEntity, quantity);
            }
            else{
                throw new QuantityLessException("Product with id " + productId + " has less quantity than in order");
            }
            productEntity.setQuantityProduct(productEntity.getQuantityProduct()-quantity);
            productRepository.save(productEntity);
        }
        orderRepository.save(orderEntity);
        cartService.clearCart(userId);
    }
    @Override
    public OrderDto findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
        OrderDto orderDTO = modelMapper.map(orderEntity, OrderDto.class);
        return orderDTO;
    }

    public List<OrderDto> findAll(){
        List<OrderEntity> orderEntities = orderRepository.findAll();
        List<OrderDto> orderDtos = orderEntities.stream()
                .map(orderEntity -> modelMapper.map(orderEntity,OrderDto.class))
                .toList();
        return orderDtos;
    }
}
