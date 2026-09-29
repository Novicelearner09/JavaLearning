package Oops;

/**
 * POLYMORPHISM DEMO
 * -----------------
 * Definition: The ability of a single interface or method name to express different behaviors.
 * - Compile-Time Polymorphism (Method Overloading): Same method name with different parameters.
 * - Runtime Polymorphism (Method Overriding): Subclass provides custom implementation of base method (vtable dynamic dispatch).
 *
 * Utilities & Benefits:
 * - Uniform Interfaces: Callers use a clean, consistent interface.
 * - Dynamic Extensibility: Open/Closed Principle - new behaviors added without changing client code.
 */
public class PolymorphismDemo {

    // --- 1. COMPILE-TIME POLYMORPHISM (METHOD OVERLOADING) ---
    public static class PaymentProcessor {
        public void processPayment(double amount) {
            System.out.println("Processing cash payment of $" + amount);
        }

        public void processPayment(double amount, String currency) {
            System.out.println("Processing payment of " + amount + " " + currency);
        }

        public void processPayment(double amount, String currency, String promoCode) {
            System.out.println("Processing payment of " + amount + " " + currency + " with Promo: " + promoCode);
        }
    }

    // --- 2. RUNTIME POLYMORPHISM (METHOD OVERRIDING) ---
    public static class NotificationService {
        public void send(String message) {
            System.out.println("Sending generic notification: " + message);
        }
    }

    public static class EmailNotification extends NotificationService {
        @Override
        public void send(String message) {
            System.out.println("[EMAIL SENDER] " + message);
        }
    }

    public static class SmsNotification extends NotificationService {
        @Override
        public void send(String message) {
            System.out.println("[SMS SENDER] " + message);
        }
    }

    public static class PushNotification extends NotificationService {
        @Override
        public void send(String message) {
            System.out.println("[PUSH NOTIFICATION] " + message);
        }
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("         3. POLYMORPHISM DEMO            ");
        System.out.println("=========================================");

        // Method Overloading (Compile-Time)
        System.out.println("--- Compile-Time Polymorphism (Overloading) ---");
        PaymentProcessor processor = new PaymentProcessor();
        processor.processPayment(100.0);
        processor.processPayment(250.0, "EUR");
        processor.processPayment(500.0, "USD", "DISCOUNT2026");

        // Method Overriding (Runtime Dynamic Dispatch)
        System.out.println("\n--- Runtime Polymorphism (Overriding) ---");
        NotificationService[] channels = new NotificationService[] {
            new EmailNotification(),
            new SmsNotification(),
            new PushNotification()
        };

        for (NotificationService channel : channels) {
            channel.send("Security Alert: New login detected");
        }
    }

    public static void main(String[] args) {
        runDemo();
    }
}
