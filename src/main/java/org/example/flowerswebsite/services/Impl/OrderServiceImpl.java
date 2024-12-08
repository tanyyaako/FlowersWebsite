package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.OrderContentDTO;
import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.Entities.*;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Exceptions.QuantityLessException;
import org.example.flowerswebsite.Repositories.*;
import org.example.flowerswebsite.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository,
                            ModelMapper modelMapper, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        UserEntity userEntity = userRepository.findById(orderDto.getUserEntityId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + orderDto.getUserEntityId() + " not found"));
        OrderEntity orderEntity = new OrderEntity(userEntity);
        for (OrderContentDTO orderContentDTO : orderDto.getOrderContentDTOs()) {
            Long productId = orderContentDTO.getProductId();
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
            if(productEntity.getQuantityProduct()>orderContentDTO.getQuantity()){
                orderEntity.setOrderContent(productEntity, orderContentDTO.getQuantity());
            }
            else{
                throw new QuantityLessException("Product with id " + productId + " has less quantity than in order");
            }
        }
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        OrderDto savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDto.class);
        return savedOrderDTO;
    }

    @Override
    public OrderDto updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
        orderEntity.changeStatus(orderStatus);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        OrderDto savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDto.class);
        return savedOrderDTO;
    }
    @Override
    public OrderDto findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
        OrderDto orderDTO = modelMapper.map(orderEntity, OrderDto.class);
        return orderDTO;
    }
    @Override
    public List<OrderDto> findAllActive() {
        List<OrderEntity> activeOrders = orderRepository.findAllByStatus(OrderStatus.CREATED);
        List<OrderDto> activeOrdersDto = activeOrders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return activeOrdersDto;
    }


}
