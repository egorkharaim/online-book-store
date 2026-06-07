package mate.academy.service.shoppingcart;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.CartItemDto;
import mate.academy.dto.shoppingcart.CartItemRequestDto;
import mate.academy.dto.shoppingcart.CartItemUpdateDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.shoppingcart.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getTheCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping Cart by User id: " + userId)));
    }

    @Override
    public CartItemDto addCartItem(Long userId, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping Cart by User id: " + userId));
        CartItem item = cartItemMapper.toModel(requestDto);
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(i -> i.getBook().getId().equals(requestDto.bookId()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItem itemToUpdate = existingCartItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() + requestDto.quantity());
            return cartItemMapper.toDto(cartItemRepository.save(itemToUpdate));
        }
        item.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }

    @Override
    public CartItemDto updateCartItem(Long userId, Long cartItemId, CartItemUpdateDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + cartItemId));
        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to modify this cart item");
        }
        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + cartItemId));

        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this cart item");
        }
        cartItemRepository.delete(cartItem);
    }

}
