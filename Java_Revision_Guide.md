# Java Master Revision Guide: Beginner to Advanced

This revision guide is designed for experienced Java developers needing a rapid refresher on core concepts and an **in-depth deep dive** into advanced Java concepts (JVM internals, Memory Model, Concurrency, Garbage Collection, Modern Java 8–21+ features, and Performance Tuning).

---

## Table of Contents
1. [Phase 1: Core Fundamentals (Quick Refresher)](#phase-1-core-fundamentals-quick-refresher)
2. [Phase 2: Intermediate Concepts](#phase-2-intermediate-concepts)
3. [Phase 3: Deep-Dive Advanced Topics](#phase-3-deep-dive-advanced-topics)
   - [A. JVM Architecture & Java Memory Model (JMM)](#a-jvm-architecture--java-memory-model-jmm)
   - [B. Garbage Collection Mechanics & Tuning](#b-garbage-collection-mechanics--tuning)
   - [C. Deep-Dive Concurrency & Multithreading](#c-deep-dive-concurrency--multithreading)
   - [D. Virtual Threads & Project Loom (Java 21+)](#d-virtual-threads--project-loom-java-21)
   - [E. Modern Java Features (Java 8 to 21+)](#e-modern-java-features-java-8-to-21)
4. [Phase 4: Design Patterns, Memory Leaks & Performance Tuning](#phase-4-design-patterns-memory-leaks--performance-tuning)

---

## Phase 1: Core Fundamentals (Quick Refresher)

### 1. Object-Oriented Programming (OOP) Deep Dive & Enhancements

Object-Oriented Programming in Java rests on four primary pillars—**Encapsulation**, **Inheritance**, **Polymorphism**, and **Abstraction**—alongside essential design principles like **Composition** and modern Java additions (**Records** and **Sealed Classes**).

---

#### A. Encapsulation
* **Concept**: Hiding an object's internal state (fields) behind private access modifiers and providing controlled, validated access through public getter and setter methods.
* **Utilities & Benefits**:
  * **Data Integrity & Protection**: Prevents illegal states (e.g., negative bank balance, invalid age).
  * **Flexibility & Maintainability**: Internal field names/types can change without breaking external callers.
  * **Loose Coupling**: Clients interact only with public APIs rather than internal details.

##### Class Definition:
```java
public class BankAccount {
    // Encapsulated state: private fields cannot be accessed directly from outside
    private final String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Controlled getter for read access
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    // Business method with validation utility
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false; // Insufficient funds or invalid amount
    }
}
```

##### Usage & Output Example:
```java
BankAccount account = new BankAccount("ACC-9876", 500.00);
account.deposit(200.00);
boolean success = account.withdraw(100.00);
System.out.println("Account: " + account.getAccountNumber() + " | Balance: $" + account.getBalance());
// Output: Account: ACC-9876 | Balance: $600.0
```

---

#### B. Inheritance
* **Concept**: Allowing a child class (subclass) to acquire fields and methods from a parent class (superclass) using the `extends` keyword (`is-a` relationship).
* **Utilities & Benefits**:
  * **Code Reusability**: Eliminates duplicate code by defining common attributes in a base class.
  * **Extensibility**: Easily add new types without modifying existing base class code.
  * **Polymorphic Capability**: Subclasses can be treated as instances of their superclass.

##### Class Definition:
```java
// Base / Super class
public class Employee {
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

// Derived / Subclass
public class Developer extends Employee {
    private final String programmingLanguage;
    private final double bonus;

    public Developer(String name, double baseSalary, String programmingLanguage, double bonus) {
        super(name, baseSalary); // Call superclass constructor
        this.programmingLanguage = programmingLanguage;
        this.bonus = bonus;
    }

    public String getProgrammingLanguage() { return programmingLanguage; }

    // Override base behavior
    @Override
    public double calculatePay() {
        return getBaseSalary() + bonus;
    }
}
```

##### Usage & Output Example:
```java
Employee emp = new Employee("Alice", 60000);
Developer dev = new Developer("Bob", 80000, "Java", 15000);

System.out.println(emp.getName() + " Pay: $" + emp.calculatePay());
System.out.println(dev.getName() + " (" + dev.getProgrammingLanguage() + " Dev) Pay: $" + dev.calculatePay());
// Output:
// Alice Pay: $60000.0
// Bob (Java Dev) Pay: $95000.0
```

---

#### C. Polymorphism
* **Concept**: The ability of a single interface or method to take multiple forms.
  * **Compile-Time Polymorphism (Method Overloading)**: Same method name with different parameter types, numbers, or order in the same class.
  * **Runtime Polymorphism (Method Overriding)**: Subclass provides its own specific implementation of a method already declared in its superclass/interface. Dynamic dispatch via `vtable` determines method call at runtime.
* **Utilities & Benefits**:
  * **Flexibility & Clean APIs**: Method overloading gives callers convenient interfaces for different input types.
  * **Plug-and-Play Extensibility**: Method overriding allows introducing new subclasses without altering client code (Open/Closed Principle).

##### Class Definition:
```java
// Compile-Time Polymorphism (Method Overloading) Utility
public class PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing cash payment of $" + amount);
    }

    public void processPayment(double amount, String currency) {
        System.out.println("Processing payment of " + amount + " " + currency);
    }

    public void processPayment(double amount, String currency, String promoCode) {
        System.out.println("Processing payment of " + amount + " " + currency + " using promo: " + promoCode);
    }
}

// Runtime Polymorphism (Method Overriding) Utility
public class NotificationService {
    public void send(String message) {
        System.out.println("Sending generic notification: " + message);
    }
}

public class EmailNotification extends NotificationService {
    @Override
    public void send(String message) {
        System.out.println("[EMAIL] Sending email: " + message);
    }
}

public class SmsNotification extends NotificationService {
    @Override
    public void send(String message) {
        System.out.println("[SMS] Sending SMS: " + message);
    }
}
```

##### Usage & Output Example:
```java
// Overloading (Compile-time)
PaymentProcessor processor = new PaymentProcessor();
processor.processPayment(100.0);
processor.processPayment(250.0, "EUR");
processor.processPayment(500.0, "USD", "SUMMER2026");

// Overriding (Runtime Dynamic Dispatch)
NotificationService service1 = new EmailNotification();
NotificationService service2 = new SmsNotification();

service1.send("Server CPU load > 90%");
service2.send("OTP: 482910");
// Output:
// Processing cash payment of $100.0
// Processing payment of 250.0 EUR
// Processing payment of 500.0 USD using promo: SUMMER2026
// [EMAIL] Sending email: Server CPU load > 90%
// [SMS] Sending SMS: OTP: 482910
```

---

#### D. Abstraction
* **Concept**: Hiding internal execution complexity and exposing only essential interfaces/contracts to callers.
  * **Abstract Class**: Partial abstraction (`abstract class`). Holds instance state (fields), constructors, concrete methods, and abstract methods.
  * **Interface**: Pure contract definition (`interface`). Supports `default` methods (behavior contract), `static` helper methods, and `private` helper methods (Java 9+).
* **Utilities & Benefits**:
  * **Separation of Concerns**: Hides implementation details behind stable contracts.
  * **Shared Frameworks**: Abstract classes provide template method patterns and shared state; Interfaces enable multiple inheritance of type.

##### Class & Interface Definition:
```java
// 1. Abstract Class Example
public abstract class Vehicle {
    private final String brand; // State in abstract class

    public Vehicle(String brand) {
        this.brand = brand;
    }

    public String getBrand() { return brand; }

    // Concrete method shared by all subclasses
    public void startEngine() {
        System.out.println(brand + " engine started.");
    }

    // Abstract method: MUST be implemented by concrete subclasses
    public abstract void drive();
}

public class Car extends Vehicle {
    public Car(String brand) {
        super(brand);
    }

    @Override
    public void drive() {
        System.out.println("Driving the " + getBrand() + " car on roads.");
    }
}

// 2. Modern Interface Example (with default, static, and private methods)
public interface PaymentGateway {
    // Abstract method (contract)
    void executeTransaction(double amount);

    // Default method (provides default implementation, can be overridden)
    default void logTransaction(String txId, double amount) {
        formatAndLog("TX_SUCCESS", txId, amount);
    }

    // Static helper method
    static boolean isSupportedCurrency(String code) {
        return "USD".equalsIgnoreCase(code) || "EUR".equalsIgnoreCase(code) || "GBP".equalsIgnoreCase(code);
    }

    // Private method (encapsulates shared helper logic within interface - Java 9+)
    private void formatAndLog(String status, String txId, double amount) {
        System.out.println("[" + status + "] TX ID: " + txId + " | Amount: $" + amount);
    }
}

public class StripeGateway implements PaymentGateway {
    @Override
    public void executeTransaction(double amount) {
        System.out.println("Processing $" + amount + " via Stripe API...");
        logTransaction("ST-10928", amount);
    }
}
```

##### Usage & Output Example:
```java
Vehicle myCar = new Car("Toyota");
myCar.startEngine();
myCar.drive();

PaymentGateway stripe = new StripeGateway();
if (PaymentGateway.isSupportedCurrency("USD")) {
    stripe.executeTransaction(150.0);
}
// Output:
// Toyota engine started.
// Driving the Toyota car on roads.
// Processing $150.0 via Stripe API...
// [TX_SUCCESS] TX ID: ST-10928 | Amount: $150.0
```

---

#### E. Composition (Has-A Relationship)
* **Concept**: Building complex classes by combining simpler objects rather than relying on inheritance (`has-a` vs `is-a`).
* **Utilities & Benefits**:
  * **Flexible Runtime Behavior**: Components can be swapped dynamically at runtime (Strategy Pattern).
  * **Avoids Class Explosion**: Prevents deep, brittle inheritance hierarchies.
  * **Solves Fragile Base Class Problem**: Changes in parent class won't inadvertently break subclasses.

##### Class Definition:
```java
public class Engine {
    private final String type;

    public Engine(String type) { this.type = type; }
    public void start() { System.out.println(type + " engine roaring to life."); }
}

public class GpsNavigator {
    public void navigateTo(String destination) {
        System.out.println("Navigating to: " + destination);
    }
}

// Composition: SmartCar HAS-AN Engine and HAS-A GpsNavigator
public class SmartCar {
    private final Engine engine;
    private final GpsNavigator gps;

    public SmartCar(Engine engine, GpsNavigator gps) {
        this.engine = engine;
        this.gps = gps;
    }

    public void driveTo(String destination) {
        engine.start();
        gps.navigateTo(destination);
        System.out.println("SmartCar is autonomously driving...");
    }
}
```

---

#### F. Modern Java OOP Enhancements: Records & Sealed Classes

##### 1. Records (Java 16+)
* **Concept**: Immutable, transparent data carriers with auto-generated `equals()`, `hashCode()`, `toString()`, constructor, and getters.
* **Utility**: Replaces boilerplate POJOs/DTOs with zero runtime overhead.
```java
public record UserDto(String username, String email) {
    // Compact constructor for invariant validation
    public UserDto {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
```

##### 2. Sealed Classes & Interfaces (Java 17+)
* **Concept**: Limits which classes or interfaces can extend or implement them using `permits`.
* **Utility**: Domain model protection and **exhaustive pattern matching** in `switch` expressions without requiring a `default` case.
```java
public sealed interface Shape permits Circle, Rectangle {}
public final class Circle implements Shape { public double radius() { return 5.0; } }
public non-sealed class Rectangle implements Shape { public double width() { return 4.0; } public double height() { return 6.0; } }

// Exhaustive pattern matching (Java 21)
public static String describeShape(Shape shape) {
    return switch (shape) {
        case Circle c    -> "Circle with radius: " + c.radius();
        case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
    };
}
```

---

### Summary: Comparison Matrix of Core OOP Concepts

| Concept | Primary Keyword / Mechanism | Core Purpose | Best Used When |
| :--- | :--- | :--- | :--- |
| **Encapsulation** | `private` fields + `getters`/`setters` | Data protection & invariant validation | Building domain models with valid state rules |
| **Inheritance** | `extends` | Code reuse & hierarchical classification | Modeling true `is-a` domain relationships |
| **Polymorphism** | Overloading (same name, diff params) / Overriding (`@Override`) | Flexibility & unified caller interface | Designing plug-and-play components & extensible handlers |
| **Abstraction** | `abstract class` / `interface` | Hiding operational details behind contracts | Standardizing behaviors across disparate systems |
| **Composition** | Instance field references (`has-a`) | Loose coupling & component assembly | Preferred default over inheritance for modular design |
| **Records** | `record` (Java 16+) | Immutable data carrier (DTO) | Storing immutable state with automatic boilerplate generation |
| **Sealed Classes** | `sealed ... permits` (Java 17+) | Restricting class hierarchies | Defining closed set of domain types for pattern matching |

---

### 2. Core API Essentials
* **String Immutability & String Pool**:
  * Strings are immutable (`byte[]` array + encoding flag since Java 9).
  * `String s = "hello"` places literals in the **String Constant Pool** (part of Metaspace/Heap depending on Java version; currently stored in Heap).
  * `s.intern()` ensures explicit pooling.
  * Use `StringBuilder` (non-thread-safe, fast) or `StringBuffer` (thread-safe, synchronized) for dynamic string operations.
* **Exceptions Hierarchy**:
  * `Throwable` -> `Error` (Unrecoverable: `OutOfMemoryError`, `StackOverflowError`) and `Exception`.
  * `Checked Exception`: Inherits from `Exception` (must be declared/caught).
  * `Unchecked Exception`: Inherits from `RuntimeException` (logical/programming bugs).
  * **Try-With-Resources**: Auto-closes resources implementing `AutoCloseable` in reverse order of declaration.

### 3. Collections Framework Overview
| Data Structure | Implementation | Time Complexity (Search / Insert) | Ordering / Key Property |
| :--- | :--- | :--- | :--- |
| **List** | `ArrayList` | $O(1)$ random access, $O(n)$ insertion/deletion | Dynamic array |
| **List** | `LinkedList` | $O(n)$ search, $O(1)$ head/tail insertion | Doubly linked list, high memory overhead |
| **Set** | `HashSet` | $O(1)$ average search & insertion | Backed by `HashMap`, unordered |
| **Set** | `TreeSet` | $O(\log n)$ | Backed by `TreeMap` (Red-Black tree), sorted |
| **Map** | `HashMap` | $O(1)$ average, $O(\log n)$ worst (treeified) | Key-value pairs, hash collision handling |
| **Map** | `TreeMap` | $O(\log n)$ | Sorted key-value pairs |
| **Queue** | `ArrayDeque` / `PriorityQueue` | $O(1)$ offer/poll / $O(\log n)$ heap | Double-ended queue / Binary Heap |

* **Fail-Fast vs Fail-Safe Iterators**:
  * **Fail-Fast** (`ArrayList`, `HashMap` iterators): Throw `ConcurrentModificationException` via structural modification counter `modCount`.
  * **Fail-Safe / Weakly-Consistent** (`ConcurrentHashMap`, `CopyOnWriteArrayList`): Iterate over snapshot or concurrent buckets without throwing exceptions.

---

## Phase 2: Intermediate Concepts

### 1. Functional Programming & Stream API
* **Functional Interfaces**: `@FunctionalInterface` with exactly one abstract method.
  * `Function<T, R>`: `R apply(T t)`
  * `Predicate<T>`: `boolean test(T t)`
  * `Supplier<T>`: `T get()`
  * `Consumer<T>`: `void accept(T t)`
* **Stream API Internals**:
  * **Intermediate Operations** (`map`, `filter`, `flatMap`): Lazy execution; build a pipeline of stream stages (`AbstractPipeline`).
  * **Terminal Operations** (`collect`, `reduce`, `forEach`): Trigger evaluation of pipeline.
  * **Parallel Streams**: Uses `ForkJoinPool.commonPool()`. Beware of shared state and blocking I/O in parallel streams.

### 2. Generics & Type System Internals
* **Type Erasure**: Generics are a compile-time feature. Types are erased to `Object` or upper bound (e.g., `Comparable`) at bytecode level. Bridge methods are generated by the compiler to preserve covariance.
* **PECS Rule (Producer Extends, Consumer Super)**:
  * `? extends T` (Upper Bound): Read-only from producer list (`T item = list.get(i)`). Cannot add elements (except `null`).
  * `? super T` (Lower Bound): Write-only into consumer list (`list.add(t)`).

### 3. Java I/O vs NIO / NIO.2
* **Classic I/O**: Blocking stream-oriented (`InputStream`, `OutputStream`).
* **NIO (Non-blocking I/O)**: Buffer-oriented (`ByteBuffer`), Channel-oriented (`FileChannel`, `SocketChannel`), and `Selector`-based multiplexing.
* **NIO.2 (`java.nio.file`)**: Asynchronous File I/O (`AsynchronousFileChannel`), `Path`, `Files`, and `WatchService`.

---

## Phase 3: Deep-Dive Advanced Topics

### A. JVM Architecture & Java Memory Model (JMM)

#### 1. JVM Runtime Data Areas
```
+-------------------------------------------------------------------+
|                        JVM Runtime Memory                         |
+-------------------------------------------------------------------+
|               Shared Across Threads               |   Per-Thread  |
+--------------------------------------------------+---------------+
|  Heap Memory                                     | Thread Stack  |
|  +-----------------------+--------------------+  | (Frames,      |
|  | Young Gen (Eden, S0,S1)| Old Gen (Tenured)  |  |  Local Vars,  |
|  +-----------------------+--------------------+  |  Operand Stk) |
+--------------------------------------------------+---------------+
|  Metaspace (Native Memory, Class Metadata)       | PC Register   |
+--------------------------------------------------+---------------+
|  Code Cache (JIT Compiled Native Code)           | Native Stack  |
+--------------------------------------------------+---------------+
```

* **Heap**: Stores all object instances and arrays.
  * **Young Generation**:
    * **Eden Space**: New objects allocated here (via Thread-Local Allocation Buffers - TLAB).
    * **Survivor Spaces (S0 / S1)**: Objects surviving Minor GC rotate between S0 and S1 with an incremental tenure threshold.
  * **Old (Tenured) Generation**: Long-lived objects promoted when tenure threshold is hit or during large object direct allocations.
* **Metaspace**: Native memory holding class metadata, method structures, field data, and constant pools. Replaced PermGen in Java 8.
* **JVM Stacks**: Each thread has its own execution stack storing frame structures for methods (local variables, operand stack, reference frame).

#### 2. Java Memory Model (JMM) Internals
The JMM defines how threads interact through main memory and CPU local caches (L1/L2/L3).

```
[ Thread 1 CPU Core ] ----> [ Local Cache ] 
                                  |
                           (Volatile Barrier / Flush)
                                  v
[ Main Memory (Shared Objects / Fields) ]
                                  ^
                           (Volatile Barrier / Invalidate)
                                  |
[ Thread 2 CPU Core ] ----> [ Local Cache ]
```

* **Instruction Reordering**: Compiler, JIT, and CPU hardware reorder instructions for performance optimization, provided single-threaded *as-if-serial* semantics are preserved.
* **Happens-Before Relationship**:
  * **Program Order Rule**: Each action in a thread happens-before subsequent actions in program order.
  * **Monitor Lock Rule**: An unlock on a monitor happens-before every subsequent lock on that monitor.
  * **Volatile Variable Rule**: A write to a `volatile` variable happens-before every subsequent read of that `volatile`.
  * **Thread Start/Join Rules**: `Thread.start()` happens-before any action in the started thread.
* **Volatile Mechanics**:
  * Guarantees **Visibility** and **Instruction Ordering** (prevents reordering via Memory Barriers: `LoadLoad`, `LoadStore`, `StoreStore`, `StoreLoad`).
  * **Does NOT guarantee Atomicity** for compound operations (e.g., `count++` requires synchronization or atomic variables).

---

### B. Garbage Collection Mechanics & Tuning

#### 1. Garbage Collection Phases & Algorithms
* **Mark Phase**: Traverses GC Roots (Thread stack local vars, static references, JNI references) to mark reachable objects.
* **Sweep Phase**: Reclaims memory of unmarked objects.
* **Compact Phase**: Relocates live objects to eliminate heap fragmentation.

#### 2. Garbage Collectors Comparison

| Collector | Target Use Case | GC Strategy | Pause Characteristics |
| :--- | :--- | :--- | :--- |
| **Serial GC** | Single-threaded / CLI apps | Stop-the-World (STW) Mark-Sweep-Compact | High pause times |
| **Parallel GC** | Throughput-focused batch workloads | Multi-threaded Young & Old generation STW | High throughput, predictable pause |
| **G1 GC** | Default collector (Java 9+), large heaps | Region-based (1MB–32MB regions), incremental compaction | Configurable pause-time targets (`-XX:MaxGCPauseMillis`) |
| **ZGC** | Ultra-low latency, multi-terabyte heaps | Colored Pointers & Load Barriers, concurrent marking/relocation | Sub-millisecond pause times regardless of heap size |
| **Shenandoah** | Ultra-low latency | Concurrent evacuation using Brooks Pointers / Load Reference Barriers | Ultra-low pause times |

#### 3. Essential GC Tuning & Monitoring JVM Flags
```bash
# Set Memory Limits
-Xms4g -Xmx4g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m

# GC Selection
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=45

# GC Logging (Unified JVM Logging in Java 9+)
-Xlog:gc*,gc+phases=debug:file=/var/log/gc.log:time,uptime,pid:filecount=5,filesize=50M

# Diagnostic Options on OutOfMemoryError
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/dumps/heap_oom.hprof
```

---

### C. Deep-Dive Concurrency & Multithreading

#### 1. `synchronized` Internals & Lock Escalation
Every Java object has an **Object Header** containing a **Mark Word** (32/64 bits) storing hash code, GC age, and lock states.

```
Lock State Escalation Pipeline:
[ Unlocked ] ---> [ Biased Lock ] ---> [ Lightweight Lock ] ---> [ Heavyweight Lock ]
                    (Thread ID)         (CAS Spinlock)            (OS Mutex / Monitor)
```

1. **Biased Lock**: Locks biased towards the first thread that acquires it (bypasses CAS). *Note: Deprecated/Disabled in recent JDKs*.
2. **Lightweight Lock**: When another thread contends, the lock escalates. Contending threads use **CAS (Compare-And-Swap) Spinlocks** to copy the Mark Word to their Stack Frame (Displaced Mark Word).
3. **Heavyweight Lock**: If CAS spinning fails or contention is high, the lock inflates to a Heavyweight Lock backed by OS Mutex and Monitor (`ObjectMonitor`). Contending threads are placed into an entry list and suspended/parked.

#### 2. Advanced Locks & Explicit Synchronization
* **`ReentrantLock`**:
  * Implemented via **AbstractQueuedSynchronizer (AQS)**.
  * Features: Interruptible lock acquisition (`lockInterruptibly()`), Lock timeout (`tryLock()`), Fair vs Non-fair queuing, and Multiple `Condition` queues.
* **`StampedLock` (Java 8+)**:
  * Provides Optimistic Reading via validation stamps (`tryOptimisticRead()`), avoiding reader blocking when writes are rare.
  ```java
  public class OptimisticPoint {
      private double x, y;
      private final StampedLock sl = new StampedLock();

      public double distanceFromOrigin() {
          long stamp = sl.tryOptimisticRead(); // Non-blocking read
          double currentX = x, currentY = y;
          if (!sl.validate(stamp)) { // Check if a write occurred
              stamp = sl.readLock(); // Upgrade to pessimistic read lock
              try {
                  currentX = x;
                  currentY = y;
              } finally {
                  sl.unlockRead(stamp);
              }
          }
          return Math.sqrt(currentX * currentX + currentY * currentY);
      }
  }
  ```

#### 3. Concurrent Collections Internals
* **`ConcurrentHashMap`**:
  * Bucket-level synchronization using `CAS` for empty bucket insertion, and `synchronized` on the head node of the bucket bin for non-empty buckets.
  * Bins treeify into Red-Black Trees (`TreeBin`) when bin count exceeds 8 (`TREEIFY_THRESHOLD`).
  * Lock-free reads without locking.
* **Atomics & `LongAdder`**:
  * `AtomicInteger` / `AtomicReference`: Use hardware-level CAS instructions (`Unsafe` / `VarHandle`).
  * `LongAdder` / `DoubleAdder`: Under high contention, distributes counter updates across an array of `Cell` instances to prevent CAS CPU cache line bouncing.

#### 4. Thread Pools (`ThreadPoolExecutor`) Deep Dive
```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,       // Minimum active threads
    maximumPoolSize,    // Maximum active threads when queue is full
    keepAliveTime,      // Idle thread survival time
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100), // Bounded Work Queue
    new ThreadFactoryBuilder().setNameFormat("app-worker-%d").build(),
    new ThreadPoolExecutor.CallerRunsPolicy() // Rejection Handler Strategy
);
```

**Task Rejection Policies**:
1. `AbortPolicy` (Default): Throws `RejectedExecutionException`.
2. `CallerRunsPolicy`: Runs task on caller thread (provides backpressure).
3. `DiscardPolicy`: Silently drops task.
4. `DiscardOldestPolicy`: Drops oldest task in queue and retries execution.

---

### D. Virtual Threads & Project Loom (Java 21+)

#### 1. Platform Threads vs Virtual Threads
* **Platform Threads**: Wraps OS threads 1:1. Memory footprint ~1MB stack size. Creation cost is high. Limited to thousands per process.
* **Virtual Threads**: Managed by the JVM (`java.lang.Thread` instance). Stack stored on Heap. Millions can run concurrently.

```
[ Virtual Threads ]   VT1   VT2   VT3   VT4   VT5 ... (Millions)
                        \    |     /     |     /
[ Carrier Threads ]      Platform Thread 1 (ForkJoinPool)
                             |
[ OS Threads ]           Kernel Thread
```

#### 2. Virtual Thread Execution & Unpinning
* Virtual threads are mounted on **Carrier Threads** (a FIFO `ForkJoinPool`).
* When blocking I/O (e.g., socket read, file I/O, `Thread.sleep`) occurs, the JVM **unmounts** the virtual thread, saving its stack frame on the heap and freeing the carrier thread.
* **Thread Pinning Warning**: A virtual thread cannot be unmounted if blocking occurs inside a `synchronized` block/method or native call.
  * *Best Practice*: Replace `synchronized` with `ReentrantLock` in high-throughput virtual thread applications.

```java
// Creating Virtual Threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> 
        executor.submit(() -> {
            // Unmounts carrier thread during blocking network call
            String response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return response;
        })
    );
} // Auto-closes and awaits completion
```

---

### E. Modern Java Features (Java 8 to 21+)

#### 1. Pattern Matching Evolution
```java
// Pattern Matching for instanceof (Java 16)
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}

// Pattern Matching for switch & Record Patterns (Java 21)
public String processShape(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "Large Circle";
        case Circle c                      -> "Small Circle";
        case Rectangle(double w, double h) -> "Rectangle area: " + (w * h);
        case null                          -> "Null shape";
    };
}
```

#### 2. Modern Language Syntax Refinements
* **Switch Expressions (Java 14)**: Arrow syntax (`->`), yield values from blocks, non-fallthrough semantics.
* **Text Blocks (Java 15)**: Multiline string literals with preserved indentation (`"""`).
* **Var Variable Binding (Java 10)**: Local variable type inference.
* **Scoped Values & Structured Concurrency (Preview Java 21+)**: Safe, immutable context sharing across threads and structured subtask lifecycle management.

---

## Phase 4: Design Patterns, Memory Leaks & Performance Tuning

### 1. Production Design Patterns in Modern Java

#### A. Double-Checked Locking Singleton (Thread-Safe)
```java
public class CacheManager {
    // volatile is mandatory to prevent instruction reordering during instantiation
    private static volatile CacheManager instance;

    private CacheManager() {}

    public static CacheManager getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (CacheManager.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }
}
```

#### B. Enum Singleton (Best Practice - Defensive against reflection & serialization)
```java
public enum SingletonRegistry {
    INSTANCE;
    
    public void doWork() {
        // Business logic
    }
}
```

#### C. Dynamic Proxy & Decoupled Handlers
```java
public interface Service { void execute(); }

public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    public LoggingInvocationHandler(Object target) { this.target = target; }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("After " + method.getName());
        return result;
    }
}

// Usage
Service proxy = (Service) Proxy.newProxyInstance(
    Service.class.getClassLoader(),
    new Class<?>[]{Service.class},
    new LoggingInvocationHandler(new ServiceImpl())
);
```

---

### 2. Identifying & Diagnosing Memory Leaks

#### Common Memory Leak Causes in Java
1. **Static Collections**: Unbounded `static Map` / `List` holding references indefinitely.
2. **Unclosed Resources**: Unclosed database connections, streams, or listeners.
3. **`ThreadLocal` Retention**: Uncleared `ThreadLocal` values in application servers with reused thread pool threads (causes Metaspace / Heap leaks).
4. **Overriding `equals()` without `hashCode()`**: Causes duplicate object entries in `HashSet` / `HashMap`.

#### Diagnostic Tooling & Commands
* **Capture Thread Dump**:
  ```bash
  jstack <PID> > thread_dump.txt
  # Or using jcmd
  jcmd <PID> Thread.print > thread_dump.txt
  ```
* **Capture Heap Dump**:
  ```bash
  jcmd <PID> GC.heap_dump /tmp/heap_dump.hprof
  ```
* **Analyze Heap Dumps**: Use **Eclipse MAT (Memory Analyzer Tool)** or **VisualVM** to inspect Object Histogram, Dominator Tree, and Incoming/Outgoing references.

---

### 3. JIT Compiler Internals & Performance Optimization

```
Bytecode (.class) ---> Interpreter
                            |
                     [ Execution Counter Threshold Hit ]
                            |
                   +--------+--------+
                   |                 |
                C1 Compiler      C2 Compiler
             (Client, Fast Comp) (Server, Heavy Opt)
                   |                 |
                   +--------+--------+
                            |
                     Native Code Block
```

* **Tiered Compilation**:
  * **Level 0**: Interpreted Code.
  * **Level 1-3 (C1 / Client Compiler)**: Fast compilation with basic profiling.
  * **Level 4 (C2 / Server Compiler)**: High-level optimizations (Inlining, Escape Analysis, Loop Unrolling, Vectorization).
* **Escape Analysis Optimizations**:
  * **Scalar Replacement**: If an object does not escape a method, the JVM replaces object fields with primitive local variables on the stack (avoids heap allocation!).
  * **Lock Elision**: Removes synchronization blocks if the locked object is proved to not escape the executing thread.
* **Java Microbenchmark Harness (JMH)**:
  * Avoid writing manual loops with `System.currentTimeMillis()` due to JIT dead-code elimination and warmup distortions. Always use JMH for benchmark tests.

---

## Quick Revision Checklist for Java Interviews & Architecture

- [ ] Can you explain the **Happens-Before** relationship in JMM and why `volatile` prevents reordering?
- [ ] How does **G1 GC** differ from **ZGC** regarding pause times and colored pointers?
- [ ] What is lock escalation in `synchronized` (`Unlocked` -> `Biased` -> `Lightweight` -> `Heavyweight`)?
- [ ] Why do **Virtual Threads** unmount on blocking I/O, and what causes **Thread Pinning**?
- [ ] How does `ConcurrentHashMap` handle concurrent writes without locking the entire map?
- [ ] What is the **PECS** rule in Java Generics and when do you use `? super T`?
- [ ] How does **JIT Escape Analysis** perform scalar replacement to allocate memory on the stack?
- [ ] How do **Records** and **Sealed Classes** enable algebraic data types and pattern matching in modern Java?
