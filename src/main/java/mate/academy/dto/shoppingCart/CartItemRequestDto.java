package mate.academy.dto.shoppingCart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
    @NotNull
        Long bookId,
        @Min(1)
        int quantity
) {
    
}
