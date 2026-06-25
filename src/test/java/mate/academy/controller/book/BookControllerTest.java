package mate.academy.controller.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.config.CustomMySqlContainer;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class BookControllerTest {

    @Container
    static CustomMySqlContainer mySqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get book by id when valid id is provided")
    @WithMockUser(roles = "USER")
    @Sql(
            scripts = "classpath:database/books/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getBookById_WithValidId_ReturnsBookDto() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("The Hobbit");
        expected.setAuthor("J.R.R. Tolkien");
        expected.setIsbn("978-0-26110-2");
        expected.setPrice(BigDecimal.valueOf(46));
        expected.setDescription("A great adventure");
        expected.setCoverImage("hobbit.jpg");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all books when books exist")
    @WithMockUser(roles = "USER")
    @Sql(
            scripts = "classpath:database/books/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_WhenBooksExist_ReturnsPageOfBooks() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = root.get("content");

        assertEquals(2, content.size());
        assertEquals("The Hobbit", content.get(0).get("title").asText());
        assertEquals("Dune", content.get(1).get("title").asText());
    }

    @Test
    @DisplayName("Search books when valid title is provided")
    @WithMockUser(roles = "USER")
    @Sql(
            scripts = "classpath:database/books/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void searchBooks_WithValidTitle_ReturnsListOfBooks() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/books/search")
                .param("titles", "The Hobbit"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                BookDto[].class
        );

        assertEquals(1, actual.length);
        assertEquals("The Hobbit", actual[0].getTitle());
    }

    @Test
    @DisplayName("Save book when request dto is valid")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void save_WithValidRequestDto_ReturnsCreatedBookDto() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The Silmarillion");
        requestDto.setAuthor("J.R.R. Tolkien");
        requestDto.setIsbn("9783161484100");
        requestDto.setPrice(new BigDecimal("65.00"));
        requestDto.setDescription("Mythopoeic stories");
        requestDto.setCoverImage("silmarillion.jpg");
        requestDto.setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                .with(csrf())
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        BookDto expected = new BookDto();
        expected.setTitle("The Silmarillion");
        expected.setAuthor("J.R.R. Tolkien");
        expected.setIsbn("9783161484100");
        expected.setPrice(new BigDecimal("65.00"));
        expected.setDescription("Mythopoeic stories");
        expected.setCoverImage("silmarillion.jpg");

        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id", "categoryIds"));
    }

    @Test
    @DisplayName("Delete book by id when valid id is provided")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void deleteBookById_WithValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
