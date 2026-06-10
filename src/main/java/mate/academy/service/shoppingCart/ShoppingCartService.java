package mate.academy.service.shoppingcart;

import mate.academy.dto.shoppingcart.CartItemRequestDto;
import mate.academy.dto.shoppingcart.CartItemUpdateDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.user.User;

public interface ShoppingCartService {
    ShoppingCartDto getCart(Long userId);

    ShoppingCartDto addCartItem(Long userId, CartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long userId, Long cartItemId, CartItemUpdateDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);

    void createShoppingCartForUser(User user);
}
