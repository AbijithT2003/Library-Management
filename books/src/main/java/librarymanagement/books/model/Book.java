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
    @Size(min = 1, max = 200, message = "Title must be 1 to 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 100, message = "Author must be 1 to 100 characters")
    private String author;

    @Size(max = 255, message = "Genre cannot be more than 255 characters")
    private String genre;

    @Min(value = 1000, message = "Year must be after 1000")
    @Max(value = 2026, message = "Year cannot be in the future")
    @Column(name = "published_year")
    private int publishedYear;

    }

    
    
