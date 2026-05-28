package mate.academy.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.model.book.Book;
import mate.academy.repository.specification.SpecificationBuilder;
import mate.academy.repository.specification.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";
    private static final String ISBN_KEY = "isbn";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.allOf();

        if (searchParameters.authors() != null && !searchParameters.authors().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.titles() != null && !searchParameters.titles().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.isbns() != null && !searchParameters.isbns().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbns()));
        }

        return spec;

    }

}
