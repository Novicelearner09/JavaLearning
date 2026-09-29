package Oops;

/**
 * ENCAPSULATION DEMO
 * ------------------
 * Definition: Hiding an object's internal state (fields) behind private access modifiers
 * and providing controlled, validated access through public getter, setter, or business methods.
 *
 * Utilities & Benefits:
 * - Data Integrity: Prevents invalid or illegal states (e.g., negative balances).
 * - Flexibility: Internal representation can be changed without breaking external code.
 * - Loose Coupling: Callers interact with public APIs rather than direct state.
 */
public class EncapsulationDemo {

    public static class BankAccount {
        // Encapsulated state: Private fields cannot be accessed directly from outside
        private final String accountNumber;
        private double balance;

        public BankAccount(String accountNumber, double initialBalance) {
            if (initialBalance < 0) {
                throw new IllegalArgumentException("Initial balance cannot be negative");
            }
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        // Controlled read access via getters
        public String getAccountNumber() {
            return accountNumber;
        }

        public double getBalance() {
            return balance;
        }

        // Business methods with input validation
        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            this.balance += amount;
            System.out.println("Deposited $" + amount + " | New Balance: $" + this.balance);
        }

        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                this.balance -= amount;
                System.out.println("Withdrew $" + amount + " | Remaining Balance: $" + this.balance);
                return true;
            }
            System.out.println("Withdrawal failed: Insufficient funds or invalid amount (" + amount + ")");
            return false;
        }
    }

    public static void runDemo() {
        System.out.println("=========================================");
        System.out.println("         1. ENCAPSULATION DEMO           ");
        System.out.println("=========================================");

        BankAccount account = new BankAccount("ACC-9876", 500.00);
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Initial Balance: $" + account.getBalance());

        account.deposit(200.00);
        account.withdraw(150.00);
        account.withdraw(1000.00); // Invalid withdrawal attempt
    }

    public static void main(String[] args) {
        runDemo();
    }
}
