package Oops;

/**
 * ABSTRACTION DEMO
 * ----------------
 * Definition: Hiding implementation details and showing only mandatory features/contracts to the user.
 * - Abstract Classes: Partial abstraction holding state (fields), constructors, and abstract methods.
 * - Interfaces: Pure contract definitions supporting default methods, static helpers, and private helpers.
 *
 * Utilities & Benefits:
 * - Reduced Complexity: Users interact with essential high-level interfaces without worrying about low-level code.
 * - Standardization: Guarantees all implementing classes conform to required signatures.
 */
public class AbstractionDemo {

    // --- ABSTRACT CLASS ---
    public static abstract class Vehicle {
        private final String brand;

        public Vehicle(String brand) {
            this.brand = brand;
        }

        public String getBrand() { return brand; }

        // Concrete method shared by all vehicles
        public void startEngine() {
            System.out.println(brand + " engine started.");
        }

        // Abstract method: mandatory implementation for subclasses
        public abstract void drive();
    }

    public static class ElectricCar extends Vehicle {
        private final int batteryCapacity;

        public ElectricCar(String brand, int batteryCapacity) {
            super(brand);
            this.batteryCapacity = batteryCapacity;
        }

        @Override
        public void drive() {
            System.out.println("Driving " + getBrand() + " silently on electric power (" + batteryCapacity + " kWh battery).");
        }
    }

    // --- INTERFACE WITH MODERN FEATURES (Java 8, 9+) ---
    public interface PaymentGateway {
        // Contract method
        void executeTransaction(double amount);

        // Default method (behavior contract with default fallback)
        default void logTransaction(String txId, double amount) {
            formatAndLog("TX_SUCCESS", txId, amount);
        }

        // Static utility method
        static boolean isSupportedCurrency(String code) {
            return "USD".equalsIgnoreCase(code) || "EUR".equalsIgnoreCase(code) || "GBP".equalsIgnoreCase(code);
        }

        // Private helper method (encapsulates interface internal logic - Java 9+)
        private void formatAndLog(String status, String txId, double amount) {
            System.out.println("[" + status + "] TX ID: " + txId + " | Amount: $" + amount);
        }
    }

    public static class StripeGateway implements PaymentGateway {
        @Override
        public void executeTransaction(double amount) {
            System.out.println("Processing $" + amount + " via Stripe payment gateway...");
            logTransaction("ST-99481", amount);
        }
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("          4. ABSTRACTION DEMO            ");
        System.out.println("=========================================");

        // Abstract Class usage
        System.out.println("--- Abstract Class ---");
        Vehicle myCar = new ElectricCar("Tesla Model S", 100);
        myCar.startEngine();
        myCar.drive();

        // Interface usage
        System.out.println("\n--- Modern Interface ---");
        if (PaymentGateway.isSupportedCurrency("USD")) {
            PaymentGateway gateway = new StripeGateway();
            gateway.executeTransaction(450.00);
        }
    }

    public static void main(String[] args) {
        runDemo();
    }
}
