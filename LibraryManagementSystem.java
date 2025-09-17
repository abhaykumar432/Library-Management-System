import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

//Book Class
class Book{
    private int bookID;
    private String title;
    private String author;
    private boolean isAvail;
    //Constructor for the Book class
    public Book(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.isAvail = true;
    }
    //getter for the bookID
    public int getBookID() {
        return bookID;
    }
    //getter for the author name
    public String getAuthor() {
        return author;
    }
    //getter for the title
    public String getTitle() {
        return title;
    }
    //getter for book isAvail?
    public boolean isAvail() {
        return isAvail;
    }
    //isAvail setter
    public void setAvail(boolean avail) {
        isAvail = avail;
    }
    @Override
    public String toString() {
        return this.bookID+" | "+this.title+" | "+this.author+" | "+(isAvail?"Available":"Borrowed");
    }
}

//Borrower class
class Borrower{
    private int borrowerID;
    private String name;
    private List<Book> bookNameList;
    //Constructor for the Borrower class
    public Borrower(int borrowerID, String name) {
        this.borrowerID = borrowerID;
        this.name = name;
        this.bookNameList = new ArrayList<>();
    }
    //getter for the borrowerID
    public int getBorrowerID() {
        return borrowerID;
    }
    //getter for the borrower name
    public String getName() {
        return name;
    }
    //borrow book
    public void borrowBook(Book book){
        this.bookNameList.add(book);
    }
    //return book
    public void returnBook(Book book){
        this.bookNameList.remove(book);
    }
    //getter for the borrowed book
    public List<Book> getBookNameList(){
        return this.bookNameList;
    }
    @Override
    public String toString(){
        return this.borrowerID+" | "+this.name+" | Borrowed Books : "+this.bookNameList.size();
    }
}
public class LibraryManagementSystem{
    //InputUtil
    static Scanner sc = new Scanner(System.in);
    //BookList
    private static List<Book> book = new ArrayList<>();
    //BorrowerList
    private static List<Borrower> borrower = new ArrayList<>();

    //Main function
    public static void main(String[] args) {
        System.out.println("\n=== Welcome to Library ===\n");
        int choice;
        while(true){
            System.out.println("\n1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("6. Exit\n");
            //Select the services
            System.out.print("Select the services : ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1 -> addBook();
                case 2 -> viewBook();
                case 3 -> borrowBook();
                case 4 -> returnBook();
                case 5 -> searchBook();
                case 6 -> {
                    System.out.println("\n\nYou have successfully exited. \nThanks for visiting the library!");
                    System.exit(0); //stops the program and exits
                }
                default -> System.out.println("\n\nOops! That’s not a valid service option. \nTry again.");
            }
        }
    }

    //addBook()
    public static void addBook(){
        System.out.println("\nEnter the book details : \n");
        System.out.print("1.Enter the Book ID : ");
        int id = getID();
        System.out.print("2.Enter the Book Title : ");
        String title =  sc.nextLine();
        System.out.print("3.Enter the Book Author Name : ");
        String author = sc.nextLine();
        //add the book to library shelf
        book.add(new Book(id,title,author));
        System.out.println("\nBook added successfully!");
    }

    //viewBook()
    public static void viewBook(){
        if(!book.isEmpty()){
            System.out.println("\n----Book List----\n");
            for(Book b : book){
                System.out.println(b.toString());
            }
        }else{
            System.out.println("\nNo Books are in Library.");
        }
    }

    //borrowBook()
    public static void borrowBook(){
        System.out.print("1. Enter Borrower ID : ");
        int id  = getID();
        Borrower brr = getBorrower(id);
        if(brr == null){
            System.out.println("New borrower!\n2. Enter Borrower Name: ");
            String name = sc.nextLine();
            brr = new Borrower(id,name);
            borrower.add(brr);
        }

        System.out.print("\nEnter the Book ID to borrow :");
        int bID = getID();
        Book b = getBook(bID);

        if(b==null){
            System.out.println("\nBOOK NOT FOUND!");
        }else if(!b.isAvail()){
            System.out.println("\nBOOK IS NOT AVAILABLE");
        }else{
            b.setAvail(false);
            brr.borrowBook(b);
            System.out.println(b.getBookID()+" -> "+b.getTitle()+" is assigned to "+brr.getName());
        }
    }

    //returnBook()
    public static void returnBook(){
        System.out.print("1. Enter Borrower ID : ");
        int id  = getID();
        Borrower brr = getBorrower(id);
        if(brr!=null){
            System.out.print("\nEnter the Book ID to return :");
            int bID = getID();
            Book b = getBook(bID);
            if(b==null || !brr.getBookNameList().contains(b)){
                System.out.println("This borrower did not borrow that book!");
            }else{
                b.setAvail(true);
                brr.returnBook(b);
                System.out.println("\n"+brr.getName()+" returned book ID : "+b.getBookID()+" -> "+b.getTitle()+" successfully!.");
            }
        }else{
            System.out.println("Borrower not found.\n");
            return;
        }
    }

    //searchBook()
    public static void searchBook(){
        System.out.print("\nEnter the Author-name / Title-name of Book : ");
        String search = sc.nextLine().toLowerCase();
        boolean found = false;
        List<Book> li = new ArrayList<>();
        for(Book b : book){
            if(b.getAuthor().toLowerCase().contains(search) || b.getTitle().toLowerCase().contains(search)){
                li.add(b);
                found = true;
            }
        }
        if(found){
            for(Book b : li){
                System.out.println(b.toString());
            }
        }else{
            System.out.println("\nNo books found for: " + search);
        }
    }

    //========Utility Methods========
    //getID() for the exception handling in ID
    private static int getID(){
        while(true){
            try{
                int id = Integer.parseInt(sc.nextLine());
                return id;
            }catch (NumberFormatException e){
                //handles the exception
                System.out.print("-> Invalid Number!\ntry Again! \n\n");
            }
        }
    }

    //getBorrower(id) for checking of the borrower is new or old
    private static Borrower getBorrower(int id){
        for(Borrower br : borrower){
            if(br.getBorrowerID()==id) return br;
        }
        return null;
    }

    //getBook(id) for checking of the book in library
    private static Book getBook(int id){
        for(Book b : book){
            if(b.getBookID()==id) return b;
        }
        return null;
    }
}