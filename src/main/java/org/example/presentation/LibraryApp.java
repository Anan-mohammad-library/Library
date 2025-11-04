package org.example.presentation;

import org.example.service.*;
import java.util.Scanner;

public class LibraryApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        AdminService adminService = new AdminService();
        UserService userService = new UserService();
        BookService bookService = new BookService();
        CDService cdService = new CDService();
        LoanService loanService = new LoanService();

        MemoryNotifier notifier = new MemoryNotifier();
        ReminderService reminderService = new ReminderService(notifier, loanService, userService);

        System.out.println("Welcome to the Library Management System!");

        while (true) {
            System.out.println("\nWho are you?");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int roleChoice;
            try { roleChoice = Integer.parseInt(input.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Enter a number 1–3."); continue; }

            switch (roleChoice) {
                case 1 -> adminMenu(input, adminService, bookService, loanService, userService, cdService, reminderService);
                case 2 -> userMenu(input, loanService, bookService, cdService, userService, notifier);
                case 3 -> { System.out.println("Exiting..."); return; }
                default -> System.out.println("Invalid choice. Try again!");
            }
        }
    }

    private static void adminMenu(Scanner input, AdminService adminService, BookService bookService,
                                  LoanService loanService, UserService userService, CDService cdService,
                                  ReminderService reminderService) {

        System.out.print("Admin Username: "); String username = input.nextLine();
        System.out.print("Admin Password: "); String password = input.nextLine();

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
                    System.out.print("Search keyword: "); String keyword = input.nextLine();
                    bookService.search(keyword).forEach(System.out::println);
                    System.out.println("Search completed.");
                }
                case 3 -> {
                    System.out.print("Borrower (user): "); String borrower = input.nextLine();
                    System.out.print("Book title: "); String bookTitle = input.nextLine();
                    loanService.borrowBook(borrower, bookTitle);
                    System.out.println("Book borrowing process completed.");
                }
                case 4 -> {
                    loanService.showAllOverdues();
                    System.out.println("All overdue loans displayed.");
                }
                case 5 -> {
                    System.out.print("Borrower: "); String borrower = input.nextLine();
                    loanService.payFine(borrower);
                    System.out.println("Fine payment processed.");
                }
                case 6 -> {
                    System.out.print("New Username: "); String u = input.nextLine();
                    System.out.print("Password: "); String pw = input.nextLine();
                    System.out.print("Email: "); String email = input.nextLine();
                    userService.register(u, pw, email);
                    System.out.println("User registration completed.");
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
                    System.out.println("CD added successfully.");
                }
                case 9 -> {
                    System.out.print("Enter the email of the user to send reminder to: ");
                    String targetEmail = input.nextLine();
                    reminderService.sendOverdueReminderTo(targetEmail);
                    System.out.println("Overdue reminder sent.");
                }
                case 10 -> {
                    System.out.println("All Books:");
                    bookService.getAllBooks().forEach(System.out::println);
                    System.out.println("All CDs:");
                    cdService.getAllCDs().forEach(System.out::println);
                    System.out.println("All Loans:");
                    loanService.showAllLoans();
                    System.out.println("Displaying all books, CDs, and loans.");
                }
                case 11 -> { adminService.logout(); System.out.println("Admin logged out."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void userMenu(Scanner input, LoanService loanService, BookService bookService,
                                 CDService cdService, UserService userService, MemoryNotifier notifier) {

        System.out.print("Username: "); String username = input.nextLine();
        System.out.print("Password: "); String password = input.nextLine();

        if (!userService.login(username, password)) {
            System.out.println("Invalid username or password!");
            return;
        }

        var messages = notifier.getMessages(username);
        if (!messages.isEmpty()) {
            System.out.println("\nYou have reminders:");
            messages.forEach(System.out::println);
            notifier.clearMessages(username);
        }

        while (true) {
            System.out.println("\nUSER MENU");
            System.out.println("1. Search Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Show My Overdues");
            System.out.println("4. Pay Fine");
            System.out.println("5. Search CD");
            System.out.println("6. Borrow CD");
            System.out.println("7. Show My Loans");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            int choice;
            try { choice = Integer.parseInt(input.nextLine()); }
            catch (NumberFormatException e) { continue; }

            switch (choice) {
                case 1 -> {
                    System.out.print("Keyword: "); String keyword = input.nextLine();
                    bookService.search(keyword).forEach(System.out::println);
                    System.out.println("Book search completed.");
                }
                case 2 -> {
                    System.out.print("Book title: "); String bookTitle = input.nextLine();
                    loanService.borrowBook(username, bookTitle);
                    System.out.println("Book borrowing process completed.");
                }
                case 3 -> {
                    loanService.showUserOverdues(username);
                    System.out.println("Your overdue loans are displayed.");
                }
                case 4 -> {
                    loanService.payFine(username);
                    System.out.println("Fine payment processed.");
                }
                case 5 -> {
                    System.out.print("CD title: "); String cdKeyword = input.nextLine();
                    cdService.search(cdKeyword).forEach(System.out::println);
                    System.out.println("CD search completed.");
                }
                case 6 -> {
                    System.out.print("CD title: "); String cdTitle = input.nextLine();
                    loanService.borrowCD(username, cdTitle);
                    System.out.println("CD borrowing process completed.");
                }
                case 7 -> {
                    loanService.showUserLoans(username);
                    System.out.println("Your current loans are displayed.");
                }
                case 8 -> { System.out.println("Logged out successfully."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
