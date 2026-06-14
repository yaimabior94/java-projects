import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    @Override
    public void addBook(Book book) {

        String sql =
                "INSERT INTO books(title,author,year,status) VALUES(?,?,?,?)";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getStatus());

            stmt.executeUpdate();

            System.out.println("Book Added Successfully!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try(Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {

                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getString("status")
                ));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Book findBookByTitle(String title) {

        String sql = "SELECT * FROM books WHERE title=?";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {

                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getString("status")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateBook(Book book) {

        String sql =
                "UPDATE books SET title=?, author=?, year=?, status=? WHERE id=?";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getStatus());
            stmt.setInt(5, book.getId());

            stmt.executeUpdate();

            System.out.println("Book Updated!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id=?";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Book Deleted!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}