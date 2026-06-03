package mate.academy.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import mate.academy.model.Category;
import mate.academy.model.book.Book;
import mate.academy.repository.category.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring") 
public abstract class BookMapper {
    @Autowired
    protected CategoryRepository categoryRepository;

    public abstract BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    public abstract Book toModel(CreateBookRequestDto requestDto);

    public abstract BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateBookFromDto(CreateBookRequestDto requestDto,
            @MappingTarget Book book);

    @AfterMapping
    protected void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    @AfterMapping
    protected void setCategoriesToEntity(@MappingTarget Book book,
            CreateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>(categoryRepository
                    .findAllById(requestDto.getCategoryIds()));
            book.setCategories(categories);
        }
    }
}
