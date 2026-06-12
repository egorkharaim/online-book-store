package mate.academy.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderStatusDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.order.Order;
import mate.academy.model.order.OrderItem;
import mate.academy.model.order.Status;
import mate.academy.repository.order.OrderItemRepository;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.shoppingcart.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(this::toOrderDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find order item with id: " + itemId + " in order: " + orderId));
        if (!orderItem.getOrder().getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("You don't have access to this order item");
        }
        return toOrderItemDto(orderItem);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long userId, Long orderId, Pageable pageable) {
        Order order = getOrderForUser(userId, orderId);
        return orderItemRepository.findAllByOrderId(order.getId(), pageable).stream()
                .map(this::toOrderItemDto)
                .toList();
    }

    @Override
    public OrderDto placeOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find shopping cart for user with id: " + userId));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty shopping cart");
        }

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);

            BigDecimal itemTotal = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        shoppingCart.getCartItems().clear();

        return toOrderDto(savedOrder);
    }

    @Override
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusDto statusDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find order with id: " + orderId));

        order.setStatus(statusDto.status());

        return toOrderDto(order);
    }

    private Order getOrderForUser(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find order with id: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("You don't have access to order with id: " + orderId);
        }
        return order;
    }

    private OrderDto toOrderDto(Order order) {
        OrderDto basicDto = orderMapper.toDto(order);

        Set<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toSet());

        return new OrderDto(
                basicDto.id(),
                order.getUser().getId(),
                orderItemDtos,
                basicDto.orderDate(),
                basicDto.total(),
                basicDto.status()
        );
    }

    private OrderItemDto toOrderItemDto(OrderItem orderItem) {
        OrderItemDto basicDto = orderItemMapper.toDto(orderItem);

        return new OrderItemDto(
                basicDto.id(),
                orderItem.getBook().getId(),
                basicDto.quantity()
        );
    }

}
