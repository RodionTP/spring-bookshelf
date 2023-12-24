package org.example.springbookshelf.service;

import org.example.springbookshelf.model.Book;

import java.util.List;

public interface BookService {
    Book findBookByTitleAndAuthor(String title, String author);
    List<Book> findBooksByCategory(String category);
    List<Book> findAll();
    Book findById(Long id);
    Book save(Book book);
    Book update(Book book);
    void delete(Long id);
}
