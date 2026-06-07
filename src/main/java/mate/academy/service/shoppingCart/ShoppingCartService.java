package mate.academy.service.shoppingcart;

import mate.academy.dto.shoppingcart.CartItemDto;
import mate.academy.dto.shoppingcart.CartItemRequestDto;
import mate.academy.dto.shoppingcart.CartItemUpdateDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getTheCart(Long userId);

    CartItemDto addCartItem(Long userId, CartItemRequestDto requestDto);

    CartItemDto updateCartItem(Long userId, Long cartItemId, CartItemUpdateDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);

}
