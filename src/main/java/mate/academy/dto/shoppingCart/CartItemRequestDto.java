package mate.academy.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull
        Long bookId,
        @Min(1)
        int quantity
) {
    
}
