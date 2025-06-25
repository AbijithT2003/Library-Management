package librarymanagement.books.util;

import librarymanagement.books.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDefaultsHelper {

    public void applyDefaults(Book book) {
        if (book.getGenre() == null || book.getGenre().trim().isEmpty()) {
            book.setGenre("Unspecified");
        }
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(1);
        }
    }
}