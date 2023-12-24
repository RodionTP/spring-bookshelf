package org.example.springbookshelf.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbookshelf.dto.*;
import org.example.springbookshelf.mapper.BookMapper;
import org.example.springbookshelf.model.Book;
import org.example.springbookshelf.model.Category;
import org.example.springbookshelf.repository.CategoryRepository;
import org.example.springbookshelf.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<BookListResponse> findAll() {
        return ResponseEntity.ok(
                bookMapper.entityListToListResponse(
                        bookService.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.entityToResponse(
                bookService.findById(id)
        ));
    }

    @GetMapping("/find-by-title-and-author")
    public ResponseEntity<BookResponse> findBookByTitleAndAuthor(@RequestParam String title,
                                                                 @RequestParam String author) {
        Book foundBook = bookService.findBookByTitleAndAuthor(title, author);
        if (foundBook != null) {
            return ResponseEntity.ok(bookMapper.entityToResponse(foundBook));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/find-by-category")
    public ResponseEntity<BookListResponse> findBookByCategory(@RequestParam String category) {
        List<Book> booksInCategory = bookService.findBooksByCategory(category);
        if (!booksInCategory.isEmpty()) {
            return ResponseEntity.ok(bookMapper.entityListToListResponse(booksInCategory));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody UpsertBookRequest request) {
        Book newBook = bookService.save(bookMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookMapper.entityToResponse(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody UpsertBookRequest book) {
        Book bookUpdate = bookService.update(bookMapper.requestToEntity(id, book));
        return ResponseEntity.ok(bookMapper.entityToResponse(bookUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
