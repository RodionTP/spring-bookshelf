package org.example.springbookshelf.repository;

import org.example.springbookshelf.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book findBookByTitleAndAuthor(String title, String author);

    List<Book> findBookByCategoryName(String category);
}
