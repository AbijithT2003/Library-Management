// File: src/main/java/librarymanagement/controller/BookController.java
package librarymanagement.books.controller;

import librarymanagement.books.model.Book;
import librarymanagement.books.repository.BookRepository;
import librarymanagement.books.servicelayer.BookCreateService;
import librarymanagement.books.servicelayer.BookUpdateService;
import librarymanagement.books.servicelayer.BookQueryService;
import librarymanagement.books.dto.ApiResponse;
import librarymanagement.books.dto.PagedResponse;
import librarymanagement.books.dto.BookFilterRequest;
import librarymanagement.books.dto.BookUpdateRequest;
import librarymanagement.books.dto.BookStatisticsResponse;
import jakarta.validation.constraints.Min;
import org.springframework.web.context.request.WebRequest;
import librarymanagement.books.controller.BookController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*") // Allows frontend to access backend from anywhere (for dev)

@Tag(name = "Book Management", description = "Operations related to book management in the library")
@RestController
@Valid
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookRepository bookRepository;
    private final BookCreateService bookCreateService;
    private final BookUpdateService bookUpdateService;
    private final BookQueryService bookQueryService;

    // ========== Create Book ==========
    @Operation(summary = "Create a new book", description = "Adds a new book to the library")
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@Valid @RequestBody Book book, WebRequest request) {
        log.info("Creating new book: {}", book.getTitle());
        ApiResponse<Book> response = bookCreateService.createBook(book);
        response.setPath(request.getContextPath() + "/api/books");
        return response.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.badRequest().body(response);
    }

    // ========== Get All Books (Filter + Pagination) ==========
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves books with filtering, pagination, and sorting options")
    public ResponseEntity<ApiResponse<PagedResponse<Book>>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            WebRequest request) {

        BookFilterRequest filterRequest = BookFilterRequest.builder()
                .title(title).author(author).genre(genre)
                .yearFrom(yearFrom).yearTo(yearTo)
                .availableOnly(availableOnly).search(search)
                .page(page).size(size).sortBy(sortBy).sortDirection(sortDirection)
                .build();

        ApiResponse<PagedResponse<Book>> response = bookQueryService.getAllBooks(filterRequest);
        response.setPath(request.getContextPath() + "/api/books");

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    // ========== Get Book by ID ==========
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its unique identifier")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id, WebRequest request) {
        log.info("Fetching book with ID: {}", id);
        ApiResponse<Book> response = bookQueryService.getBookById(id);
        response.setPath(request.getContextPath() + "/api/books/" + id);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ==========  Update Book ==========
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book's details")

    public ResponseEntity<ApiResponse<Book>> updateBook(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody BookUpdateRequest updateRequest,
            WebRequest request) {

        ApiResponse<Book> response = bookUpdateService.updateBook(id, updateRequest);
        response.setPath(request.getContextPath() + "/api/books/" + id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    // ==========  Delete Book ==========
    @DeleteMapping("/{id}")
    @Operation(summary="Delete book",description="Deletes a book from the library")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable @Min(1) Long id, WebRequest request) {
        ApiResponse<Void> response = bookUpdateService.deleteBook(id);
        response.setPath(request.getContextPath() + "/api/books/" + id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

     // ========== Bulk Update ==========
  

    // ========== Statistics ==========
    @GetMapping("/statistics")
    @Operation(summary = "Get book statistics", description = "Retrieves various statistics about the book collection")
    public ResponseEntity<ApiResponse<BookStatisticsResponse>> getBookStatistics(WebRequest request) {
        ApiResponse<BookStatisticsResponse> response = bookQueryService.getBookStatistics();
        response.setPath(request.getContextPath() + "/api/books/statistics");
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
   
    //====ADD new feature if something comes to mind====
    // possible additions: 
    //

}
