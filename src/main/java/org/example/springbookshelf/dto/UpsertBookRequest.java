package org.example.springbookshelf.dto;

import lombok.Data;

@Data
public class UpsertBookRequest {
    private String title;
    private String author;
    private String category;
}
