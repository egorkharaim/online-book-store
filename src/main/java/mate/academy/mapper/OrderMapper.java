package mate.academy.mapper;

import mate.academy.dto.order.OrderDto;
import mate.academy.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    OrderDto toDto(Order order);
}

