package bookstore.repositories;

import bookstore.entities.*;

import java.util.Date;
import java.util.List;

public class RepositoryApp {
    public static void main(String[] args) {
        System.out.println("Repository Pattern Demo");

        // 1. Instantiate Repository
        IRepository<ProductEntity> repo = new ProductRepository();

        try {
            // 2. Create Items
            System.out.println("\n--- Saving Items ---");
            repo.save(new BookEntity("Clean Code",45.00,10,"Robert C. Martin"));
            repo.save(new TicketEntity("Java Conference", 299.99));
            repo.save(new DiscMagEntity("Retro Gamer", 15.00, 20, 100, new Date(), true));

            // 3. List All (Polymorphic)
            System.out.println("\n--- All Items ---");
            List<ProductEntity> items = repo.findAll();
            for (ProductEntity p : items) {
                System.out.println(p);
            }

            // 4. Update
            if (!items.isEmpty()) {
                ProductEntity first = items.getFirst();
                System.out.println("\n--- Updating Item ID: " + first.getId() + " ---");

                if (first instanceof TicketEntity) {
                    ((TicketEntity) first).setPrice(10.00);
                } else if (first instanceof PublicationEntity) {
                    ((PublicationEntity) first).setPrice(999.99);
                }

                repo.save(first);
                System.out.println("Updated: " + repo.findById(first.getId()));
            }

            // 5. Delete
            if (items.size() > 1) {
                Long idToDelete = items.get(1).getId();
                System.out.println("\n--- Deleting Item ID: " + idToDelete + " ---");
                repo.delete(idToDelete);
            }

            // 6. List again
            System.out.println("\n--- Remaining Items ---");
            repo.findAll().forEach(System.out::println);

        } finally {
            repo.close();
        }
    }
}
