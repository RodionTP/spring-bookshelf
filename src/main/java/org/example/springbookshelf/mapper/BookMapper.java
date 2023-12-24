package org.example.springbookshelf.mapper;

import org.example.springbookshelf.dto.BookListResponse;
import org.example.springbookshelf.dto.BookResponse;
import org.example.springbookshelf.dto.UpsertBookRequest;
import org.example.springbookshelf.model.Book;
import org.example.springbookshelf.model.Category;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(BookMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(source = "request.category", target = "category", qualifiedByName = "mapCategory")
    Book requestToEntity(UpsertBookRequest request);

    @Mappings({
            @Mapping(source = "bookId", target = "id"),
            @Mapping(source = "request.category", target = "category", qualifiedByName = "mapCategory")
    })
    Book requestToEntity(Long bookId, UpsertBookRequest request);

    BookResponse entityToResponse(Book book);

    default BookListResponse entityListToListResponse(List<Book> books) {
        BookListResponse response = new BookListResponse();
        response.setBooks(books.stream()
                .map(this::entityToResponse).collect(Collectors.toList()));
        return response;
    }

    @Named("mapCategory")
    default Category mapCategory(String value) {
        Category category = new Category();
        category.setName(value);
        return category;
    }
}
