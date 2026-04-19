import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// ─── Generic Order<I, P> ────────────────────────────────────────────────────
class Order<I, P extends Number> {
    private final I    id;
    private final String category;
    private final String description;
    private final P    price;

    public Order(I id, String category, String description, P price) {
        this.id          = id;
        this.category    = category;
        this.description = description;
        this.price       = price;
    }

    public I      getId()          { return id; }
    public String getCategory()    { return category; }
    public String getDescription() { return description; }
    public P      getPrice()       { return price; }

    @Override
    public String toString() {
        return String.format("Order[id=%-6s | %-13s | %-30s | price=%s]",
                id, category, description, price);
    }
}

// ─── Generic OrderRepository<I, P> ──────────────────────────────────────────
class OrderRepository<I, P extends Number> {
    private final List<Order<I, P>> orders = new ArrayList<>();

    /** Add a single order. */
    public void add(Order<I, P> order) {
        orders.add(order);
        System.out.println("  ✔ Added → " + order);
    }

    /** Return an unmodifiable view of all stored orders. */
    public List<Order<I, P>> getAll() { return List.copyOf(orders); }

    /** Display every order in the repository. */
    public void displayAll() {
        if (orders.isEmpty()) { System.out.println("  (empty)"); return; }
        orders.forEach(o -> System.out.println("  " + o));
    }

    // ── Generic utility methods ─────────────────────────────────────────────

    /**
     * Calculate the total price of any list of orders whose price type
     * extends Number.  Works for Double, Integer, Long, etc.
     *
     * @param list Any list of Order objects (wildcard – read-only)
     * @return     Total as a double
     */
    public static <I, P extends Number> double totalPrice(List<Order<I, P>> list) {
        return list.stream()
                   .mapToDouble(o -> o.getPrice().doubleValue())
                   .sum();
    }

    /**
     * Find the most expensive order in any list.
     *
     * @param list Any list of orders
     * @return     Optional containing the priciest order, or empty if list is empty
     */
    public static <I, P extends Number> Optional<Order<I, P>> mostExpensive(
            List<Order<I, P>> list) {
        return list.stream()
                   .max(Comparator.comparingDouble(o -> o.getPrice().doubleValue()));
    }
}

// ─── Main ────────────────────────────────────────────────────────────────────
public class GenericOrderSystem {

    static void printHeader(String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf( "║  %-52s║%n", title);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    static void printSummary(String label, List<? extends Order<?, ? extends Number>> raw) {
        // We need typed lists for the generic static methods – collect once.
        System.out.printf("  %-15s total  : %.2f%n", label,
                raw.stream().mapToDouble(o -> o.getPrice().doubleValue()).sum());
        raw.stream()
           .max(Comparator.comparingDouble(o -> o.getPrice().doubleValue()))
           .ifPresent(o -> System.out.printf(
                   "  %-15s max    : %s%n", label, o));
    }

    public static void main(String[] args) {

        // ── 1. Food orders  →  Order<Integer, Double> ────────────────────
        printHeader("1 · FOOD ORDERS  (id: Integer, price: Double)");
        OrderRepository<Integer, Double> foodRepo = new OrderRepository<>();

        foodRepo.add(new Order<>(101, "Food", "Grilled Salmon w/ Veggies",  24.99));
        foodRepo.add(new Order<>(102, "Food", "Margherita Pizza (large)",   18.50));
        foodRepo.add(new Order<>(103, "Food", "Wagyu Burger Deluxe",        32.00));
        foodRepo.add(new Order<>(104, "Food", "Vegan Buddha Bowl",          14.75));

        printHeader("Food Repository Contents");
        foodRepo.displayAll();

        List<Order<Integer, Double>> foodList = foodRepo.getAll();
        double foodTotal = OrderRepository.totalPrice(foodList);
        Optional<Order<Integer, Double>> priceyFood = OrderRepository.mostExpensive(foodList);

        System.out.printf("%n  ▶ Total price   : $%.2f%n", foodTotal);
        priceyFood.ifPresent(o ->
                System.out.printf("  ▶ Most expensive: %s%n", o));


        // ── 2. Electronics orders  →  Order<String, Integer> ─────────────
        printHeader("2 · ELECTRONICS ORDERS  (id: String, price: Integer)");
        OrderRepository<String, Integer> elecRepo = new OrderRepository<>();

        elecRepo.add(new Order<>("EL-001", "Electronics", "Sony 65\" OLED TV",         1_800));
        elecRepo.add(new Order<>("EL-002", "Electronics", "MacBook Pro 16-inch",        2_499));
        elecRepo.add(new Order<>("EL-003", "Electronics", "Sony WH-1000XM5 Headphones",   350));
        elecRepo.add(new Order<>("EL-004", "Electronics", "iPad Pro 12.9\" M4",           999));

        printHeader("Electronics Repository Contents");
        elecRepo.displayAll();

        List<Order<String, Integer>> elecList = elecRepo.getAll();
        double elecTotal = OrderRepository.totalPrice(elecList);
        Optional<Order<String, Integer>> priceyElec = OrderRepository.mostExpensive(elecList);

        System.out.printf("%n  ▶ Total price   : $%.2f%n", elecTotal);
        priceyElec.ifPresent(o ->
                System.out.printf("  ▶ Most expensive: %s%n", o));


        // ── 3. Service orders  →  Order<String, Double> ──────────────────
        printHeader("3 · SERVICE ORDERS  (id: String, price: Double)");
        OrderRepository<String, Double> svcRepo = new OrderRepository<>();

        svcRepo.add(new Order<>("SVC-A", "Service", "Annual Cloud Storage Plan",    99.99));
        svcRepo.add(new Order<>("SVC-B", "Service", "Premium Support Subscription", 249.00));
        svcRepo.add(new Order<>("SVC-C", "Service", "Home Cleaning (3 hrs)",         75.00));

        printHeader("Service Repository Contents");
        svcRepo.displayAll();

        List<Order<String, Double>> svcList = svcRepo.getAll();
        double svcTotal = OrderRepository.totalPrice(svcList);
        Optional<Order<String, Double>> priceyStvc = OrderRepository.mostExpensive(svcList);

        System.out.printf("%n  ▶ Total price   : $%.2f%n", svcTotal);
        priceyStvc.ifPresent(o ->
                System.out.printf("  ▶ Most expensive: %s%n", o));


        // ── 4. Grand summary ─────────────────────────────────────────────
        printHeader("4 · GRAND SUMMARY");
        double grandTotal = foodTotal + elecTotal + svcTotal;
        System.out.printf("  Food total        : $%8.2f  (%d orders)%n",
                foodTotal, foodList.size());
        System.out.printf("  Electronics total : $%8.2f  (%d orders)%n",
                elecTotal, elecList.size());
        System.out.printf("  Services total    : $%8.2f  (%d orders)%n",
                svcTotal, svcList.size());
        System.out.println("  ─────────────────────────────────────────");
        System.out.printf("  GRAND TOTAL       : $%8.2f  (%d orders)%n%n",
                grandTotal, foodList.size() + elecList.size() + svcList.size());
    }
}