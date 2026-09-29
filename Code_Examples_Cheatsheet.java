import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * Modern Java Executable Cheat-Sheet
 * Demonstrates Core OOP Concepts (Encapsulation, Inheritance, Polymorphism, Abstraction, Composition, Records, Sealed Classes)
 * alongside Java 8 to 21+ features (Virtual Threads, StampedLock, CompletableFuture).
 */
public class Code_Examples_Cheatsheet {

    // ==========================================
    // SECTION 1: OOP CONCEPTS DEMONSTRATIONS
    // ==========================================

    // --- 1. ENCAPSULATION ---
    public static class BankAccount {
        private final String accountNumber;
        private double balance;

        public BankAccount(String accountNumber, double initialBalance) {
            if (initialBalance < 0) {
                throw new IllegalArgumentException("Initial balance cannot be negative");
            }
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public double getBalance() { return balance; }

        public void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
            this.balance += amount;
        }

        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                this.balance -= amount;
                return true;
            }
            return false;
        }
    }

    // --- 2. INHERITANCE ---
    public static class Employee {
        private final String name;
        private final double baseSalary;

        public Employee(String name, double baseSalary) {
            this.name = name;
            this.baseSalary = baseSalary;
        }

        public String getName() { return name; }
        public double getBaseSalary() { return baseSalary; }

        public double calculatePay() { return baseSalary; }
    }

    public static class Developer extends Employee {
        private final String language;
        private final double bonus;

        public Developer(String name, double baseSalary, String language, double bonus) {
            super(name, baseSalary);
            this.language = language;
            this.bonus = bonus;
        }

        public String getLanguage() { return language; }

        @Override
        public double calculatePay() {
            return getBaseSalary() + bonus;
        }
    }

    // --- 3. POLYMORPHISM ---
    // Compile-time Polymorphism (Method Overloading)
    public static class PaymentProcessor {
        public String processPayment(double amount) {
            return "Paid $" + amount + " via Cash";
        }
        public String processPayment(double amount, String currency) {
            return "Paid " + amount + " " + currency;
        }
        public String processPayment(double amount, String currency, String promo) {
            return "Paid " + amount + " " + currency + " with Promo [" + promo + "]";
        }
    }

    // Runtime Polymorphism (Method Overriding)
    public static class NotificationService {
        public String send(String msg) { return "Generic notification: " + msg; }
    }

    public static class EmailNotification extends NotificationService {
        @Override
        public String send(String msg) { return "[EMAIL] " + msg; }
    }

    public static class SmsNotification extends NotificationService {
        @Override
        public String send(String msg) { return "[SMS] " + msg; }
    }

    // --- 4. ABSTRACTION ---
    // Abstract Class
    public static abstract class Vehicle {
        private final String brand;
        public Vehicle(String brand) { this.brand = brand; }
        public String getBrand() { return brand; }
        public void startEngine() { System.out.println(brand + " engine started."); }
        public abstract void drive();
    }

    public static class Car extends Vehicle {
        public Car(String brand) { super(brand); }
        @Override
        public void drive() { System.out.println("Driving the " + getBrand() + " car on roads."); }
    }

    // Interface (with default, static, private methods - Java 8, 9+)
    public interface PaymentGateway {
        void executeTransaction(double amount);

        default void logTransaction(String txId, double amount) {
            formatAndLog("TX_SUCCESS", txId, amount);
        }

        static boolean isSupportedCurrency(String code) {
            return "USD".equalsIgnoreCase(code) || "EUR".equalsIgnoreCase(code);
        }

        private void formatAndLog(String status, String txId, double amount) {
            System.out.println("[" + status + "] TX ID: " + txId + " | Amount: $" + amount);
        }
    }

    public static class StripeGateway implements PaymentGateway {
        @Override
        public void executeTransaction(double amount) {
            System.out.println("Executing $" + amount + " transaction on Stripe...");
            logTransaction("ST-90210", amount);
        }
    }

    // --- 5. COMPOSITION ---
    public static class Engine {
        private final String type;
        public Engine(String type) { this.type = type; }
        public void start() { System.out.println(type + " engine started."); }
    }

    public static class GpsNavigator {
        public void navigateTo(String dest) { System.out.println("Navigating to: " + dest); }
    }

    public static class SmartCar {
        private final Engine engine;
        private final GpsNavigator gps;

        public SmartCar(Engine engine, GpsNavigator gps) {
            this.engine = engine;
            this.gps = gps;
        }

        public void driveTo(String destination) {
            engine.start();
            gps.navigateTo(destination);
            System.out.println("SmartCar in motion...");
        }
    }

    // --- 6. RECORDS & SEALED CLASSES (Java 16 - 17) ---
    public record UserDto(String username, String email) {
        public UserDto {
            Objects.requireNonNull(username, "Username cannot be null");
            Objects.requireNonNull(email, "Email cannot be null");
        }
    }

    public sealed interface Shape permits Circle, Rectangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}

    // Pattern Matching for Switch & Record Patterns (Java 21)
    public static String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c when c.radius() > 10.0 -> "Large Circle with radius: " + c.radius();
            case Circle c                         -> "Small Circle with radius: " + c.radius();
            case Rectangle(double w, double h)    -> "Rectangle with area: " + (w * h);
            case null                             -> "Null shape provided";
        };
    }

    // ==========================================
    // SECTION 2: ADVANCED JAVA FEATURES
    // ==========================================

    // StampedLock Optimistic Reading (Java 8+)
    public static class InventoryTracker {
        private int itemQuantity = 100;
        private final StampedLock lock = new StampedLock();

        public int getItemQuantityOptimistic() {
            long stamp = lock.tryOptimisticRead(); // Non-blocking optimistic read
            int currentQty = this.itemQuantity;
            if (!lock.validate(stamp)) { // Check if a write occurred in between
                stamp = lock.readLock(); // Fallback to pessimistic read lock
                try {
                    currentQty = this.itemQuantity;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return currentQty;
        }

        public void updateQuantity(int delta) {
            long stamp = lock.writeLock();
            try {
                this.itemQuantity += delta;
            } finally {
                lock.unlockWrite(stamp);
            }
        }
    }

    // Virtual Threads Example (Java 21 / Project Loom)
    public static void runVirtualThreadsDemo() {
        System.out.println("\n=== Starting Virtual Threads Demo ===");
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(1, 10).forEach(i -> {
                executor.submit(() -> {
                    System.out.println("Task " + i + " running on thread: " + Thread.currentThread());
                    try {
                        Thread.sleep(100); // Virtual thread unmounts from carrier thread during sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return i;
                });
            });
        } // Auto-closes and blocks until all virtual threads finish
        System.out.println("=== Completed Virtual Threads Demo ===");
    }

    // CompletableFuture Pipeline Operations
    public static void runAsyncPipelineDemo() {
        System.out.println("\n=== Starting CompletableFuture Demo ===");
        CompletableFuture<String> pipeline = CompletableFuture.supplyAsync(() -> "User_101")
            .thenApplyAsync(userId -> {
                return "ProfileFor_" + userId;
            })
            .thenCombine(CompletableFuture.supplyAsync(() -> "ROLE_ADMIN"), (profile, role) -> {
                return profile + " with permissions [" + role + "]";
            })
            .exceptionally(ex -> "Error processing pipeline: " + ex.getMessage());

        System.out.println("Async Pipeline Result: " + pipeline.join());
    }

    // Helper runner to demonstrate OOP concepts cleanly
    public static void runOopDemos() {
        System.out.println("=========================================");
        System.out.println("          DEMONSTRATING OOP CONCEPTS     ");
        System.out.println("=========================================");

        // 1. Encapsulation
        System.out.println("\n--- 1. ENCAPSULATION ---");
        BankAccount acc = new BankAccount("ACC-9876", 500.0);
        acc.deposit(200.0);
        acc.withdraw(100.0);
        System.out.println("Account Number: " + acc.getAccountNumber() + " | Final Balance: $" + acc.getBalance());

        // 2. Inheritance
        System.out.println("\n--- 2. INHERITANCE ---");
        Employee emp = new Employee("Alice", 60000);
        Developer dev = new Developer("Bob", 80000, "Java", 15000);
        System.out.println(emp.getName() + " Base Pay: $" + emp.calculatePay());
        System.out.println(dev.getName() + " (" + dev.getLanguage() + ") Total Pay: $" + dev.calculatePay());

        // 3. Polymorphism
        System.out.println("\n--- 3. POLYMORPHISM ---");
        PaymentProcessor processor = new PaymentProcessor();
        System.out.println("Overloading 1: " + processor.processPayment(100.0));
        System.out.println("Overloading 2: " + processor.processPayment(250.0, "EUR"));
        System.out.println("Overloading 3: " + processor.processPayment(500.0, "USD", "SAVE2026"));

        NotificationService notif1 = new EmailNotification();
        NotificationService notif2 = new SmsNotification();
        System.out.println("Dynamic Overriding 1: " + notif1.send("System Maintenance"));
        System.out.println("Dynamic Overriding 2: " + notif2.send("Security OTP: 593021"));

        // 4. Abstraction
        System.out.println("\n--- 4. ABSTRACTION ---");
        Vehicle vehicle = new Car("Tesla");
        vehicle.startEngine();
        vehicle.drive();

        if (PaymentGateway.isSupportedCurrency("USD")) {
            PaymentGateway gateway = new StripeGateway();
            gateway.executeTransaction(350.0);
        }

        // 5. Composition
        System.out.println("\n--- 5. COMPOSITION ---");
        SmartCar tesla = new SmartCar(new Engine("V8 Electric"), new GpsNavigator());
        tesla.driveTo("Silicon Valley Headquarters");

        // 6. Records & Sealed Classes
        System.out.println("\n--- 6. RECORDS & SEALED TYPES ---");
        UserDto user = new UserDto("dev_guru", "guru@example.com");
        System.out.println("Record Auto toString: " + user);

        Shape shape = new Rectangle(5.0, 4.0);
        System.out.println("Sealed Type Pattern Match: " + describeShape(shape));
    }

    public static void main(String[] args) {
        // Execute OOP Concepts Demos
        runOopDemos();

        // Execute Advanced Java Demos
        System.out.println("\n=========================================");
        System.out.println("      ADVANCED JAVA CONCURRENCY DEMOS    ");
        System.out.println("=========================================");

        InventoryTracker tracker = new InventoryTracker();
        System.out.println("Optimistic Quantity Read: " + tracker.getItemQuantityOptimistic());

        runVirtualThreadsDemo();
        runAsyncPipelineDemo();
    }
}

