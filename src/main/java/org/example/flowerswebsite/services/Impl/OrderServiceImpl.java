package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.OrderContentDTO;
import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.Entities.*;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.OrderRepository;
import org.example.flowerswebsite.Repositories.ProductRepository;
import org.example.flowerswebsite.Repositories.StorageRepository;
import org.example.flowerswebsite.Repositories.UserRepository;
import org.example.flowerswebsite.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final StorageRepository storageRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public OrderServiceImpl(UserRepository userRepository, StorageRepository storageRepository,
                            OrderRepository orderRepository, ModelMapper modelMapper, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.storageRepository = storageRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(OrderDto orderDto) {
        UserEntity userEntity = userRepository.findById(orderDto.getUserEntityId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + orderDto.getUserEntityId() + " not found"));
        StorageEntity storage = storageRepository.findById(orderDto.getStorageId())
                .orElseThrow(() -> new EntityNotFoundException("Storage with id " + orderDto.getStorageId() + " not found"));
        OrderEntity orderEntity = new OrderEntity(storage,userEntity);
        for (OrderContentDTO orderContentDTO : orderDto.getOrderContentDTOs()) {
            Long productId = orderContentDTO.getProductId();
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
            orderEntity.setOrderContent(productEntity, orderContentDTO.getQuantity());
        }
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        OrderDto savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDto.class);
        ResponseEntity<OrderDto> responseEntity = ResponseEntity.ok().body(savedOrderDTO);
        return responseEntity;
    }

    @Override
    public ResponseEntity<OrderDto> updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
        orderEntity.changeStatus(orderStatus);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        OrderDto savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDto.class);
        ResponseEntity<OrderDto> responseEntity = ResponseEntity.ok().body(savedOrderDTO);
        return responseEntity;
    }
    @Override
    public ResponseEntity<OrderDto> findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
        OrderDto orderDTO = modelMapper.map(orderEntity, OrderDto.class);
        return ResponseEntity.ok().body(orderDTO);
    }
    @Override
    public ResponseEntity<List<OrderDto>> findAllActive() {
        List<OrderEntity> activeOrders = orderRepository.findAllByStatus(OrderStatus.CREATED);
        List<OrderDto> activeOrdersDto = activeOrders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(activeOrdersDto);
    }


}
