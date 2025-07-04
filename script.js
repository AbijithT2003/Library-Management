const API_BASE = 'http://localhost:8080/api/books';

document.addEventListener('DOMContentLoaded', () => {
    searchBooks();
    document.getElementById('addBookForm').addEventListener('submit', handleAddBook);
    document.getElementById('editBookForm').addEventListener('submit', handleEditBook);
});

function showBooksSection() {
    document.getElementById('statistics').style.display = 'none';
    document.getElementById('books').scrollIntoView({ behavior: 'smooth' });
    searchBooks();
}

function showStatistics() {
    fetch(`${API_BASE}/statistics`)
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById('statistics').style.display = 'block';
                document.getElementById('totalBooks').textContent = data.data.totalBooks;
                document.getElementById('availableBooks').textContent = data.data.availableBooks;
                document.getElementById('outOfStockBooks').textContent = data.data.outOfStockBooks;
                document.getElementById('statistics').scrollIntoView({ behavior: 'smooth' });
            }
        });
}

function searchBooks(page = 0) {
    const filters = {
        title: document.getElementById('titleFilter').value,
        author: document.getElementById('authorFilter').value,
        genre: document.getElementById('genreFilter').value,
        yearFrom: document.getElementById('yearFromFilter').value,
        yearTo: document.getElementById('yearToFilter').value,
        availableOnly: document.getElementById('availableOnlyFilter').checked,
        page,
        size: 6,
        sortBy: 'title',
        sortDirection: 'asc'
    };

    const query = new URLSearchParams(filters).toString();

    fetch(`${API_BASE}?${query}`)
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                renderBooks(data.data.content);
                renderPagination(data.data.pageNumber, data.data.totalPages);
            } else {
                document.getElementById('booksGrid').innerHTML = '<p>No books found.</p>';
            }
        });
}

function renderBooks(books) {
    const grid = document.getElementById('booksGrid');
    grid.innerHTML = books.map(book => `
        <div class="book-card">
            <h3>${book.title}</h3>
            <div class="author">${book.author}</div>
            <div class="genre">${book.genre}</div>
            <div class="details">
                <span>Year: ${book.publishedYear || '-'}</span>
                <span>Available: ${book.availableCopies || 0}</span>
            </div>
            <div class="actions">
                <button class="edit-btn" onclick="openEditBookModal(${book.id})">Edit</button>
                <button class="delete-btn" onclick="deleteBook(${book.id})">Delete</button>
            </div>
        </div>
    `).join('');
}

function renderPagination(current, total) {
    const pagination = document.getElementById('pagination');
    if (total <= 1) return pagination.innerHTML = '';
    
    let buttons = '';
    for (let i = 0; i < total; i++) {
        buttons += `<button onclick="searchBooks(${i})" ${i === current ? 'disabled' : ''}>${i + 1}</button>`;
    }
    pagination.innerHTML = buttons;
}

function clearSearch() {
    ['titleFilter', 'authorFilter', 'genreFilter', 'yearFromFilter', 'yearToFilter'].forEach(id => {
        document.getElementById(id).value = '';
    });
    document.getElementById('availableOnlyFilter').checked = false;
    searchBooks();
}

// ===== Add Book =====
function handleAddBook(e) {
    e.preventDefault();
    const book = getFormData('addBookForm');
    fetch(API_BASE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(book)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            closeAddBookModal();
            searchBooks();
        } else {
            alert('Failed to add book');
        }
    });
}

function openEditBookModal(id) {
    fetch(`${API_BASE}/${id}`)
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                const book = data.data;
                document.getElementById('editBookId').value = book.id;
                document.getElementById('editBookTitle').value = book.title;
                document.getElementById('editBookAuthor').value = book.author;
                document.getElementById('editBookGenre').value = book.genre;
                document.getElementById('editBookYear').value = book.publishedYear;
                document.getElementById('editBookPrice').value = book.price;
                document.getElementById('editBookCopies').value = book.availableCopies;
                document.getElementById('editBookModal').style.display = 'block';
            }
        });
}

function handleEditBook(e) {
    e.preventDefault();
    const id = document.getElementById('editBookId').value;
    const book = getFormData('editBookForm');
    fetch(`${API_BASE}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(book)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            closeEditBookModal();
            searchBooks();
        } else {
            alert('Failed to update book');
        }
    });
}

function deleteBook(id) {
    if (!confirm('Are you sure you want to delete this book?')) return;

    fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                searchBooks();
            } else {
                alert('Failed to delete book');
            }
        });
}

// ===== Modal Control =====
function closeAddBookModal() {
    document.getElementById('addBookModal').style.display = 'none';
    document.getElementById('addBookForm').reset();
}

function closeEditBookModal() {
    document.getElementById('editBookModal').style.display = 'none';
    document.getElementById('editBookForm').reset();
}

function showAddBookModal() {
    document.getElementById('addBookModal').style.display = 'block';
}

// ===== Utility =====
function getFormData(formId) {
    const form = document.getElementById(formId);
    const data = {};
    [...form.elements].forEach(el => {
        if (el.name) data[el.name] = el.type === 'checkbox' ? el.checked : el.value;
    });
    return data;
}

// Dummy login buttons
function adminLogin() {
    alert("Redirecting to Admin login...");
}

function userLogin() {
    alert("Redirecting to User login...");
}
