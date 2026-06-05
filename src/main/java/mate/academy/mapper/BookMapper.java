package mate.academy.mapper;

import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import mate.academy.model.book.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring") 
public abstract class BookMapper {

    public abstract BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    public abstract Book toModel(CreateBookRequestDto requestDto);

    public abstract BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateBookFromDto(CreateBookRequestDto requestDto,
            @MappingTarget Book book);
}
