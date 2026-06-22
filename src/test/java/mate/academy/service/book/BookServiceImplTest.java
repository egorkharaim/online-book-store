package mate.academy.service.book;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Category;
import mate.academy.model.book.Book;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.book.BookSpecificationBuilder;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Get book by id when book exists")
    void getBookById_WithExistingId_ReturnsBookDto() {
        // Given
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setIsbn("978-0-26110-2");
        book.setPrice(BigDecimal.valueOf(45.50));

        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle("The Hobbit");
        expected.setAuthor("J.R.R. Tolkien");
        expected.setIsbn("978-0-26110-2");
        expected.setPrice(BigDecimal.valueOf(45.50));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.getBookById(bookId);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throw exception when book does not exist")
    void getBookById_WithNonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long bookId = 999L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookById(bookId)
        );

        // Then
        Assertions.assertEquals("Can't find book by id: " + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("Save book when request dto is valid")
    void save_WithValidRequestDto_ReturnsBookDto() {
        // Given
        Long categoryId = 1L;

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The Hobbit");
        requestDto.setAuthor("J.R.R. Tolkien");
        requestDto.setIsbn("978-0-26110-2");
        requestDto.setPrice(BigDecimal.valueOf(45.50));
        requestDto.setDescription("A great adventure");
        requestDto.setCoverImage("hobbit.jpg");
        requestDto.setCategoryIds(Set.of(categoryId));

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");

        Book book = new Book();
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setIsbn("978-0-26110-2");
        book.setPrice(BigDecimal.valueOf(45.50));
        book.setDescription("A great adventure");
        book.setCoverImage("hobbit.jpg");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("The Hobbit");
        savedBook.setAuthor("J.R.R. Tolkien");
        savedBook.setIsbn("978-0-26110-2");
        savedBook.setPrice(BigDecimal.valueOf(45.50));
        savedBook.setDescription("A great adventure");
        savedBook.setCoverImage("hobbit.jpg");
        savedBook.setCategories(Set.of(category));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("The Hobbit");
        expected.setAuthor("J.R.R. Tolkien");
        expected.setIsbn("978-0-26110-2");
        expected.setPrice(BigDecimal.valueOf(45.50));
        expected.setDescription("A great adventure");
        expected.setCoverImage("hobbit.jpg");

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expected);

        // When
        BookDto actual = bookService.save(requestDto);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update book when book exists")
    void update_WithExistingId_ReturnsUpdatedBookDto() {
        // Given
        Long bookId = 1L;
        Long categoryId = 2L;

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated title");
        requestDto.setAuthor("Updated author");
        requestDto.setIsbn("978-0-26110-2");
        requestDto.setPrice(BigDecimal.valueOf(55.00));
        requestDto.setDescription("Updated description");
        requestDto.setCoverImage("updated.jpg");
        requestDto.setCategoryIds(Set.of(categoryId));

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Science Fiction");

        Book bookFromDb = new Book();
        bookFromDb.setId(bookId);
        bookFromDb.setTitle("Old title");
        bookFromDb.setAuthor("Old author");
        bookFromDb.setIsbn("978-0-26110-2");
        bookFromDb.setPrice(BigDecimal.valueOf(45.50));
        bookFromDb.setDescription("Old description");
        bookFromDb.setCoverImage("old.jpg");

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated title");
        updatedBook.setAuthor("Updated author");
        updatedBook.setIsbn("978-0-26110-2");
        updatedBook.setPrice(BigDecimal.valueOf(55.00));
        updatedBook.setDescription("Updated description");
        updatedBook.setCoverImage("updated.jpg");
        updatedBook.setCategories(Set.of(category));

        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle("Updated title");
        expected.setAuthor("Updated author");
        expected.setIsbn("978-0-26110-2");
        expected.setPrice(BigDecimal.valueOf(55.00));
        expected.setDescription("Updated description");
        expected.setCoverImage("updated.jpg");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookFromDb));
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookRepository.save(bookFromDb)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);

        // When
        BookDto actual = bookService.update(bookId, requestDto);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throw exception when updating non-existing book")
    void update_WithNonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long bookId = 999L;

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated title");
        requestDto.setAuthor("Updated author");
        requestDto.setIsbn("978-0-26110-2");
        requestDto.setPrice(BigDecimal.valueOf(55.00));
        requestDto.setDescription("Updated description");
        requestDto.setCoverImage("updated.jpg");
        requestDto.setCategoryIds(Set.of(1L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(bookId, requestDto)
        );

        // Then
        Assertions.assertEquals("Can't find a book by id:" + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("Find all books by category id when category exists")
    void findAllByCategoryId_WithExistingCategoryId_ReturnsBooksWithoutCategoryIds() {
        // Given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("The Hobbit");
        firstBook.setAuthor("J.R.R. Tolkien");
        firstBook.setIsbn("978-0-26110-2");
        firstBook.setCategories(Set.of(category));

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Harry Potter");
        secondBook.setAuthor("J.K. Rowling");
        secondBook.setIsbn("978-0-74753-2");
        secondBook.setCategories(Set.of(category));

        BookDtoWithoutCategoryIds firstBookDto = new BookDtoWithoutCategoryIds(
                1L,
                "The Hobbit",
                "J.R.R. Tolkien",
                "978-0-26110-2",
                null,
                null,
                null
        );

        BookDtoWithoutCategoryIds secondBookDto = new BookDtoWithoutCategoryIds(
                2L,
                "Harry Potter",
                "J.K. Rowling",
                "978-0-74753-2",
                null,
                null,
                null
        );

        when(bookRepository.findAllByCategoriesId(categoryId))
                .thenReturn(List.of(firstBook, secondBook));
        when(bookMapper.toDtoWithoutCategories(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDtoWithoutCategories(secondBook)).thenReturn(secondBookDto);

        // When
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        // Then
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(firstBookDto, actual.get(0));
        Assertions.assertEquals(secondBookDto, actual.get(1));
    }
}
