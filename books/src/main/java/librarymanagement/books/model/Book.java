package librarymanagement.books.model;
// File: src/main/java/librarymanagement/books/model/Book.java
// Library Management System - Book Entity

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @Size(max = 255, message = "Genre cannot be more than 255 characters")
    private String genre;

    @Column(name = "published_year")
    private int publishedYear;

    }

    
    
