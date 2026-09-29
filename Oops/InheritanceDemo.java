package Oops;

/**
 * INHERITANCE DEMO
 * ----------------
 * Definition: Allowing a child class (subclass) to inherit state and behaviors from a
 * parent class (superclass) using the 'extends' keyword ('is-a' relationship).
 *
 * Utilities & Benefits:
 * - Code Reusability: Centralizes common attributes in a base class.
 * - Extensibility: Allows adding new specialized types without altering base code.
 * - Polymorphic Foundation: Subclasses can be referenced via superclass types.
 */
public class InheritanceDemo {

    // Superclass / Base Class
    public static class Employee {
        private final String name;
        private final double baseSalary;

        public Employee(String name, double baseSalary) {
            this.name = name;
            this.baseSalary = baseSalary;
        }

        public String getName() { return name; }
        public double getBaseSalary() { return baseSalary; }

        public double calculatePay() {
            return baseSalary;
        }
    }

    // Subclass / Derived Class 1
    public static class Developer extends Employee {
        private final String programmingLanguage;
        private final double bonus;

        public Developer(String name, double baseSalary, String programmingLanguage, double bonus) {
            super(name, baseSalary); // Call superclass constructor
            this.programmingLanguage = programmingLanguage;
            this.bonus = bonus;
        }

        public String getProgrammingLanguage() { return programmingLanguage; }

        @Override
        public double calculatePay() {
            return getBaseSalary() + bonus; // Overriding base pay calculation
        }
    }

    // Subclass / Derived Class 2
    public static class Manager extends Employee {
        private final int teamSize;
        private final double allowance;

        public Manager(String name, double baseSalary, int teamSize, double allowance) {
            super(name, baseSalary);
            this.teamSize = teamSize;
            this.allowance = allowance;
        }

        public int getTeamSize() { return teamSize; }

        @Override
        public double calculatePay() {
            return getBaseSalary() + (teamSize * 500) + allowance;
        }
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("          2. INHERITANCE DEMO            ");
        System.out.println("=========================================");

        Employee emp = new Employee("Alice", 60000);
        Developer dev = new Developer("Bob", 80000, "Java", 15000);
        Manager mgr = new Manager("Carol", 95000, 5, 10000);

        System.out.println(emp.getName() + " (Employee) Total Pay: $" + emp.calculatePay());
        System.out.println(dev.getName() + " (" + dev.getProgrammingLanguage() + " Dev) Total Pay: $" + dev.calculatePay());
        System.out.println(mgr.getName() + " (Manager of " + mgr.getTeamSize() + ") Total Pay: $" + mgr.calculatePay());
    }

    public static void main(String[] args) {
        runDemo();
    }
}
