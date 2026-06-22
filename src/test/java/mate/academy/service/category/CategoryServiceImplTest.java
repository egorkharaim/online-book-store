package mate.academy.service.category;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Get category by id when category exists")
    void getById_WithExistingId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        CategoryDto expected = new CategoryDto(categoryId, "Fantasy", "Fantasy books");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.getById(categoryId);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throw exception when category does not exist")
    void getById_WithNonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 999L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );

        // Then
        Assertions.assertEquals(
                "Can't find category with id: " + categoryId,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Save category when request dto is valid")
    void save_WithValidRequestDto_ReturnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Fantasy", "Fantasy books");

        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Fantasy");
        savedCategory.setDescription("Fantasy books");

        CategoryDto expected = new CategoryDto(1L, "Fantasy", "Fantasy books");

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.save(requestDto);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category when category exists")
    void update_WithExistingId_ReturnsUpdatedCategoryDto() {
        // Given
        Long categoryId = 1L;

        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Updated Fantasy", "Updated fantasy books");

        Category categoryFromDb = new Category();
        categoryFromDb.setId(categoryId);
        categoryFromDb.setName("Fantasy");
        categoryFromDb.setDescription("Fantasy books");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Fantasy");
        updatedCategory.setDescription("Updated fantasy books");

        CategoryDto expected = new CategoryDto(
                categoryId,
                "Updated Fantasy",
                "Updated fantasy books"
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryFromDb));
        when(categoryRepository.save(categoryFromDb)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.update(categoryId, requestDto);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all categories when categories exist")
    void findAll_ReturnsPageOfCategoryDto() {
        // Given
        Category firstCategory = new Category();
        firstCategory.setId(1L);
        firstCategory.setName("Fantasy");
        firstCategory.setDescription("Fantasy books");

        Category secondCategory = new Category();
        secondCategory.setId(2L);
        secondCategory.setName("Science Fiction");
        secondCategory.setDescription("Science fiction books");

        CategoryDto firstCategoryDto = new CategoryDto(1L, "Fantasy", "Fantasy books");

        CategoryDto secondCategoryDto = new CategoryDto(
                2L,
                "Science Fiction",
                "Science fiction books"
        );

        Page<Category> categoryPage = new PageImpl<>(
                List.of(firstCategory, secondCategory),
                PageRequest.of(0, 10),
                2
        );

        when(categoryRepository.findAll(PageRequest.of(0, 10))).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        // When
        Page<CategoryDto> actual = categoryService.findAll(PageRequest.of(0, 10));

        // Then
        Assertions.assertEquals(2, actual.getTotalElements());
        Assertions.assertEquals(firstCategoryDto, actual.getContent().get(0));
        Assertions.assertEquals(secondCategoryDto, actual.getContent().get(1));
    }
}
