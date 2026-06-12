package mate.academy.service.order;

import java.util.List;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto placeOrder(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    OrderDto updateStatus(Long orderId, UpdateOrderStatusDto statusDto);

    List<OrderItemDto> getOrderItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Long userId, Long orderId, Long itemId);
}
