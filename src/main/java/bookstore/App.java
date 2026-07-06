package bookstore;

import bookstore.entities.*;
import bookstore.pojos.*;
import bookstore.repositories.IRepository;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {

    private final IRepository<ProductEntity> repository;
    private final CashTill cashTill = new CashTill();
    private final Scanner input = new Scanner(System.in);

    public App(IRepository<ProductEntity> repository) {
        this.repository = repository;
    }

    public void run() {
        populate();

        int choice = 0;

        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println(" 1. Add Items (Repository Save)");
            System.out.println(" 2. Edit Items (Repository Save/Update)");
            System.out.println(" 3. Delete Items (Repository Delete)");
            System.out.println(" 4. Sell item(s) (Logic & Repo Sync)");
            System.out.println(" 5. List items (Polymorphic Filtering)");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: \n");

            try {
                String line = input.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                choice = Integer.parseInt(line.trim());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    editItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    sellItem();
                    break;
                case 5:
                    listAny();
                    break;
                case 99:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void shutdown() {
        repository.close();
    }

    public void addItem() {

        int choice = 0;

        while (choice != 99) {

            System.out.println("\nAdd an item\n");
            System.out.println("1. Add Book");
            System.out.println("2. Add Magazine");
            System.out.println("3. Add DiscMag");
            System.out.println("4. Add Ticket");
            System.out.println("5. Add Tire");
            System.out.println("6. Add Battery");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                choice = Integer.parseInt(line.trim());

            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) {
                return;
            }

            Editable item = null;

            switch (choice) {
                case 1:
                    item = new Book();
                    break;
                case 2:
                    item = new Magazine();
                    break;
                case 3:
                    item = new DiscMag();
                    break;
                case 4:
                    item = new Ticket();
                    break;
                case 5:
                    item = new Tire();
                    break;
                case 6:
                    item = new Battery();
                    break;
                default:
                    System.out.println("Invalid selection.");
                    continue;
            }

            item.initialize(input);
            saveToDb(item);
        }
    }

    private void saveToDb(Editable item) {

        ProductEntity entity = null;

        if (item instanceof Book) {
            entity = ((Book) item).toEntity();
        } else if (item instanceof DiscMag) {
            entity = ((DiscMag) item).toEntity();
        } else if (item instanceof Magazine) {
            entity = ((Magazine) item).toEntity();
        } else if (item instanceof Ticket) {
            entity = ((Ticket) item).toEntity();
        } else if (item instanceof Battery) {
            entity = ((Battery) item).toEntity();
        } else if (item instanceof Tire) {
            entity = ((Tire) item).toEntity();
        }

        if (entity != null) {
            repository.save(entity);
            System.out.println("Successfully saved.");
        }
    }

    public void listAny() {

        int choice = 0;

        while (choice != 99) {

            System.out.println("\nAll Items");
            System.out.println("-----------");
            System.out.println("1. All Products");
            System.out.println("2. Books");
            System.out.println("3. Magazines");
            System.out.println("4. DiscMags");
            System.out.println("5. Tickets");
            System.out.println("6. Tires");
            System.out.println("7. Batteries");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                choice = Integer.parseInt(line.trim());

            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) {
                return;
            }

            Class<? extends ProductEntity> filterClass = null;

            switch (choice) {
                case 1:
                    filterClass = ProductEntity.class;
                    break;
                case 2:
                    filterClass = BookEntity.class;
                    break;
                case 3:
                    filterClass = MagazineEntity.class;
                    break;
                case 4:
                    filterClass = DiscMagEntity.class;
                    break;
                case 5:
                    filterClass = TicketEntity.class;
                    break;
                case 6:
                    filterClass = TireEntity.class;
                    break;
                case 7:
                    filterClass = BatteryEntity.class;
                    break;
                default:
                    continue;
            }

            List<ProductEntity> dbEntities = repository.findAll();

            for (ProductEntity entity : dbEntities) {

                if (!filterClass.isInstance(entity)) {
                    continue;
                }

                if (filterClass == MagazineEntity.class && entity instanceof DiscMagEntity) {
                    continue;
                }

                printEntityAsDto(entity);
            }
        }
    }
    private void printEntityAsDto(ProductEntity entity) {
        if (entity instanceof BookEntity) {
            System.out.println(Book.fromEntity((BookEntity) entity));
        } else if (entity instanceof DiscMagEntity) {
            System.out.println(DiscMag.fromEntity((DiscMagEntity) entity));
        } else if (entity instanceof MagazineEntity) {
            System.out.println(Magazine.fromEntity((MagazineEntity) entity));
        } else if (entity instanceof TicketEntity) {
            System.out.println(Ticket.fromEntity((TicketEntity) entity));
        } else if (entity instanceof BatteryEntity) {
            System.out.println(Battery.fromEntity((BatteryEntity) entity));
        } else if (entity instanceof TireEntity) {
            System.out.println(Tire.fromEntity((TireEntity) entity));
        }
    }

    public void editItem() {
        List<ProductEntity> entities = repository.findAll();

        if (entities.isEmpty()) {
            System.out.println("No records found to edit.");
            return;
        }

        System.out.println("Select item index to edit:");

        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());

            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                ProductEntity updatedEntity = null;

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof BatteryEntity) {
                    Battery dto = Battery.fromEntity((BatteryEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TireEntity) {
                    Tire dto = Tire.fromEntity((TireEntity) entity);
                    dto.edit(input);
                    updatedEntity = dto.toEntity();
                }

                if (updatedEntity != null) {
                    updatedEntity.setId(entity.getId());
                    repository.save(updatedEntity);
                    System.out.println("Successfully updated.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void deleteItem() {
        List<ProductEntity> entities = repository.findAll();

        if (entities.isEmpty()) {
            System.out.println("No records found to delete.");
            return;
        }

        System.out.println("Select item index to delete:");

        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());

            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                repository.delete(entity.getId());
                System.out.println("Successfully deleted.");
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void sellItem() {
        List<ProductEntity> entities = repository.findAll();

        if (entities.isEmpty()) {
            System.out.println("No inventory found to sell.");
            return;
        }

        System.out.println("Select item index to sell:");

        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());

            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                ProductEntity updatedEntity = null;

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof BatteryEntity) {
                    Battery dto = Battery.fromEntity((BatteryEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TireEntity) {
                    Tire dto = Tire.fromEntity((TireEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                }

                if (updatedEntity != null) {
                    updatedEntity.setId(entity.getId());
                    repository.save(updatedEntity);
                    System.out.println("Sale synchronized.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void populate() {
        long count = repository.count();

        if (count > 0) {
            System.out.println("Database already seeded.");
            return;
        }

        System.out.println("Seeding data.");
        Faker faker = new Faker();

        for (int i = 0; i < 2; i++) {
            BookEntity b = new BookEntity(
                    faker.book().title(),
                    faker.number().randomDouble(2, 10, 50),
                    faker.number().numberBetween(1, 20),
                    faker.book().author()
            );
            repository.save(b);

            MagazineEntity m = new MagazineEntity(
                    faker.book().title() + " Monthly",
                    faker.number().randomDouble(2, 5, 15),
                    faker.number().numberBetween(5, 50),
                    faker.number().numberBetween(100, 500),
                    faker.date().past(30, TimeUnit.DAYS)
            );
            repository.save(m);

            DiscMagEntity dm = new DiscMagEntity(
                    "Tech Disc: " + faker.app().name(),
                    faker.number().randomDouble(2, 10, 25),
                    faker.number().numberBetween(5, 30),
                    faker.number().numberBetween(50, 200),
                    faker.date().past(60, TimeUnit.DAYS),
                    faker.bool().bool()
            );
            repository.save(dm);

            TicketEntity t = new TicketEntity();
            t.setDescription("Concert: " + faker.rockBand().name());
            t.setPrice(faker.number().randomDouble(2, 50, 150));
            repository.save(t);

            TireEntity tire = new TireEntity(
                    faker.company().name() + " Tires",
                    faker.number().randomDouble(2, 80, 350),
                    faker.number().numberBetween(15, 22)
            );
            repository.save(tire);

            BatteryEntity battery = new BatteryEntity(
                    faker.company().name() + " Batteries",
                    faker.number().randomDouble(2, 100, 250),
                    faker.number().numberBetween(500, 950)
            );
            repository.save(battery);
        }

        System.out.println("Seeding complete.");
    }

    public SaleableItem findItem(SaleableItem item) {
        if (item instanceof Product) {
            String uuid = ((Product) item).getProductId();

            ProductEntity entity = repository.findByProductId(uuid);

            if (entity instanceof BookEntity) {
                return Book.fromEntity((BookEntity) entity);
            } else if (entity instanceof DiscMagEntity) {
                return DiscMag.fromEntity((DiscMagEntity) entity);
            } else if (entity instanceof MagazineEntity) {
                return Magazine.fromEntity((MagazineEntity) entity);
            } else if (entity instanceof TicketEntity) {
                return Ticket.fromEntity((TicketEntity) entity);
            } else if (entity instanceof BatteryEntity) {
                return Battery.fromEntity((BatteryEntity) entity);
            } else if (entity instanceof TireEntity) {
                return Tire.fromEntity((TireEntity) entity);
            }
        }

        return null;
    }

    public boolean findItemExists(SaleableItem item) {
        return findItem(item) != null;
    }

    public SaleableItem getItem(SaleableItem item) {
        return findItem(item);
    }
}