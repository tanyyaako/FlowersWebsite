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
    public void addToCart(String userId, String productId, Integer quantity){
        String key="cart:" + userId;
        Map<String, Long> cart = getCart(userId);
        if (cart == null) {
            cart = new HashMap<>();
        }
        long incr = (quantity == null) ? 1 : quantity;
        cart.put(productId, cart.getOrDefault(productId, 0L) + quantity);
        redisService.save(key, cart);
    }

    public  void clearCart(String userId){
        String key="cart:" + userId;
        redisService.remove(key);
    }
    public void decreaseQuantity(String userId, String productId) {
        String key = "cart:" + userId;
        Map<String, Long> cart = getCart(userId);

        if (cart != null && cart.containsKey(productId)) {
            Long currentQuantity = cart.get(productId);
            if (currentQuantity > 1) {
                cart.put(productId, currentQuantity - 1);
            } else {
                cart.remove(productId);
            }
            redisService.save(key, cart);
        }
    }
}
