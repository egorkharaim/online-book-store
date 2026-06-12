package mate.academy.mapper;

import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    
    @Mapping(target = "bookId", ignore = true)
    OrderItemDto toDto(OrderItem orderItem);
}

