package mate.academy.service.shoppingcart;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.CartItemDto;
import mate.academy.dto.shoppingcart.CartItemRequestDto;
import mate.academy.dto.shoppingcart.CartItemUpdateDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.ShoppingCartDoesNotBelongToUserException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.book.Book;
import mate.academy.model.user.User;
import mate.academy.repository.book.BookRepository;
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
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                    "Can't find shopping Cart by User id: " + userId)));
    }

    @Override
    public CartItemDto addCartItem(Long userId, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart for user: " + userId));

        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id: " + requestDto.bookId()));

        Optional<CartItem> existingCartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId());

        if (existingCartItem.isPresent()) {
            CartItem itemToUpdate = existingCartItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() + requestDto.quantity());
            return cartItemMapper.toDto(cartItemRepository.save(itemToUpdate));
        }

        CartItem newItem = new CartItem();
        newItem.setShoppingCart(shoppingCart);
        newItem.setBook(book);
        newItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(newItem));
    }

    @Override
    public CartItemDto updateCartItem(Long userId, Long cartItemId, CartItemUpdateDto requestDto) {
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new ShoppingCartDoesNotBelongToUserException(
                        "User " + userId + " tried to access cart item " + cartItemId
                                + " which does not belong to them"));

        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new ShoppingCartDoesNotBelongToUserException(
                        "User " + userId + " tried to delete cart item " + cartItemId
                                + " which does not belong to them"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);
    }
}
