package Oops;

/**
 * MAIN RUNNER FOR OOPS PACKAGE
 * ----------------------------
 * Executes all individual OOP concept demos in sequence.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("*************************************************");
        System.out.println("*         JAVA OOP CONCEPTS MASTER DEMO          *");
        System.out.println("*************************************************\n");

        // 1. Encapsulation
        EncapsulationDemo.runDemo();
        System.out.println();

        // 2. Inheritance
        InheritanceDemo.runDemo();
        System.out.println();

        // 3. Polymorphism
        PolymorphismDemo.runDemo();
        System.out.println();

        // 4. Abstraction
        AbstractionDemo.runDemo();
        System.out.println();

        // 5. Composition
        CompositionDemo.runDemo();
        System.out.println();

        // 6. Modern OOP Enhancements (Records & Sealed Types)
        RecordsAndSealedDemo.runDemo();
        System.out.println();

        System.out.println("*************************************************");
        System.out.println("*         ALL OOP DEMOS COMPLETED SUCCESSFULLY   *");
        System.out.println("*************************************************");
    }
}
