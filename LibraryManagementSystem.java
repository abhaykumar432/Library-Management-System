import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Book {
    private int bookID;
    private String title;
    private String author;
    private boolean isAvail;

    public Book(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.isAvail = true;
    }

    public int getBookID() { return bookID; }
    public String getAuthor() { return author; }
    public String getTitle() { return title; }
    public boolean isAvail() { return isAvail; }
    public void setAvail(boolean avail) { isAvail = avail; }

    @Override
    public String toString() {
        return bookID + " | " + title + " | " + author + " | " + (isAvail ? "Available" : "Borrowed");
    }
}

class Borrower {
    private int borrowerID;
    private String name;
    private List<Book> borrowedBooks;

    public Borrower(int borrowerID, String name) {
        this.borrowerID = borrowerID;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getBorrowerID() { return borrowerID; }
    public String getName() { return name; }

    public void borrowBook(Book book) { borrowedBooks.add(book); }
    public void returnBook(Book book) { borrowedBooks.remove(book); }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(borrowerID + " | " + name + " | Borrowed Books: " + borrowedBooks.size());
        for (Book b : borrowedBooks) {
            sb.append("\n   -> ").append(b.getTitle());
        }
        return sb.toString();
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private List<Borrower> borrowers = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);


    public void addBook() {
        System.out.println("\nEnter the book details:");
        System.out.print("Book ID: ");
        int id = getID();
        if (getBook(id) != null) {
            System.out.println("Book ID already exists!");
            return;
        }
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        books.add(new Book(id, title, author));
        System.out.println("Book added successfully!");
    }


    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("\nNo books in library.");
        } else {
            System.out.println("\n---- Book List ----");
            books.forEach(System.out::println);
        }
    }


    public void borrowBook() {
        System.out.print("Borrower ID: ");
        int id = getID();
        Borrower brr = getBorrower(id);
        if (brr == null) {
            System.out.print("New borrower! Enter name: ");
            String name = sc.nextLine();
            brr = new Borrower(id, name);
            borrowers.add(brr);
        }

        System.out.print("Enter Book ID to borrow: ");
        int bookID = getID();
        Book b = getBook(bookID);

        if (b == null) {
            System.out.println("Book not found!");
        } else if (!b.isAvail()) {
            System.out.println("Book is not available!");
        } else {
            b.setAvail(false);
            brr.borrowBook(b);
            System.out.println(b.getBookID() + " -> " + b.getTitle() + " borrowed by " + brr.getName());
        }
    }


    public void returnBook() {
        System.out.print("Borrower ID: ");
        int id = getID();
        Borrower brr = getBorrower(id);
        if (brr == null) {
            System.out.println("Borrower not found.");
            return;
        }

        System.out.print("Enter Book ID to return: ");
        int bookID = getID();
        Book b = getBook(bookID);

        if (b == null || !brr.getBorrowedBooks().contains(b)) {
            System.out.println("This borrower did not borrow that book!");
        } else {
            b.setAvail(true);
            brr.returnBook(b);
            System.out.println(brr.getName() + " returned: " + b.getTitle());
        }
    }


    public void searchBook() {
        System.out.print("Enter Title/Author keyword: ");
        String search = sc.nextLine().toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(search) || b.getAuthor().toLowerCase().contains(search)) {
                result.add(b);
            }
        }
        if (result.isEmpty()) {
            System.out.println("No books found for: " + search);
        } else {
            result.forEach(System.out::println);
        }
    }


    private int getID() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }

    private Borrower getBorrower(int id) {
        return borrowers.stream().filter(b -> b.getBorrowerID() == id).findFirst().orElse(null);
    }

    private Book getBook(int id) {
        return books.stream().filter(b -> b.getBookID() == id).findFirst().orElse(null);
    }
}


public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();

        System.out.println("\n=== Welcome to Library ===\n");
        while (true) {
            System.out.println("\n1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice!");
                continue;
            }

            switch (choice) {
                case 1 -> library.addBook();
                case 2 -> library.viewBooks();
                case 3 -> library.borrowBook();
                case 4 -> library.returnBook();
                case 5 -> library.searchBook();
                case 6 -> {
                    System.out.println("Thanks for visiting the library!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
