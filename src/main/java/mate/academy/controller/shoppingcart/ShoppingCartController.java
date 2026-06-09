package mate.academy.controller.shoppingcart;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.CartItemDto;
import mate.academy.dto.shoppingcart.CartItemRequestDto;
import mate.academy.dto.shoppingcart.CartItemUpdateDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.user.User;
import mate.academy.service.shoppingcart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing Shopping Cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getCart(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CartItemDto addCartItem(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(user.getId(), requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto requestDto) {
        return shoppingCartService.updateCartItem(user.getId(), cartItemId, requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(user.getId(), cartItemId);
    }
}
