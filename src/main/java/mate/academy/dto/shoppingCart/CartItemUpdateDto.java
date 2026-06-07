package mate.academy.dto.shoppingcart;

import jakarta.validation.constraints.Min;

public record CartItemUpdateDto(
        @Min(1) int quantity) {

}
