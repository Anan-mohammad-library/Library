package org.example.presentation;

import org.example.service.AdminService;
import org.example.service.BookService;
import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminService adminService = new AdminService();
        BookService bookService = new BookService();

        while (true) {
            System.out.println("\n=== Library System ===");
            System.out.println("1. Login");
            System.out.println("2. Add Book");
            System.out.println("3. Search Book");
            System.out.println("4. Logout");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Username: ");
                    String u = sc.nextLine();
                    System.out.print("Password: ");
                    String p = sc.nextLine();
                    if (adminService.login(u, p))
                        System.out.println("✅ Login successful!");
                    else
                        System.out.println("❌ Invalid credentials!");
                }
                case 2 -> {
                    if (!adminService.isLoggedIn()) {
                        System.out.println("⚠️ Please login first!");
                        break;
                    }
                    System.out.print("Title: ");
                    String t = sc.nextLine();
                    System.out.print("Author: ");
                    String a = sc.nextLine();
                    System.out.print("ISBN: ");
                    String i = sc.nextLine();
                    bookService.addBook(t, a, i);
                    System.out.println("✅ Book added!");
                }
                case 3 -> {
                    System.out.print("Keyword: ");
                    String k = sc.nextLine();
                    bookService.search(k).forEach(System.out::println);
                }
                case 4 -> {
                    adminService.logout();
                    System.out.println("✅ Logged out!");
                }
                case 5 -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
