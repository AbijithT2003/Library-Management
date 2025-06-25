package librarymanagement.books.APIresponse;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;

// ...existing code...
// Book filter request DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFilterRequest {
    
    private String title;
    private String author;
    private String genre;
    private Integer yearFrom;
    private Integer yearTo;
    private java.math.BigDecimal minPrice;
    private java.math.BigDecimal maxPrice;
    private Boolean availableOnly;
    private String search;
    
    // Pagination parameters
    @Builder.Default
    private int page = 0;
    
    @Builder.Default
    private int size = 10;
    
    @Builder.Default
    private String sortBy = "id";
    
    @Builder.Default
    private String sortDirection = "asc";
}
