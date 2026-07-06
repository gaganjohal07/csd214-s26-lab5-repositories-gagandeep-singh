package bookstore;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bookstore Application Started");
        App app = new App();
        try {
            app.run();
        } finally {
            app.shutdown();
        }
    }
}