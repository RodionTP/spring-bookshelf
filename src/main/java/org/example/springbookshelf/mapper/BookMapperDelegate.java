package org.example.springbookshelf.mapper;

import org.example.springbookshelf.dto.UpsertBookRequest;
import org.example.springbookshelf.model.Book;
import org.example.springbookshelf.model.Category;

public abstract class BookMapperDelegate implements BookMapper {

    @Override
    public Book requestToEntity(UpsertBookRequest request) {
        Book entity = new Book();
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        Category category = mapCategory(request.getCategory());
        category.setBook(entity);
        entity.setCategory(category);
        return entity;
    }

    @Override
    public Book requestToEntity(Long bookId, UpsertBookRequest request) {
        Book entity = requestToEntity(request);
        entity.setId(bookId);
        return entity;
    }
}
