package mate.academy.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import mate.academy.validation.book.Isbn;

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
    
    @NotEmpty
    private Set<Long> categoryIds;
}
