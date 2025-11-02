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
        ReminderService reminderService = new ReminderService(notifier, loanService);

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
                }
                case 2 -> {
                    System.out.print("Search keyword: "); String keyword = input.nextLine();
                    bookService.search(keyword).forEach(System.out::println);
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
                    userService.register(u, pw);
                }
                case 7 -> {
                    System.out.print("Username to remove: "); String u = input.nextLine();
                    userService.unregister(u, loanService);
                }
                case 8 -> {
                    System.out.print("CD Title: "); String t = input.nextLine();
                    System.out.print("Artist: "); String a = input.nextLine();
                    System.out.print("CD ID: "); String id = input.nextLine();
                    cdService.addCD(t, a, id);
                }
                case 9 -> reminderService.sendOverdueReminders();
                case 10 -> {
                    System.out.println("All Books:");
                    bookService.getAllBooks().forEach(System.out::println);
                    System.out.println("All CDs:");
                    cdService.getAllCDs().forEach(System.out::println);
                    System.out.println("All Loans:");
                    loanService.showAllLoans();
                }
                case 11 -> { adminService.logout(); System.out.println("Logged out."); return; }
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
                }
                case 2 -> {
                    System.out.print("Book title: "); String bookTitle = input.nextLine();
                    loanService.borrowBook(username, bookTitle);
                }
                case 3 -> loanService.showUserOverdues(username);
                case 4 -> loanService.payFine(username);
                case 5 -> {
                    System.out.print("CD title: "); String cdKeyword = input.nextLine();
                    cdService.search(cdKeyword).forEach(System.out::println);
                }
                case 6 -> {
                    System.out.print("CD title: "); String cdTitle = input.nextLine();
                    loanService.borrowCD(username, cdTitle);
                }
                case 7 -> loanService.showUserLoans(username);
                case 8 -> { System.out.println("Logged out."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
