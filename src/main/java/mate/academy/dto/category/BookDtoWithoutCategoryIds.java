package mate.academy.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import mate.academy.validation.book.Isbn;

public record BookDtoWithoutCategoryIds(
        Long id,
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Isbn String isbn,
        @NotNull @Min(0) BigDecimal price, // Аннотации стоят ПЕРЕД типом
        String description,
        String coverImage
) {
}
