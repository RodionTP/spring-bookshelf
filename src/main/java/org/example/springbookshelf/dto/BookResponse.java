package org.example.springbookshelf.dto;

import lombok.Data;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private CategoryResponse category;
}
