package mate.academy.dto.book;

import java.util.Set;

public record BookSearchParametersDto(
        Set<String> titles,
        Set<String> authors,
        Set<String> isbns) {

}
