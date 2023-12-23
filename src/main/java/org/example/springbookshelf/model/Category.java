package org.example.springbookshelf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "book_id")
    private Long id;
    private String name;
    @OneToOne
    @MapsId
    @JoinColumn(name = "book_id")
    private Book book;
}
