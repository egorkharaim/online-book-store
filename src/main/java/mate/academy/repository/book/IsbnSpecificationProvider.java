package mate.academy.repository.book;

import java.util.Arrays;
import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_NAME = "isbn";

    @Override
    public String getKey() {
        return FIELD_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(getKey()).in(Arrays.asList(params));
    }

}
