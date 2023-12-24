package org.example.springbookshelf.service.impl;

import lombok.AllArgsConstructor;
import org.example.springbookshelf.configuration.properties.AppCacheProperties;
import org.example.springbookshelf.exception.EntityNotFoundException;
import org.example.springbookshelf.model.Book;
import org.example.springbookshelf.model.Category;
import org.example.springbookshelf.repository.BookRepository;
import org.example.springbookshelf.repository.CategoryRepository;
import org.example.springbookshelf.service.BookService;
import org.example.springbookshelf.utils.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@AllArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class DatabaseBookService implements BookService {

    private final BookRepository repository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.BY_TITLE_AND_AUTHOR, key = "#title + #author")
    @Override
    public Book findBookByTitleAndAuthor(String title, String author) {
        return repository.findBookByTitleAndAuthor(title, author);
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.BY_CATEGORY, key = "#category")
    @Override
    public List<Book> findBooksByCategory(String category) {
        return repository.findBookByCategoryName(category);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Книга с ID {0} не найдена!", id
                )));
    }

    @Override
    @CacheEvict(value = AppCacheProperties.CacheNames.BY_CATEGORY, key = "#book.category.name")
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.BY_CATEGORY, key = "#book.category.name"),
            @CacheEvict(value = AppCacheProperties.CacheNames.BY_TITLE_AND_AUTHOR, key = "#book.title+#book.author")
    })
    public Book update(Book book) {
        Book existedBook = findById(book.getId());
        Category category = categoryRepository.findById(existedBook.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Категория с ID {0} не найдена!", existedBook.getCategory().getId()
                )));

        redisTemplate.delete(AppCacheProperties.CacheNames.BY_CATEGORY + "::" + existedBook.getCategory().getName());
        redisTemplate.delete(AppCacheProperties.CacheNames.BY_TITLE_AND_AUTHOR + "::" + existedBook.getTitle() + existedBook.getAuthor());

        category.setName(book.getCategory().getName());
        book.setCategory(category);
        BeanUtils.copyNonNullProperties(book, existedBook);
        return repository.save(existedBook);
    }

    @Override
    @CacheEvict(value = {AppCacheProperties.CacheNames.BY_CATEGORY,
            AppCacheProperties.CacheNames.BY_TITLE_AND_AUTHOR}, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
