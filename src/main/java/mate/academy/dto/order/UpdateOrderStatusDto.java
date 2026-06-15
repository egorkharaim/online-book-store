package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.model.order.Status;

public record UpdateOrderStatusDto(
        @NotNull(message = "Status cannot be null")
        Status status
) {}
