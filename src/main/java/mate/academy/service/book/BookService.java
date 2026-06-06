package mate.academy.service.book;

import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
