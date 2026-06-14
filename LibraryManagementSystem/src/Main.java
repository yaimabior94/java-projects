import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookDAO dao = new BookDAOImpl();

        while(true) {

            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Exit");

            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:
                    System.out.print("Title: ");
                    String title = sc.nextLine();

                    System.out.print("Author: ");
                    String author = sc.nextLine();

                    System.out.print("Year: ");
                    int year = sc.nextInt();
                    sc.nextLine();

                    dao.addBook(
                            new Book(title, author, year, "Available")
                    );
                    break;

                case 2:
                    List<Book> books = dao.getAllBooks();

                    for(Book b : books) {
                        System.out.println(
                                b.getId() + " | " +
                                b.getTitle() + " | " +
                                b.getAuthor() + " | " +
                                b.getYear() + " | " +
                                b.getStatus()
                        );
                    }
                    break;

                case 3:
                    System.out.print("Enter title: ");
                    String searchTitle = sc.nextLine();

                    Book book = dao.findBookByTitle(searchTitle);

                    if(book != null) {
                        System.out.println(book.getTitle() +
                                " by " + book.getAuthor());
                    } else {
                        System.out.println("Book not found");
                    }
                    break;

                case 4:
                    System.out.print("Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Title: ");
                    String newTitle = sc.nextLine();

                    System.out.print("New Author: ");
                    String newAuthor = sc.nextLine();

                    System.out.print("New Year: ");
                    int newYear = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status: ");
                    String status = sc.nextLine();

                    dao.updateBook(
                            new Book(id, newTitle, newAuthor, newYear, status)
                    );
                    break;

                case 5:
                    System.out.print("Book ID: ");
                    int deleteId = sc.nextInt();

                    dao.deleteBook(deleteId);
                    break;

                case 6:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}