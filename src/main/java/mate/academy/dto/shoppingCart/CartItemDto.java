package mate.academy.dto.shoppingCart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemDto(Long id,
        Long bookId,
        String bookTitle,
        int quantity) {

}
