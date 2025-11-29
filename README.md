# Library

A console-based software engineering project that simulates a library system with support for books, CDs, users, administrators, loans, reminders, and fines. The project is designed with clean separation of concerns and follows object-oriented principles and software engineering best practices.

## Features

### Admin Capabilities:
- Add books & CDs
- Search books (by title or author)
- Borrow books on behalf of users
- View all overdue items
- Mark fines as paid
- Register and unregister users
- Send overdue reminders (email + in-app memory storage)
- Display all books, CDs, and active loans

### User Capabilities:
- Login / Authentication
- Search books
- Borrow books & CDs
- View personal overdue items
- Pay fines
- View borrowing history

## Architecture

The system follows a layered architecture:

presentation → service → domain  
                       ↘ strategy

- domain/ — Core entity models (Book, CD, Loan, Admin, Media)
- service/ — Business logic (LoanService, BookService, UserService, ReminderService, CDService)
- strategy/ — Strategy Pattern for searching & sorting books
- presentation/ — CLI user interface (LibraryApp)

## Design Patterns Used

### Strategy Pattern
Used in book searching & sorting:

BookSearchStrategy:
- TitleSearchStrategy
- AuthorSearchStrategy

BookSortStrategy:
- SortByTitleStrategy
- SortByAuthorStrategy

This allows runtime switching of searching/sorting behavior without modifying core code.

### Dependency Injection
Used for email notification:

Notifier → EmailNotifier → EmailServer  
where EmailServer can be:  
- FakeEmailServer (test mode)  
- JakartaEmailServer (real SMTP Gmail sending)

## Testing Approach

- FakeEmailServer used for simulated message sending
- MemoryNotifier used for testing notification delivery
- Test coverage considered at:
  - Class level
  - Method level
  - Line coverage
  - Branch coverage

## Data Persistence

These files act as the storage layer of the system:
- books.txt
- cds.txt
- admins.txt
- users.txt
- loans.txt
- emails.log
- reminders.log

## How to Run

1. Open the project in IntelliJ or any Java IDE  
2. Run:

LibraryApp.main()

3. Choose user role (Admin or User)  
4. Follow on-screen instructions

## Sample Admin Accounts

Username: mohammad  
Password: 123  

Username: anan  
Password: 1234  

## Authors

- Mohammad Marei  
- Anan AbuKhader  

## Academic Details

Software Engineering Project  
An-Najah National University  
Faculty of Engineering  

## Future Improvements

- Replace text files with a database (MySQL or MongoDB)
- Add GUI or web-based interface
- Add password hashing for security
- Add permissions system and user roles
- Implement automated background reminder scheduling

## License

This project is for academic and educational purposes only.
