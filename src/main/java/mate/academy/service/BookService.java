package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
