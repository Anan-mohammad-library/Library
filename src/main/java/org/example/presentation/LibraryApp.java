package org.example.presentation;
import org.example.service.*;
import org.example.domain.Book;
import org.example.strategy.*;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
public class LibraryApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        AdminService adminService = new AdminService();
        UserService userService = new UserService();
        BookService bookService = new BookService();
        CDService cdService = new CDService();
        LoanService loanService = new LoanService();
        EmailServer emailServer = new JakartaEmailServer();
        Notifier notifier = new EmailNotifier(emailServer);

        ReminderService reminderService = new ReminderService(notifier, loanService, userService);

        System.out.println("Welcome to the Library Management System!");

        while (true) {
            System.out.println("\nWho are you?");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int roleChoice;
            try {
                roleChoice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number 1–3.");
                continue;
            }

            switch (roleChoice) {
                case 1 -> adminMenu(input, adminService, bookService, loanService, userService, cdService, reminderService, notifier);
                case 2 -> userMenu(input, loanService, bookService, cdService, userService, notifier);
                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again!");
            }
        }
    }
    private static void adminMenu(Scanner input, AdminService adminService, BookService bookService,
                                  LoanService loanService, UserService userService, CDService cdService,
                                  ReminderService reminderService, Notifier notifier) {

        System.out.print("Admin Username: ");
        String username = input.nextLine();
        System.out.print("Admin Password: ");
        String password = input.nextLine();

        if (!adminService.login(username, password)) {
            System.out.println("Invalid username or password!");
            return;
        }

        System.out.println("Admin logged in successfully!");

        while (true) {
            System.out.println("\nADMIN MENU");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. Borrow Book (as user)");
            System.out.println("4. Show All Overdues");
            System.out.println("5. Pay Fine");
            System.out.println("6. Register User");
            System.out.println("7. Unregister User");
            System.out.println("8. Add CD");
            System.out.println("9. Send Overdue Reminders");
            System.out.println("10. Show All Books, CDs and Loans");
            System.out.println("11. Logout");
            System.out.print("Enter your choice: ");

            int choice;
            try { choice = Integer.parseInt(input.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); continue; }

            switch (choice) {
                case 1 -> {
                    System.out.print("Book title: "); String t = input.nextLine();
                    System.out.print("Author: "); String a = input.nextLine();
                    System.out.print("ISBN: "); String isbn = input.nextLine();
                    bookService.addBook(t, a, isbn);
                    System.out.println("Book added successfully.");
                }

                case 2 -> {
                    System.out.println("Choose search strategy: 1-By Title, 2-By Author");
                    int searchChoice;
                    try { searchChoice = Integer.parseInt(input.nextLine()); }
                    catch (NumberFormatException e) { searchChoice = 1; }

                    switch (searchChoice) {
                        case 2 -> bookService.setSearchStrategy(new AuthorSearchStrategy());
                        default -> bookService.setSearchStrategy(new TitleSearchStrategy());
                    }

                    System.out.print("Enter keyword: ");
                    String keyword = input.nextLine();
                    List<Book> results = bookService.search(keyword);
                    if (results.isEmpty()) System.out.println("No books found.");
                    else results.forEach(System.out::println);
                }

                case 3 -> {
                    System.out.print("Borrower (user): "); String borrower = input.nextLine();
                    System.out.print("Book title: "); String bookTitle = input.nextLine();
                    loanService.borrowBook(borrower, bookTitle);
                }

                case 4 -> loanService.showAllOverdues();

                case 5 -> {
                    System.out.print("Borrower: "); String borrower = input.nextLine();
                    loanService.payFine(borrower);
                }

                case 6 -> {
                    System.out.print("New Username: "); String u = input.nextLine();
                    System.out.print("Password: "); String pw = input.nextLine();
                    System.out.print("Email: "); String email = input.nextLine();
                    if(userService.register(u, pw, email)) System.out.println("User registration completed.");
                    else System.out.println("User already exists!");
                }

                case 7 -> {
                    System.out.print("Username to remove: "); String u = input.nextLine();
                    userService.unregister(u, loanService);
                    System.out.println("User removed successfully.");
                }

                case 8 -> {
                    System.out.print("CD Title: "); String t = input.nextLine();
                    System.out.print("Artist: "); String a = input.nextLine();
                    System.out.print("CD ID: "); String id = input.nextLine();
                    cdService.addCD(t, a, id);
                }

                case 9 -> sendReminders(input, userService, loanService, reminderService, notifier);

                case 10 -> showAllLibrary(input, bookService, cdService, loanService);

                case 11 -> { adminService.logout(); System.out.println("Admin logged out."); return; }

                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void userMenu(Scanner input, LoanService loanService, BookService bookService,
                                 CDService cdService, UserService userService, Notifier notifier) {

        System.out.print("Username: "); String username = input.nextLine();
        System.out.print("Password: "); String password = input.nextLine();

        if(!userService.login(username, password)) { System.out.println("Invalid username or password!"); return; }
        System.out.println("Logged in successfully!");

        while(true) {
            System.out.println("\nUSER MENU");
            System.out.println("1. Search Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Show My Overdues");
            System.out.println("4. Pay Fine");
            System.out.println("5. Search CD");
            System.out.println("6. Borrow CD");
            System.out.println("7. Show My Loans");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try { choice = Integer.parseInt(input.nextLine()); } catch (NumberFormatException e) { continue; }

            switch(choice) {
                case 1 -> {
                    System.out.println("Choose search strategy: 1-Title, 2-Author");
                    int searchChoice;
                    try { searchChoice = Integer.parseInt(input.nextLine()); } catch (NumberFormatException e) { searchChoice = 1; }
                    switch (searchChoice) {
                        case 2 -> bookService.setSearchStrategy(new AuthorSearchStrategy());
                        default -> bookService.setSearchStrategy(new TitleSearchStrategy());
                    }
                    System.out.print("Enter keyword: "); String keyword = input.nextLine();
                    var results = bookService.search(keyword);
                    if(results.isEmpty()) System.out.println("No books found.");
                    else results.forEach(System.out::println);
                }

                case 2 -> { System.out.print("Book title: "); String bookTitle = input.nextLine(); loanService.borrowBook(username, bookTitle); }
                case 3 -> loanService.showUserOverdues(username);
                case 4 -> loanService.payFine(username);
                case 5 -> { System.out.print("CD title: "); String cdKeyword = input.nextLine(); cdService.search(cdKeyword).forEach(System.out::println); }
                case 6 -> { System.out.print("CD title: "); String cdTitle = input.nextLine(); loanService.borrowCD(username, cdTitle); }
                case 7 -> loanService.showUserLoans(username);
                case 8 -> { System.out.println("Logged out successfully."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void sendReminders(Scanner input, UserService userService, LoanService loanService, ReminderService reminderService, Notifier notifier) {
        loanService.refreshOverdues();
        var allLoans = loanService.getAllLoans();
        Set<String> overdueUsers = new HashSet<>();
        for (var loan : allLoans) if(!loan.isReturned() && loan.getFine() > 0) overdueUsers.add(loan.getBorrower());

        if(overdueUsers.isEmpty()) { System.out.println("No users have overdue items."); return; }

        System.out.println("Users with overdue items:"); overdueUsers.forEach(System.out::println);
        System.out.print("Enter username to send email: "); String target = input.nextLine();

        if(!overdueUsers.contains(target)) { System.out.println("User not in overdue list."); return; }

        String email = userService.getEmail(target);
        if(email == null) System.out.println("No email available.");
        else {
            String message = reminderService.createOverdueMessageFor(target);
            notifier.notify(email, message);
            System.out.println(" Reminder sent to: " + target + " (" + email + ")");
        }
    }

    private static void showAllLibrary(Scanner input, BookService bookService, CDService cdService, LoanService loanService) {
        System.out.println("Sort books? 1-Title, 2-Author, 3-No sort");
        int sortChoice;
        try { sortChoice = Integer.parseInt(input.nextLine()); } catch (NumberFormatException e) { sortChoice = 3; }

        switch (sortChoice) {
            case 1 -> bookService.setSortStrategy(new SortByTitleStrategy());
            case 2 -> bookService.setSortStrategy(new SortByAuthorStrategy());
            default -> bookService.setSortStrategy(null);
        }

        System.out.println("All Books:");
        List<Book> booksToShow = (bookService.getSortStrategy() != null) ? bookService.sort() : bookService.getAllBooks();
        booksToShow.forEach(System.out::println);

        System.out.println("All CDs:");
        cdService.getAllCDs().forEach(System.out::println);

        System.out.println("All Loans:");
        loanService.showAllLoans();
    }
}
