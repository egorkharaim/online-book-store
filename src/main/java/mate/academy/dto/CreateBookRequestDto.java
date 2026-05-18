package mate.academy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import mate.academy.validation.Isbn;

@Data
public class CreateBookRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    @Isbn
    private String isbn;

    @Min(0)
    @NotNull
    private BigDecimal price;

    private String description;

    private String coverImage;
}
