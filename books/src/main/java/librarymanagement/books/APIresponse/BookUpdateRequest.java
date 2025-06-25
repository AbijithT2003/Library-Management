package librarymanagement.books.APIresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookUpdateRequest {
    
    private String title;
    private String author;
    private String genre;
    private Integer publishedYear;
    private String isbn;
    private String description;
    private java.math.BigDecimal price;
    private Integer availableCopies;
    
    // Helper method to check if field should be updated
    public boolean hasTitle() { return title != null; }
    public boolean hasAuthor() { return author != null; }
    public boolean hasGenre() { return genre != null; }
    public boolean hasPublishedYear() { return publishedYear != null; }
    public boolean hasIsbn() { return isbn != null; }
    public boolean hasDescription() { return description != null; }
    public boolean hasPrice() { return price != null; }
    public boolean hasAvailableCopies() { return availableCopies != null; }
}
