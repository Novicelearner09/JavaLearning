package Oops;

import java.util.Objects;

/**
 * MODERN JAVA OOP ENHANCEMENTS DEMO
 * ---------------------------------
 * 1. Records (Java 16+): Immutable data carriers with auto-generated equals(), hashCode(), toString(), and getters.
 * 2. Sealed Classes / Interfaces (Java 17+): Restrict implementation/extension hierarchies using 'permits'.
 *
 * Utilities & Benefits:
 * - Boilerplate Elimination: Zero POJO boilerplate for DTOs.
 * - Domain Protection: Exhaustive type checking in switch pattern matching without 'default' branch (Java 21).
 */
public class RecordsAndSealedDemo {

    // --- 1. RECORD DEMO ---
    public record UserDto(String username, String email) {
        // Compact constructor for validation
        public UserDto {
            Objects.requireNonNull(username, "Username cannot be null");
            Objects.requireNonNull(email, "Email cannot be null");
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email address: " + email);
            }
        }
    }

    // --- 2. SEALED INTERFACE DEMO ---
    public sealed interface Shape permits Circle, Rectangle, Triangle {}

    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double base, double height) implements Shape {}

    // Pattern Matching for Switch & Record Patterns (Java 21)
    public static String calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c       -> "Circle Area: " + (Math.PI * c.radius() * c.radius());
            case Rectangle r    -> "Rectangle Area: " + (r.width() * r.height());
            case Triangle t     -> "Triangle Area: " + (0.5 * t.base() * t.height());
        };
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("  6. RECORDS & SEALED TYPES DEMO (16-21) ");
        System.out.println("=========================================");

        // Record demonstration
        UserDto user = new UserDto("dev_guru", "guru@example.com");
        System.out.println("Record instance: " + user);
        System.out.println("Username: " + user.username());
        System.out.println("Email: " + user.email());

        // Sealed Class & Pattern Matching demonstration
        Shape c = new Circle(5.0);
        Shape r = new Rectangle(4.0, 6.0);
        Shape t = new Triangle(3.0, 8.0);

        System.out.println(calculateArea(c));
        System.out.println(calculateArea(r));
        System.out.println(calculateArea(t));
    }

    public static void main(String[] args) {
        runDemo();
    }
}
