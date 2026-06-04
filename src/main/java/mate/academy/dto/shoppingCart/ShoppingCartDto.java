package mate.academy.dto.shoppingCart;

import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
    
}
