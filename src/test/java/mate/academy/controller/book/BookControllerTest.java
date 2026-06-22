package mate.academy.controller.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.security.JwtAuthenticationFilter;
import mate.academy.service.book.BookService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Save book when request dto is valid")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void save_WithValidRequestDto_ReturnsCreatedBookDto() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The Hobbit");
        requestDto.setAuthor("J.R.R. Tolkien");
        requestDto.setIsbn("9783161484100");
        requestDto.setPrice(BigDecimal.valueOf(45.50));
        requestDto.setDescription("A great adventure");
        requestDto.setCoverImage("hobbit.jpg");
        requestDto.setCategoryIds(Set.of(1L));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual.getId());
        boolean isEqual = EqualsBuilder.reflectionEquals(expected, actual, "id", "categoryIds");
        Assertions.assertTrue(isEqual);
    }

    @Test
    @DisplayName("Get book by id when valid id is provided")
    @WithMockUser(roles = "USER")
    void getBookById_WithValidId_ReturnsBookDto() throws Exception {

        //Given
        Long bookId = 1L;
        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle("The Hobbit");
        expected.setAuthor("J.R.R. Tolkien");
        expected.setIsbn("978-0-26110-2");
        expected.setPrice(BigDecimal.valueOf(45.50));

        when(bookService.getBookById(bookId)).thenReturn(expected);

        //When
        MvcResult result = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete book by id when valid id is provided")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBookById_WithValidId_ReturnsNoContent() throws Exception {
        // Given
        Long bookId = 1L;

        // When
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());

        // Then
    }

    @Test
    @DisplayName("Get all books when books exist")
    @WithMockUser(roles = "USER")
    void getAll_WhenBooksExist_ReturnsPageOfBooks() throws Exception {
        // Given
        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("The Hobbit");
        firstBook.setAuthor("J.R.R. Tolkien");
        firstBook.setIsbn("978-0-26110-2");
        firstBook.setPrice(BigDecimal.valueOf(45.50));

        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("Dune");
        secondBook.setAuthor("Frank Herbert");
        secondBook.setIsbn("978-0-44117-2");
        secondBook.setPrice(BigDecimal.valueOf(55.00));

        Page<BookDto> booksPage = new PageImpl<>(
                List.of(firstBook, secondBook),
                PageRequest.of(0, 10),
                2
        );

        when(bookService.findAll(any())).thenReturn(booksPage);

        // When
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = root.get("content");
        BookDto[] actual = objectMapper.treeToValue(content, BookDto[].class);

        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(firstBook, actual[0]);
        Assertions.assertEquals(secondBook, actual[1]);
    }

    @Test
    @DisplayName("Search books when valid title is provided")
    @WithMockUser(roles = "USER")
    void searchBooks_WithValidTitle_ReturnsListOfBooks() throws Exception {
        // Given

        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("The Hobbit");
        firstBook.setAuthor("J.R.R. Tolkien");
        firstBook.setIsbn("978-0-26110-2");
        firstBook.setPrice(BigDecimal.valueOf(45.50));

        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("The Lord of the Rings");
        secondBook.setAuthor("J.R.R. Tolkien");
        secondBook.setIsbn("978-0-26110-3");
        secondBook.setPrice(BigDecimal.valueOf(60.00));

        // When
        when(bookService.search(any(BookSearchParametersDto.class)))
                .thenReturn(List.of(firstBook, secondBook));

        MvcResult result = mockMvc.perform(get("/books/search")
                .param("titles", "The Hobbit"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(firstBook, actual[0]));
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(secondBook, actual[1]));
    }

}
