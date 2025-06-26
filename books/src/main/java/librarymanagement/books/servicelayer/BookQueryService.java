package librarymanagement.books.servicelayer;

import librarymanagement.books.dto.ApiResponse;
import librarymanagement.books.dto.BookFilterRequest;
import librarymanagement.books.dto.PagedResponse;
import librarymanagement.books.model.Book;
import librarymanagement.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import librarymanagement.books.dto.BookStatisticsResponse;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookQueryService {

    private final BookRepository bookRepository;

    /**
     * Fetch books using filters or search, with pagination and sorting
     */
    public ApiResponse<PagedResponse<Book>> getFilteredBooks(BookFilterRequest filterRequest) {
        try {
            Pageable pageable = PageRequest.of(
                    filterRequest.getPage(),
                    filterRequest.getSize(),
                    createSort(filterRequest.getSortBy(), filterRequest.getSortDirection())
            );

            Page<Book> bookPage = (filterRequest.getSearch() != null && !filterRequest.getSearch().trim().isEmpty())
                    ? bookRepository.searchBooks(filterRequest.getSearch().trim(), pageable)
                    : bookRepository.findBooksWithFilters(
                            filterRequest.getTitle(),
                            filterRequest.getAuthor(),
                            filterRequest.getGenre(),
                            filterRequest.getYearFrom(),
                            filterRequest.getYearTo(),
                            filterRequest.getAvailableOnly(),
                            pageable
                    );

            PagedResponse<Book> pagedResponse = PagedResponse.<Book>builder()
                    .content(bookPage.getContent())
                    .totalElements(bookPage.getTotalElements())
                    .totalPages(bookPage.getTotalPages())
                    .pageInfo(PagedResponse.PageInfo.builder()
                            .page(bookPage.getNumber())
                            .size(bookPage.getSize())
                            .first(bookPage.isFirst())
                            .last(bookPage.isLast())
                            .hasNext(bookPage.hasNext())
                            .hasPrevious(bookPage.hasPrevious())
                            .build())
                    .build();

            return ApiResponse.success(pagedResponse, "Books retrieved successfully");
        } catch (Exception e) {
            log.error("Error retrieving filtered books: {}", e.getMessage());
            return ApiResponse.error("Failed to retrieve books: " + e.getMessage());
        }
    }

    /**
     * Sort builder with validation
     */
    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Basic validation
        String[] validFields = {
                "id", "title", "author", "genre", "publishedYear", "price",
                "availableCopies", "createdAt", "updatedAt"
        };

        boolean isValid = false;
        for (String field : validFields) {
            if (field.equalsIgnoreCase(sortBy)) {
                sortBy = field;
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            log.warn("Invalid sort field: {}. Defaulting to 'id'", sortBy);
            sortBy = "id";
        }

        return Sort.by(direction, sortBy);
    }
    public ApiResponse<Book> getBookById(Long id) {
    return bookRepository.findById(id)
            .map(book -> ApiResponse.success(book, "Book found"))
            .orElse(ApiResponse.error("Book with ID " + id + " not found"));
}
   public ApiResponse<BookStatisticsResponse> getBookStatistics() {
    long totalBooks = bookRepository.count();
    long availableBooks = bookRepository.countByAvailableCopiesGreaterThan(0);
    long outOfStockBooks = bookRepository.countByAvailableCopiesEquals(0);

    BookStatisticsResponse stats = BookStatisticsResponse.builder()
            .totalBooks(totalBooks)
            .availableBooks(availableBooks)
            .outOfStockBooks(outOfStockBooks)
            .build();

    return ApiResponse.success(stats, "Book statistics retrieved");
}
 
}
