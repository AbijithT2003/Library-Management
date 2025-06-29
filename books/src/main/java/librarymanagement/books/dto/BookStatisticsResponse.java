package librarymanagement.books.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Statistics response DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookStatisticsResponse {
    
    private long totalBooks;
    private long availableBooks;
    private long uniqueAuthors;
    private long outOfStockBooks;
    private long totalGenres;
    private List<GenreCount> genreStatistics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreCount {
        private String genre;
        private long count;
    }
}
