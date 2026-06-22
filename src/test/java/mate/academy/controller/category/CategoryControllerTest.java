package mate.academy.controller.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.security.JwtAuthenticationFilter;
import mate.academy.service.book.BookService;
import mate.academy.service.category.CategoryService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Create category when request dto is valid")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_WithValidRequestDto_ReturnsCreatedCategoryDto() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Fantasy", "Fantasy books");

        CategoryDto expected = new CategoryDto(1L, requestDto.name(), requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        Assertions.assertNotNull(actual.id());
        boolean isEqual = EqualsBuilder.reflectionEquals(expected, actual, "id");
        Assertions.assertTrue(isEqual);
    }

    @Test
    @DisplayName("Get category by id when valid id is provided")
    @WithMockUser(roles = "USER")
    void getCategoryById_WithValidId_ReturnsCategoryDto() throws Exception {
        // Given
        Long categoryId = 1L;

        CategoryDto expected = new CategoryDto(categoryId, "Fantasy", "Fantasy books");

        when(categoryService.getById(categoryId)).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get books by category id when valid id is provided")
    @WithMockUser(roles = "USER")
    void getBooksByCategoryId_WithValidId_ReturnsBooks() throws Exception {
        // Given
        Long categoryId = 1L;

        BookDtoWithoutCategoryIds firstBook = new BookDtoWithoutCategoryIds(
                1L,
                "The Hobbit",
                "J.R.R. Tolkien",
                "978-0-26110-2",
                null,
                null,
                null
        );

        BookDtoWithoutCategoryIds secondBook = new BookDtoWithoutCategoryIds(
                2L,
                "Harry Potter",
                "J.K. Rowling",
                "978-0-74753-2",
                null,
                null,
                null
        );

        when(bookService.findAllByCategoryId(categoryId)).thenReturn(List.of(firstBook, secondBook));

        // When
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String actual = result.getResponse().getContentAsString();

        Assertions.assertTrue(actual.contains("The Hobbit"));
        Assertions.assertTrue(actual.contains("Harry Potter"));
    }
}
