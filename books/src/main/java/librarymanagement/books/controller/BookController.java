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
@Validated 
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
    @PatchMapping("/bulk")
    @Operation(summary = "Bulk update books", description = "Updates multiple books in a single operation")
    public ResponseEntity<ApiResponse<String>> bulkUpdateBooks(
            @RequestBody BulkUpdateRequest bulkRequest, WebRequest request) {

        int updatedCount = 0;
        for (Long bookId : bulkRequest.getBookIds()) {
            ApiResponse<Book> updateResponse = bookUpdateService.updateBook(bookId, bulkRequest.getUpdateRequest());
            if (updateResponse.isSuccess()) updatedCount++;
        }

        String message = String.format("Successfully updated %d out of %d books",
                updatedCount, bulkRequest.getBookIds().size());

        ApiResponse<String> response = ApiResponse.success(message, message);
        response.setPath(request.getContextPath() + "/api/books/bulk");
        response.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

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
