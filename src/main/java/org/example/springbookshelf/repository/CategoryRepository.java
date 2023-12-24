package org.example.springbookshelf.repository;

import org.example.springbookshelf.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
