package mate.academy.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.model.order.Status;

public record OrderDto(
        Long id,
        Long userId, 
        Set<OrderItemDto> orderItems, 
        LocalDateTime orderDate,
        BigDecimal total,
        Status status) {

}
