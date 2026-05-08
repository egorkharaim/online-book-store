package mate.academy.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns) {

}
