import java.util.List;

public class BookDB {
    public static void main(String[] args) {
        try {
//            Class.forName("org.h2.Driver");
            BookDAO dao = new BookDAOImpl();

            dao.createTable();

            dao.insertBook(new Book(1, "The Alchemist", "Paulo Coelho"));
            dao.insertBook(new Book(2, "1984", "George Orwell"));

            System.out.println("Books after insertion:");
            dao.getAllBooks().forEach(System.out::println);

            // Update a book
            dao.updateBook(new Book(1, "The Alchemist", "P. Coelho"));
            System.out.println("\nAfter update:");
            dao.getAllBooks().forEach(System.out::println);

            // Delete a book
            dao.deleteBook(2);
            System.out.println("\nAfter deletion:");
            dao.getAllBooks().forEach(System.out::println);

            // Get books by author
            System.out.println("\nBooks by author 'P. Coelho':");
            List<Book> booksByAuthor = dao.getBook("P. Coelho");
            booksByAuthor.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
