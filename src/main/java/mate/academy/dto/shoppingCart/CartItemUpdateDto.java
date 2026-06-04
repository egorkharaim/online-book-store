package mate.academy.dto.shoppingCart;

import jakarta.validation.constraints.Min;

public record CartItemUpdateDto(
        @Min(1) int quantity) {

}
