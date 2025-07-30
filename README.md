# 📚 Library Management System (not deployed)

A robust Spring Boot REST API for managing books in a library system. This microservice provides comprehensive book management functionality with advanced filtering, pagination, and statistics capabilities.

## ✨ Features

- **📖 Complete CRUD Operations** - Create, read, update, and delete books
- **🔍 Advanced Search & Filtering** - Search by title, author, genre, publication year, and availability
- **📄 Pagination & Sorting** - Efficient data retrieval with customizable page sizes and sorting options
- **📊 Statistics Dashboard** - Get insights into your book collection
- **✅ Data Validation** - Comprehensive input validation with meaningful error messages
- **🏗️ Clean Architecture** - Well-structured service layers following best practices
- **📝 API Documentation** - Swagger/OpenAPI integration for easy API exploration
- **🔄 Transaction Management** - Reliable data consistency with Spring transactions

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **Lombok** - Reducing boilerplate code
- **Swagger/OpenAPI 3** - API documentation
- **MySQL/PostgreSQL** - Database support
- **Maven** - Dependency management

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Database (MySQL/PostgreSQL for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/library-management-books.git
   cd library-management-books
   ```

2. **Configure the database**
   
   Update `application.properties` or `application.yml`:
   ```properties
   # For Database 
   # spring.datasource.url=jdbc:mysql://localhost:3306/library_db
   # spring.datasource.username=your_username
   # spring.datasource.password=your_password
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   - API Base URL: `http://localhost:8080/api/books`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📡 API Endpoints

### Books Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/books` | Create a new book |
| `GET` | `/api/books` | Get all books with filtering & pagination |
| `GET` | `/api/books/{id}` | Get book by ID |
| `PUT` | `/api/books/{id}` | Update an existing book |
| `DELETE` | `/api/books/{id}` | Delete a book |
| `GET` | `/api/books/statistics` | Get book collection statistics |

### Sample Requests

#### Create a Book
```json
POST /api/books
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "genre": "Classic Literature",
  "publishedYear": 1925,
  "availableCopies": 5
}
```

#### Filter Books with Pagination
```bash
GET /api/books?author=Fitzgerald&availableOnly=true&page=0&size=10&sortBy=title&sortDirection=asc
```

#### Update a Book
```json
PUT /api/books/1
{
  "title": "The Great Gatsby - Updated Edition",
  "availableCopies": 3
}
```

## 🔍 Advanced Filtering

The API supports comprehensive filtering options:

- **`title`** - Filter by book title (case-insensitive partial match)
- **`author`** - Filter by author name (case-insensitive partial match)
- **`genre`** - Filter by genre (case-insensitive partial match)
- **`yearFrom`** - Filter books published from this year
- **`yearTo`** - Filter books published up to this year
- **`availableOnly`** - Show only books with available copies
- **`search`** - Global search across title, author, and genre

### Pagination & Sorting

- **`page`** - Page number (0-based, default: 0)
- **`size`** - Page size (default: 10)
- **`sortBy`** - Sort field (id, title, author, genre, publishedYear, availableCopies, createdAt, updatedAt)
- **`sortDirection`** - Sort direction (asc/desc, default: asc)

## 📊 Response Format

All API responses follow a consistent structure:

```json
{
  "success": true,
  "message": "Books retrieved successfully",
  "data": {
    "content": [...],
    "pageInfo": {
      "page": 0,
      "size": 10,
      "first": true,
      "last": false,
      "hasNext": true,
      "hasPrevious": false
    },
    "totalElements": 25,
    "totalPages": 3
  },
  "timestamp": "2024-01-15 10:30:45",
  "path": "/api/books"
}
```

##  Project Structure

```
src/main/java/librarymanagement/books/
├── BooksApplication.java                 
├── controller/
│   └── BookController.java              
├── dto/
│   ├── ApiResponse.java                 
│   ├── BookFilterRequest.java           
│   ├── BookStatisticsResponse.java      
│   ├── BookUpdateRequest.java           
│   └── PagedResponse.java               
├── exception/
│   └── GlobalExceptionHandler.java      
├── model/
│   └── Book.java                        
├── repository/
│   └── BookRepository.java              
└── servicelayer/
    ├── BookCreateService.java           
    ├── BookDefaultsHelper.java          
    ├── BookMapper.java                  
    ├── BookQueryService.java            
    └── BookUpdateService.java           
```

## 📋 Book Entity Schema

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | Auto-generated | Unique identifier |
| `title` | String | Required, 1-200 chars | Book title |
| `author` | String | Required, 1-100 chars | Author name |
| `genre` | String | Optional, max 255 chars | Book genre |
| `publishedYear` | Integer | 1000-2026 | Publication year |
| `availableCopies` | Integer | Min 0 | Available copies count |
| `createdAt` | LocalDateTime | Auto-generated | Creation timestamp |
| `updatedAt` | LocalDateTime | Auto-updated | Last update timestamp |


##  Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Swagger configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Logging
logging.level.librarymanagement.books=DEBUG
```
##  API Documentation

Once the application is running, you can explore the full API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
