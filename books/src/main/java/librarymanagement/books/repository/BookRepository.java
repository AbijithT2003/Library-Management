package librarymanagement.books.repository;

import librarymanagement.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    long countByAvailableCopiesGreaterThan(int count);
    long countByAvailableCopiesEquals(int count);


    // Custom query methods for filtering
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    List<Book> findByGenreContainingIgnoreCase(String genre);
    
    List<Book> findByPublishedYear(Integer year);
    
    List<Book> findByPublishedYearBetween(Integer startYear, Integer endYear);
    
    List<Book> findByAvailableCopiesGreaterThan(Integer copies);

     // Complex filtering 
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:genre IS NULL OR LOWER(COALESCE(b.genre, '')) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
           "(:yearFrom IS NULL OR b.publishedYear >= :yearFrom) AND " +
           "(:yearTo IS NULL OR b.publishedYear <= :yearTo) AND " +
           "(:availableOnly IS NULL OR :availableOnly = false OR b.availableCopies > 0)")
    
    Page<Book> findBooksWithFilters(
        @Param("title") String title,
        @Param("author") String author,
        @Param("genre") String genre,
        @Param("yearFrom") Integer yearFrom,
        @Param("yearTo") Integer yearTo,
        @Param("availableOnly") Boolean availableOnly,
        Pageable pageable
    );

    @Query("SELECT b FROM Book b WHERE (" +
       "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(COALESCE(b.genre, '')) LIKE LOWER(CONCAT('%', :searchTerm, '%')) )")
    Page<Book> searchBooks(
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );

    // Statistics queries
    @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies > 0")
    Long countAvailableBooks();

    @Query("SELECT b.genre, COUNT(b) FROM Book b WHERE b.genre IS NOT NULL GROUP BY b.genre ORDER BY COUNT(b) DESC")
    List<Object[]> getGenreStatistics();
}
