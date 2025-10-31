package org.example.presentation;

import org.example.service.AdminService;
import org.example.service.BookService;
import org.example.service.LoanService;
import org.example.service.UserService;
import org.example.service.EmailNotifier;
import org.example.service.FakeEmailServer;
import org.example.service.ReminderService;

import java.util.Scanner;

public class LibraryApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        AdminService adminService = new AdminService();
        BookService bookService   = new BookService();
        LoanService loanService   = new LoanService();
        UserService userService   = new UserService();

        ReminderService reminderService =
                new ReminderService(new EmailNotifier(new FakeEmailServer()), loanService);

        System.out.println("📚 Welcome to the Library Management System!");

        while (true) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1. Login as Admin");
            System.out.println("2. Add a Book (Admin only)");
            System.out.println("3. Search for a Book");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Check Overdue Books");
            System.out.println("6. Pay Fine");
            System.out.println("7. Register User");
            System.out.println("8. Unregister User (Admin only)");
            System.out.println("9. Send Overdue Reminders (Admin only)");
            System.out.println("10. Logout");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a number between 1–11.");
                continue;
            }

            switch (choice) {

                case 1 -> {
                    System.out.print("👤 Username: ");
                    String user = input.nextLine();
                    System.out.print("🔐 Password: ");
                    String pass = input.nextLine();

                    if (adminService.login(user, pass))
                        System.out.println("✅ Logged in successfully!");
                    else
                        System.out.println("❌ Invalid username or password!");
                }

                case 2 -> {
                    if (!adminService.isLoggedIn()) {
                        System.out.println("❌ Admin login required!");
                        break;
                    }
                    System.out.print("Book title: ");
                    String title = input.nextLine();
                    System.out.print("Author: ");
                    String author = input.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = input.nextLine();

                    bookService.addBook(title, author, isbn);
                }

                case 3 -> {
                    System.out.print("🔍 Search keyword: ");
                    String keyword = input.nextLine();
                    var results = bookService.search(keyword);

                    if (results.isEmpty())
                        System.out.println("❌ No matching books found.");
                    else {
                        System.out.println("✅ Search results:");
                        results.forEach(System.out::println);
                    }
                }

                case 4 -> {
                    System.out.print("👤 Borrower name: ");
                    String borrower = input.nextLine();
                    userService.ensureExists(borrower);

                    System.out.print("📕 Book title: ");
                    String bookTitle = input.nextLine();

                    loanService.borrowBook(borrower, bookTitle);
                }

                case 5 -> loanService.checkOverdueBooks();

                case 6 -> {
                    System.out.print("👤 Borrower name: ");
                    String borrower = input.nextLine();
                    loanService.payFine(borrower);
                }

                case 7 -> {
                    System.out.print("👤 New Username: ");
                    String u = input.nextLine();
                    userService.register(u);
                }

                case 8 -> {
                    System.out.print("👤 Username to unregister: ");
                    String u = input.nextLine();
                    userService.unregister(u, adminService, loanService);
                }

                case 9 -> {
                    if (!adminService.isLoggedIn()) {
                        System.out.println("❌ Admin login required!");
                        break;
                    }
                    reminderService.sendOverdueReminders();
                }

                case 10 -> {
                    adminService.logout();
                    System.out.println("✅ Logged out successfully.");
                }

                case 11 -> {
                    System.out.println("👋 Exiting... Have a great day!");
                    return;
                }

                default -> System.out.println("⚠️ Invalid option. Try again!");
            }
        }
    }
}
