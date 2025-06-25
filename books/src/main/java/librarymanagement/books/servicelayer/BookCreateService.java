package librarymanagement.books.service;

import librarymanagement.books.model.Book;
import librarymanagement.books.repository.BookRepository;
import librarymanagement.books.util.BookDefaultsHelper;
import librarymanagement.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookCreateService {

    private final BookRepository bookRepository;
    private final BookDefaultsHelper defaultsHelper;

    public ApiResponse<Book> createBook(Book book) {
        try {
            defaultsHelper.applyDefaults(book);
            Book savedBook = bookRepository.save(book);
            log.info("Book created successfully with ID: {}", savedBook.getId());
            return ApiResponse.success(savedBook, "Book created successfully");
        } catch (Exception e) {
            log.error("Error creating book: {}", e.getMessage());
            return ApiResponse.error("Failed to create book: " + e.getMessage());
        }
    }
}
