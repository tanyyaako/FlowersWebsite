package org.example.flowerswebsite.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private final RedisService redisService;

    public CartService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Map<String, Long> getCart(String userId){
        return redisService.get("cart:" + userId);
    }
    public void addToCart(String userId, String productId, int quantity){
        String key="cart:" + userId;
        Map<String, Long> cart = getCart(userId);
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(productId, cart.getOrDefault(productId, 0L) + quantity);
        redisService.save(key, cart);
    }

    public  void clearCart(String userId){
        String key="cart:" + userId;
        redisService.remove(key);
    }
}
