// File: src/main/java/librarymanagement/controller/BookController.java
package librarymanagement.books.controller;

import librarymanagement.books.model.Book;
import librarymanagement.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*") // Allows frontend to access backend from anywhere (for dev)

@Tag(name = "Book Management", description = "Operations related to book management in the library")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Create a new book with validation
    @Operation(summary = "Create a new book", description = "Adds a new book to the library")
    @PostMapping
    public ResponseEntity<String> createBook(@Valid @RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.ok("✅ Book added successfully!");
    }

    // Read all books
    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the library") 
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Operation(summary = "Get a book by ID", description = "Retrieves a book by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
            .map(book -> ResponseEntity.ok(book))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


        // Update an existing book with validation
    @Operation(summary = "Update a book", description = "Updates an existing book in the library")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setGenre(updatedBook.getGenre());
            book.setPublishedYear(updatedBook.getPublishedYear());
            bookRepository.save(book);
            return ResponseEntity.ok("✅ Book updated successfully (ID: " + id + ")");
        }).orElseGet(() -> {
            updatedBook.setId(id);
            bookRepository.save(updatedBook);
            return ResponseEntity.status(201).body("Book not found, so a new book was created (ID: " + id + ")");
        });
    }

    // Delete a book
    @Operation(summary = "Delete a book", description = "Deletes a book from the library")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(404).body("❌ Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
        return ResponseEntity.ok("✅ Book deleted successfully (ID: " + id + ")");
    }

}
