package mate.academy.dto;

import java.util.Set;

public record BookSearchParametersDto(
        Set<String> titles,
        Set<String> authors,
        Set<String> isbns) {

}
