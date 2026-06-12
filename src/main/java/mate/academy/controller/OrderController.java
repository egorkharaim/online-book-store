package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderStatusDto;
import mate.academy.model.user.User;
import mate.academy.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing user orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order",
                 description = "Place a new order using items from the user's shopping cart")
    public OrderDto placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.placeOrder(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history", 
                description = "Retrieve the order history for the currently logged-in user")
    public List<OrderDto> getOrderHistory(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status",     
                description = "Update the status of a specific order (Admin only)")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusDto statusDto) {
        return orderService.updateStatus(id, statusDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items",     
                description = "Retrieve all items for a specific order")
    public List<OrderItemDto> getOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            Pageable pageable) {
        return orderService.getOrderItems(user.getId(), orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a specific order item", 
                description = "Retrieve a specific item from a specific order")
    public OrderItemDto getOrderItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(user.getId(), orderId, itemId);
    }
}
