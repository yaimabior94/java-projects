import java.util.List;

public interface BookDAO {

    void addBook(Book book);

    List<Book> getAllBooks();

    Book findBookByTitle(String title);

    void updateBook(Book book);

    void deleteBook(int id);
}