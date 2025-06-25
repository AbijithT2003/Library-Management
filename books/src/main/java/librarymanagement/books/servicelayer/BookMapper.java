package librarymanagement.books.servicelayer;

import librarymanagement.books.dto.BookUpdateRequest;
import librarymanagement.books.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public void applyUpdate(Book book, BookUpdateRequest req) {
        if (req.hasTitle()) book.setTitle(req.getTitle());
        if (req.hasAuthor()) book.setAuthor(req.getAuthor());
        if (req.hasGenre()) {
            book.setGenre(defaultIfBlank(req.getGenre(), "Unspecified"));
        }
        if (req.hasPublishedYear()) book.setPublishedYear(req.getPublishedYear());
        if (req.hasAvailableCopies()) {
            Integer copies = req.getAvailableCopies();
            book.setAvailableCopies(copies != null && copies >= 0 ? copies : 0);
        }
    }

    private String defaultIfBlank(String val, String defaultVal) {
        return (val == null || val.trim().isEmpty()) ? defaultVal : val;
    }
}
