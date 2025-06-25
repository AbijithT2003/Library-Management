package librarymanagement.books.servicelayer;

import librarymanagement.books.dto.BookUpdateRequest;
import librarymanagement.books.model.Book;
import librarymanagement.books.repository.BookRepository;
import librarymanagement.books.servicelayer.BookMapper;
import librarymanagement.books.dto.ApiResponse;
import librarymanagement.books.exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookUpdateService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public ApiResponse<Book> updateBook(Long id, BookUpdateRequest updateRequest) {
        try {
            Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

            bookMapper.applyUpdate(book, updateRequest);
            Book updatedBook = bookRepository.save(book);

            log.info("Book updated successfully with ID: {}", id);
            return ApiResponse.success(updatedBook, "Book updated successfully");
        } catch (Exception e) {
            log.error("Failed to update book: {}", e.getMessage());
            return ApiResponse.error("Failed to update book: " + e.getMessage());
        }
    }
}
