/*
 * =====================================================================================
 *  LIBRARY MANAGER - plain Java console app (no frameworks, no database)
 * =====================================================================================
 *
 *  HOW TO USE THIS FILE
 *  --------------------
 *  This one file holds EVERYTHING: the README text, every class, and a "WHY" explanation
 *  for every class and method. Java allows many top-level classes in one file as long as
 *  only ONE of them is public and its name matches the file name (Main -> Main.java).
 *
 *  Run it:        javac Main.java
 *                 java Main
 *
 *  For GitHub, split it into one file per class (Book.java, Member.java, Loan.java,
 *  Library.java, the six exception files, Validation.java, the two comparators and
 *  Main.java). Put "public" in front of each class when you do. Copy the README section
 *  below into README.md.
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 1 - FEATURES
 * -------------------------------------------------------------------------------------
 *   1.  Add book            (validates every field, rejects duplicate ISBN)
 *   2.  Register member     (validates fields, rejects duplicate member id)
 *   3.  Issue book          (book exists, member exists, not already held,
 *                            copy available, borrow limit of 3 not reached)
 *   4.  Return book         (restores the copy, removes the loan, reports lateness)
 *   5.  Search books        (title or author, case-insensitive, partial match)
 *   6.  List all books      (sorted by title OR by author)
 *   7.  List books by genre (genres in alphabetical order)
 *   8.  Show overdue loans
 *   9.  Show top 3 most borrowed books
 *   10. Show a member's current loans
 *   11. Exit
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 2 - WHICH COLLECTION IS USED FOR WHAT, AND WHY
 * -------------------------------------------------------------------------------------
 *  Collection                          Where        Purpose                      Why this one
 *  ----------------------------------  -----------  ---------------------------  ---------------------------------
 *  Map<String,Book>  (HashMap)         Library      ISBN -> Book                 O(1) lookup by key. We look up a
 *                                                                                book on every issue/return.
 *  Map<String,Member> (HashMap)        Library      memberId -> Member           Same reason: O(1) lookup by id.
 *  Set<String> (HashSet)               Member       ISBNs the member holds       A Set cannot hold duplicates, so
 *                                                                                "same book twice" is blocked by
 *                                                                                the data structure itself, and
 *                                                                                contains() is O(1).
 *  List<Loan> (ArrayList)              Library      all active loans             Simple ordered list; we only ever
 *                                                                                scan it, never look up by key.
 *  SortedMap<LocalDate,List<Loan>>     Library      loans grouped by due date    A TreeMap keeps keys sorted, so
 *    (TreeMap)                                                                   headMap(today) returns every key
 *                                                                                strictly before today = overdue,
 *                                                                                without checking every loan.
 *  Map<String,Integer> (HashMap)       Library      ISBN -> times borrowed       Counter table; O(1) increment.
 *  Queue<Map.Entry> (PriorityQueue)    Library      top 3 most borrowed          A min-heap capped at size 3 keeps
 *                                                                                only the 3 best while scanning the
 *                                                                                counts once: O(n log 3).
 *  Map<String,List<Book>> (TreeMap)    Library      genre -> books               TreeMap iterates keys in
 *                                                                                alphabetical order for free.
 *
 *  Interface types (Map, List, Set, Queue, SortedMap) are used on the LEFT side of every
 *  declaration. WHY: the rest of the code depends on "what it can do", not "how it is
 *  built", so you can swap HashMap for LinkedHashMap later by changing ONE word.
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 3 - EXCEPTION HIERARCHY
 * -------------------------------------------------------------------------------------
 *   Throwable
 *    +-- Exception                          (CHECKED: compiler forces you to handle)
 *    |    +-- BookNotFoundException
 *    |    +-- MemberNotFoundException
 *    |    +-- BookUnavailableException
 *    |    +-- BorrowLimitExceededException
 *    +-- RuntimeException                   (UNCHECKED: no forced handling)
 *         +-- InvalidInputException         (blank title, negative copies, bad input)
 *         +-- DuplicateEntryException       (book / member / loan already exists)
 *
 *  WHY two kinds?
 *   - CHECKED = a normal business situation the caller can recover from ("book not
 *     found", "no copies left"). Making them checked forces Main to deal with them, so
 *     you can never forget to show the librarian a friendly message.
 *   - UNCHECKED = the caller sent bad data or broke a rule that should have been
 *     prevented ("blank title", "duplicate ISBN"). Forcing try/catch around every
 *     addBook() call would add noise, so these extend RuntimeException.
 *   - Library throws, only Main catches and prints. WHY: Library has no System.out, so
 *     it can be reused in a GUI, web app or unit test without changes.
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 4 - MANUAL TEST CASES (works with the seed data)
 * -------------------------------------------------------------------------------------
 *  Seed data: 12 books (B101..B112), members M001 Asha, M002 Ravi, M003 Meera.
 *  M001 already holds B101 and it is 6 DAYS OVERDUE. M002 holds B103, the ONLY copy.
 *
 *  1. Duplicate ISBN       -> option 1, ISBN B101                -> DuplicateEntryException
 *  2. Zero copies left     -> option 3, B103 to M003             -> BookUnavailableException
 *  3. Fourth borrow        -> option 3, give M003 B102, B104, B105, then B106
 *                                                                -> BorrowLimitExceededException
 *  4. Same book twice      -> option 3, B102 to M003 twice       -> rejected (DuplicateEntryException)
 *  5. Return never issued  -> option 4, B106 from M003           -> clear "no active loan" error
 *  6. Overdue list         -> option 8                           -> shows B101 / Asha, 6 days late
 *  7. equals/hashCode      -> see the self-check in the comment above class Book
 *  8. Letters for number   -> type "abc" at any number prompt    -> re-prompts, no crash
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 5 - BUILD ORDER (4 days)
 * -------------------------------------------------------------------------------------
 *  Day 1: Validation, exceptions, Book, Member, Loan, addBook, registerMember
 *  Day 2: issueBook, returnBook (all exception paths)
 *  Day 3: search, sorted listings, genre view, overdue report, top 3 report
 *  Day 4: Main input validation, cleanup, README, manual testing
 *
 * -------------------------------------------------------------------------------------
 *  README SECTION 6 - HOW TO REUSE THIS DESIGN FOR YOUR OWN PROJECTS
 * -------------------------------------------------------------------------------------
 *  Every console "manager" app (inventory, hospital, bank, hotel...) follows this recipe:
 *
 *   1. NOUNS become model classes (Book, Member, Loan). Give each one private fields, a
 *      constructor that validates, and equals/hashCode on its IDENTITY field only.
 *   2. For every "what can go wrong?" write down an exception. Ask: "can the caller
 *      recover?" yes -> checked, "caller sent garbage / broke a rule" -> unchecked.
 *   3. VERBS become methods on ONE service class (Library). All rules live there; it
 *      never prints and never reads input.
 *   4. For every question the app must answer, pick the collection that answers it
 *      fastest:
 *        "find by id"           -> HashMap
 *        "no duplicates"        -> HashSet
 *        "keep in order"        -> TreeMap / sort a List
 *        "everything before X"  -> TreeMap.headMap(X)
 *        "top N"                -> PriorityQueue capped at N
 *        "just keep a list"     -> ArrayList
 *   5. Keep several indexes in sync inside ONE method (e.g. issueBook updates the loan
 *      list, the due-date index, the counter and the member set together). If you
 *      update one index and forget another, your reports silently go wrong.
 *   6. Main only: print menu, read text, parse numbers, call Library, catch exceptions,
 *      print messages.
 *   7. Seed data + a written test checklist = you can test in 2 minutes after each change.
 *
 *  STRETCH-GOAL HINTS
 *   - Late fee: returnBook already returns "days late". Fee = daysLate * ratePerDay.
 *   - Reservation queue: add Map<String, Deque<String>> (isbn -> waiting memberIds,
 *     ArrayDeque). On returnBook, if the deque is not empty, poll() the next member.
 *   - Save/load: write one line per book/member/loan with a separator ("|"), read
 *     back with BufferedReader and rebuild through addBook / registerMember / issueBook
 *     so that all validation and all indexes are re-used.
 * =====================================================================================
 */

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

// =====================================================================================
//  PART 1 - CUSTOM EXCEPTIONS
// =====================================================================================
//  Every exception has the same two constructors:
//    (String message)                  -> a clear human-readable message
//    (String message, Throwable cause) -> keeps the ORIGINAL error attached
//  WHY the cause constructor: when you catch a low-level error and re-throw your own,
//  the stack trace still shows the real root cause. Without it, debugging is guesswork.
// =====================================================================================

/** CHECKED. Thrown when a book (or a loan of a book) cannot be found. */
class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

/** CHECKED. Thrown when a member id is not registered. */
class MemberNotFoundException extends Exception {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

/** CHECKED. Thrown when a book exists but has no copies left to lend. */
class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }

    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

/** CHECKED. Thrown when a member already holds the maximum number of books. */
class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) {
        super(message);
    }

    public BorrowLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}

/** UNCHECKED. Blank title, negative copies, empty search text, and so on. */
class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}

/** UNCHECKED. A book / member / loan that already exists is being created again. */
class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}

// =====================================================================================
//  PART 2 - SMALL HELPERS
// =====================================================================================

/**
 * Validation - one place for the "is this text usable?" rule.
 *
 * WHY a helper class: Book, Member and Library all need "reject blank text and trim
 * it". Writing that check three times means three places to fix when the rule changes
 * (DRY = Don't Repeat Yourself). The private constructor stops anyone from creating an
 * object of a class that only has static methods. Static utility methods hold no state,
 * so this does not break the "no static global state" rule.
 */
final class Validation {
    private Validation() {
    }

    /** Returns the trimmed text, or throws InvalidInputException if null/blank. */
    static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}

/**
 * AuthorComparator - the "sorted by author" ordering.
 *
 * WHY a separate Comparator: Book can only have ONE natural order (Comparable = by
 * title). Any other ordering is supplied from outside as a Comparator. Ties on author
 * fall back to title so the output is stable and predictable.
 */
class AuthorComparator implements Comparator<Book> {
    @Override
    public int compare(Book a, Book b) {
        int byAuthor = a.getAuthor().compareToIgnoreCase(b.getAuthor());
        if (byAuthor != 0) {
            return byAuthor;
        }
        return a.compareTo(b);
    }
}

/**
 * BorrowCountComparator - ordering used by the "top 3" PriorityQueue.
 *
 * WHY: a PriorityQueue is a MIN-heap, meaning poll() removes the SMALLEST element. We
 * keep the heap capped at 3, so whenever it grows to 4 we poll() the smallest, which is
 * exactly the one we want to throw away. Smallest = fewest borrows. On a tie we treat
 * the HIGHER ISBN as smaller, so it is evicted first and ties resolve to the lower
 * ISBN (deterministic output instead of random HashMap order).
 */
class BorrowCountComparator implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
        int byCount = a.getValue().compareTo(b.getValue());
        if (byCount != 0) {
            return byCount;
        }
        return b.getKey().compareTo(a.getKey());
    }
}

// =====================================================================================
//  PART 3 - MODEL CLASSES
// =====================================================================================

/**
 * Book - one catalog entry (not one physical copy; copies are just counters).
 *
 * WHY equals/hashCode on isbn ONLY: two Book objects describing the same ISBN are the
 * same book, even if the title was typed differently or the copy counts differ. A
 * HashSet/HashMap decides "same or different" with hashCode() then equals(), so basing
 * both on the same single field makes two such objects collapse into one entry.
 *
 * Self-check for test case 7 (put in a scratch main to try it):
 *   Book a = new Book("X1", "T", "A", "G", 1);
 *   Book b = new Book("X1", "Other title", "B", "H", 5);
 *   a.equals(b)                  -> true
 *   new HashSet<>(List.of(a, b)).size() -> 1
 *
 * WHY Comparable<Book> (by title): gives Book a "natural order" so
 * Collections.sort(listOfBooks) works with no extra argument.
 */
class Book implements Comparable<Book> {
    private final String isbn;
    private final String title;
    private final String author;
    private final String genre;
    private final int totalCopies;
    private int availableCopies; // the ONLY field that changes after creation

    /**
     * WHY validate in the constructor: an invalid Book can then never exist anywhere in
     * the program. You never need "is this book valid?" checks later.
     * Fields that never change are final, so they cannot be altered by accident.
     */
    public Book(String isbn, String title, String author, String genre, int totalCopies) {
        this.isbn = Validation.requireNonBlank(isbn, "ISBN");
        this.title = Validation.requireNonBlank(title, "Title");
        this.author = Validation.requireNonBlank(author, "Author");
        this.genre = Validation.requireNonBlank(genre, "Genre");
        if (totalCopies < 0) {
            throw new InvalidInputException("Total copies must not be negative (got " + totalCopies + ").");
        }
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies; // a new book starts fully on the shelf
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Take one copy off the shelf.
     * WHY here and not in Library: the rule "available copies can never go below 0" is
     * about Book's own data, so Book protects it. Library checks first and throws a
     * friendly checked exception; this guard is a safety net for programmer mistakes
     * (hence IllegalStateException, a "this should never happen" error).
     */
    void checkOut() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No copies of " + isbn + " are available to check out.");
        }
        availableCopies--;
    }

    /** Put one copy back. Same reasoning: available can never exceed total. */
    void checkIn() {
        if (availableCopies >= totalCopies) {
            throw new IllegalStateException("All copies of " + isbn + " are already on the shelf.");
        }
        availableCopies++;
    }

    /**
     * Natural order = by title, ignoring case, ISBN as tie-breaker.
     * WHY the tie-breaker: compareTo should be consistent with equals. Two different
     * books with the same title must not compare as 0, otherwise sorted collections
     * (TreeSet/TreeMap) would wrongly treat them as duplicates.
     */
    @Override
    public int compareTo(Book other) {
        int byTitle = this.title.compareToIgnoreCase(other.title);
        if (byTitle != 0) {
            return byTitle;
        }
        return this.isbn.compareTo(other.isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book other = (Book) o;
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode(); // MUST use exactly the fields equals() uses
    }

    /** WHY: so System.out.println(book) is readable instead of "Book@1b6d3586". */
    @Override
    public String toString() {
        return "[" + isbn + "] " + title + " by " + author + " (" + genre + ") - "
                + availableCopies + "/" + totalCopies + " available";
    }
}

/**
 * Member - a person who can borrow.
 *
 * WHY the borrowed ISBNs are a Set: a Set cannot contain duplicates, and contains() is
 * O(1). "Does this member already hold this book?" is a single call.
 * WHY the limit lives here: "a member may hold at most 3 books" is a fact about Member.
 */
class Member {
    public static final int MAX_BORROWED_BOOKS = 3; // a constant, not changeable state

    private final String memberId;
    private final String name;
    private final Set<String> borrowedBooks = new HashSet<>();

    public Member(String memberId, String name) {
        this.memberId = Validation.requireNonBlank(memberId, "Member id");
        this.name = Validation.requireNonBlank(name, "Member name");
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    /**
     * WHY unmodifiableSet: if we returned the real set, any caller could do
     * member.getBorrowedBooks().add("X") and bypass every rule in Library (limit,
     * counters, loan list). The unmodifiable VIEW still shows live data but throws
     * UnsupportedOperationException on any change attempt.
     */
    public Set<String> getBorrowedBooks() {
        return Collections.unmodifiableSet(borrowedBooks);
    }

    public boolean hasBorrowed(String isbn) {
        return borrowedBooks.contains(isbn);
    }

    public boolean hasReachedLimit() {
        return borrowedBooks.size() >= MAX_BORROWED_BOOKS;
    }

    /** Package-private: only Library (same package) should change a member's books. */
    boolean addBorrowedBook(String isbn) {
        return borrowedBooks.add(isbn); // false if already there - the Set protects us
    }

    boolean removeBorrowedBook(String isbn) {
        return borrowedBooks.remove(isbn);
    }

    /** equals/hashCode on memberId only: the id IS the identity. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member other = (Member) o;
        return memberId.equals(other.memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }

    @Override
    public String toString() {
        return memberId + " - " + name + " (" + borrowedBooks.size() + "/" + MAX_BORROWED_BOOKS + " books)";
    }
}

/**
 * Loan - an immutable record that "member X borrowed book Y on date D, due on D+14".
 *
 * WHY immutable (all fields final): a loan is a historical fact. Nothing should edit
 * it; when the book comes back we delete the Loan instead of modifying it.
 * WHY LocalDate: it represents a calendar day without time or time zone, which is
 * exactly the precision a library needs, and it has built-in date arithmetic.
 */
class Loan {
    public static final int LOAN_PERIOD_DAYS = 14;

    private final String isbn;
    private final String memberId;
    private final LocalDate issueDate;
    private final LocalDate dueDate;

    public Loan(String isbn, String memberId, LocalDate issueDate) {
        this.isbn = Objects.requireNonNull(isbn, "isbn");
        this.memberId = Objects.requireNonNull(memberId, "memberId");
        this.issueDate = Objects.requireNonNull(issueDate, "issueDate");
        this.dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS); // computed ONCE, never typed by hand
    }

    public String getIsbn() {
        return isbn;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Overdue = today is strictly AFTER the due date (due today is still fine).
     * WHY this exact rule: TreeMap.headMap(today) is exclusive, so it returns due dates
     * strictly BEFORE today. The two definitions must match or reports disagree.
     * WHY "today" is a parameter and not LocalDate.now() inside: you can test with any
     * date you like instead of waiting 15 real days.
     */
    public boolean isOverdue(LocalDate today) {
        return today.isAfter(dueDate);
    }

    /** 0 if not late, otherwise whole days past the due date. */
    public long daysOverdue(LocalDate today) {
        if (!isOverdue(today)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, today);
    }

    /** Does this loan belong to this exact (book, member) pair? */
    public boolean matches(String isbn, String memberId) {
        return this.isbn.equals(isbn) && this.memberId.equals(memberId);
    }

    @Override
    public String toString() {
        return "Loan{isbn=" + isbn + ", member=" + memberId + ", issued=" + issueDate + ", due=" + dueDate + "}";
    }
}

// =====================================================================================
//  PART 4 - LIBRARY (THE SERVICE CLASS: ALL BUSINESS LOGIC LIVES HERE)
// =====================================================================================

/**
 * Library - owns every collection and enforces every rule.
 *
 * GOLDEN RULES of this class
 *   1. It NEVER prints and NEVER reads input (so it is reusable and testable).
 *   2. It THROWS exceptions; it never catches-and-hides them.
 *   3. Whenever data changes, ALL indexes that mention that data change in the same
 *      method (activeLoans, loansByDueDate, borrowCounts, Book/Member state).
 *   4. Fields are private and final; getters return unmodifiable views/copies.
 */
class Library {

    private static final int TOP_N = 3;

    // ISBN -> Book. WHY HashMap: constant-time lookup by key.
    private final Map<String, Book> books = new HashMap<>();

    // memberId -> Member. Same reasoning.
    private final Map<String, Member> members = new HashMap<>();

    // All loans currently out. WHY ArrayList: we only append and scan.
    private final List<Loan> activeLoans = new ArrayList<>();

    // dueDate -> loans due that day. WHY SortedMap/TreeMap: keys stay sorted, so
    // headMap(today) = "every due date before today" = overdue. The declared type is
    // SortedMap (not Map) because headMap() is defined on SortedMap.
    private final SortedMap<LocalDate, List<Loan>> loansByDueDate = new TreeMap<>();

    // ISBN -> how many times it was ever borrowed (never decreases on return).
    private final Map<String, Integer> borrowCounts = new HashMap<>();

    // genre -> books of that genre. WHY TreeMap with CASE_INSENSITIVE_ORDER: genres
    // list alphabetically AND "fantasy" / "Fantasy" land in the same group.
    private final Map<String, List<Book>> booksByGenre = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    // ---------------------------------------------------------------------------------
    //  DAY 1 - ADD BOOK, REGISTER MEMBER
    // ---------------------------------------------------------------------------------

    /**
     * Add a new book to the catalog.
     *
     * WHY the order: build the Book FIRST (its constructor validates every field and
     * throws InvalidInputException), THEN check for a duplicate. That way a blank title
     * is reported as "bad input", not as "duplicate".
     * WHY update booksByGenre here: indexes must be maintained at the single moment the
     * data is created, otherwise the genre view and the main map drift apart.
     * WHY sort the genre list: the genre report is then always in title order.
     */
    public Book addBook(String isbn, String title, String author, String genre, int totalCopies) {
        Book book = new Book(isbn, title, author, genre, totalCopies);
        if (books.containsKey(book.getIsbn())) {
            throw new DuplicateEntryException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        books.put(book.getIsbn(), book);

        List<Book> sameGenre = booksByGenre.get(book.getGenre());
        if (sameGenre == null) {
            sameGenre = new ArrayList<>();
            booksByGenre.put(book.getGenre(), sameGenre);
        }
        sameGenre.add(book);
        Collections.sort(sameGenre);
        return book;
    }

    /** Register a new member. Same pattern: validate (constructor), reject duplicate id, store. */
    public Member registerMember(String memberId, String name) {
        Member member = new Member(memberId, name);
        if (members.containsKey(member.getMemberId())) {
            throw new DuplicateEntryException("A member with id " + member.getMemberId() + " already exists.");
        }
        members.put(member.getMemberId(), member);
        return member;
    }

    // ---------------------------------------------------------------------------------
    //  DAY 2 - ISSUE AND RETURN
    // ---------------------------------------------------------------------------------

    /** Convenience overload: issue today. */
    public Loan issueBook(String isbn, String memberId)
            throws BookNotFoundException, MemberNotFoundException,
            BookUnavailableException, BorrowLimitExceededException {
        return issueBook(isbn, memberId, LocalDate.now());
    }

    /**
     * Issue a book to a member on a given date.
     *
     * WHY the date is a parameter in this overload: lets seed data and tests create
     * loans in the PAST, which is the only way to test "overdue" without waiting.
     *
     * CHECK ORDER and why:
     *   1. book exists            - nothing else makes sense without it
     *   2. member exists          - same
     *   3. member does not hold it - BEFORE availability, otherwise a member who holds
     *                               the last copy would be told "unavailable", which is
     *                               a misleading message
     *   4. copy available
     *   5. limit not reached
     * Every check happens BEFORE any change, so a failed issue leaves NO partial state.
     *
     * WHY DuplicateEntryException for "already holds it": it is a duplicate of an
     * existing loan, and a caller cannot "retry" it into success.
     */
    public Loan issueBook(String isbn, String memberId, LocalDate issueDate)
            throws BookNotFoundException, MemberNotFoundException,
            BookUnavailableException, BorrowLimitExceededException {

        Book book = findBook(isbn);
        Member member = findMember(memberId);

        if (member.hasBorrowed(book.getIsbn())) {
            throw new DuplicateEntryException(member.getName() + " already has \"" + book.getTitle() + "\" on loan.");
        }
        if (book.getAvailableCopies() == 0) {
            throw new BookUnavailableException("No copies of \"" + book.getTitle() + "\" are available right now.");
        }
        if (member.hasReachedLimit()) {
            throw new BorrowLimitExceededException(member.getName() + " already holds the maximum of "
                    + Member.MAX_BORROWED_BOOKS + " books.");
        }

        // --- all checks passed: now change every structure together ---
        Loan loan = new Loan(book.getIsbn(), member.getMemberId(), issueDate);

        activeLoans.add(loan);

        List<Loan> sameDueDate = loansByDueDate.get(loan.getDueDate());
        if (sameDueDate == null) {
            sameDueDate = new ArrayList<>();
            loansByDueDate.put(loan.getDueDate(), sameDueDate);
        }
        sameDueDate.add(loan);

        book.checkOut();
        member.addBorrowedBook(book.getIsbn());

        Integer current = borrowCounts.get(book.getIsbn());
        borrowCounts.put(book.getIsbn(), current == null ? 1 : current + 1);

        return loan;
    }

    /** Convenience overload: return today. */
    public long returnBook(String isbn, String memberId)
            throws BookNotFoundException, MemberNotFoundException {
        return returnBook(isbn, memberId, LocalDate.now());
    }

    /**
     * Return a book.
     *
     * RETURNS the number of days late (0 = on time). WHY return a number instead of
     * printing: Main decides the wording, and a later "late fee" feature just multiplies
     * this number by a rate.
     *
     * WHY BookNotFoundException when no loan exists: the "loan" the caller is asking
     * about cannot be found. (If you prefer, add a LoanNotFoundException; the rest of
     * the code would not change.)
     *
     * WHY Iterator + it.remove(): deleting from a list inside a for-each loop throws
     * ConcurrentModificationException. iterator.remove() is the safe way to delete the
     * current element mid-iteration.
     */
    public long returnBook(String isbn, String memberId, LocalDate returnDate)
            throws BookNotFoundException, MemberNotFoundException {

        Book book = findBook(isbn);
        Member member = findMember(memberId);

        Loan found = null;
        Iterator<Loan> iterator = activeLoans.iterator();
        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            if (loan.matches(book.getIsbn(), member.getMemberId())) {
                found = loan;
                iterator.remove(); // safe deletion during iteration
                break;
            }
        }
        if (found == null) {
            throw new BookNotFoundException("No active loan found: " + member.getName()
                    + " has not borrowed \"" + book.getTitle() + "\".");
        }

        removeFromDueDateIndex(found);
        book.checkIn();
        member.removeBorrowedBook(book.getIsbn());
        // NOTE: borrowCounts is NOT decreased. It counts "times borrowed ever".

        return found.daysOverdue(returnDate);
    }

    /**
     * Remove one loan from the due-date index.
     *
     * WHY removeIf: the other safe way to delete while iterating (it does the loop for
     * you). WHY drop the key when its list becomes empty: otherwise empty lists pile up
     * forever and headMap() keeps visiting dead entries.
     */
    private void removeFromDueDateIndex(Loan loan) {
        List<Loan> sameDueDate = loansByDueDate.get(loan.getDueDate());
        if (sameDueDate == null) {
            return;
        }
        sameDueDate.removeIf(candidate -> candidate.matches(loan.getIsbn(), loan.getMemberId()));
        if (sameDueDate.isEmpty()) {
            loansByDueDate.remove(loan.getDueDate());
        }
    }

    // ---------------------------------------------------------------------------------
    //  DAY 3 - SEARCH, SORTED LISTS, GENRE VIEW, REPORTS
    // ---------------------------------------------------------------------------------

    /**
     * Search by title OR author, case-insensitive, partial match.
     *
     * WHY lower-case both sides: "TOLK" must match "Tolkien". WHY contains(): partial
     * match. WHY scan every book: a substring search cannot use a HashMap key lookup.
     * WHY sort the result: so output order is stable, not HashMap's arbitrary order.
     * WHY InvalidInputException for blank text: an empty query would match everything,
     * which is almost certainly a mistake.
     */
    public List<Book> searchBooks(String query) {
        String needle = Validation.requireNonBlank(query, "Search text").toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            boolean titleMatches = book.getTitle().toLowerCase().contains(needle);
            boolean authorMatches = book.getAuthor().toLowerCase().contains(needle);
            if (titleMatches || authorMatches) {
                results.add(book);
            }
        }
        Collections.sort(results);
        return Collections.unmodifiableList(results);
    }

    /**
     * All books sorted by title (natural order).
     * WHY copy into a NEW list first: sorting changes the list, and books.values() is
     * the live internal collection. A copy keeps the internals untouched.
     */
    public List<Book> listBooksSortedByTitle() {
        List<Book> copy = new ArrayList<>(books.values());
        Collections.sort(copy);
        return Collections.unmodifiableList(copy);
    }

    /** All books sorted by author, using the external Comparator. */
    public List<Book> listBooksSortedByAuthor() {
        List<Book> copy = new ArrayList<>(books.values());
        Collections.sort(copy, new AuthorComparator());
        return Collections.unmodifiableList(copy);
    }

    /**
     * Books grouped by genre, genres alphabetical.
     * WHY build a fresh map with unmodifiable lists inside: wrapping only the OUTER map
     * would still let a caller modify the INNER lists. Protect both levels.
     */
    public Map<String, List<Book>> getBooksByGenre() {
        Map<String, List<Book>> view = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, List<Book>> entry : booksByGenre.entrySet()) {
            view.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(view);
    }

    /** Overdue loans as of today. */
    public List<Loan> getOverdueLoans() {
        return getOverdueLoans(LocalDate.now());
    }

    /**
     * Overdue loans as of a chosen date.
     *
     * WHY headMap(today): the TreeMap is sorted by due date, so headMap(today) hands
     * back exactly the keys strictly before today with no per-loan date check. Loans
     * due today or later are never even looked at.
     */
    public List<Loan> getOverdueLoans(LocalDate today) {
        List<Loan> overdue = new ArrayList<>();
        SortedMap<LocalDate, List<Loan>> pastDue = loansByDueDate.headMap(today);
        for (Map.Entry<LocalDate, List<Loan>> entry : pastDue.entrySet()) {
            for (Loan loan : entry.getValue()) {
                overdue.add(loan);
            }
        }
        return Collections.unmodifiableList(overdue);
    }

    /**
     * Top 3 most borrowed books, best first.
     *
     * HOW: scan the counter map ONCE. Push each entry into a min-heap; when the heap
     * grows past 3, poll() removes the smallest, so only the 3 largest survive.
     * Cost: O(n log 3) instead of sorting everything O(n log n).
     * The heap pops smallest-first, so inserting each popped item at index 0 reverses
     * the order into "most borrowed first".
     */
    public List<Book> getTopBorrowedBooks() {
        Queue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(new BorrowCountComparator());
        for (Map.Entry<String, Integer> entry : borrowCounts.entrySet()) {
            heap.offer(entry);
            if (heap.size() > TOP_N) {
                heap.poll(); // discard the current smallest
            }
        }
        List<Book> top = new ArrayList<>();
        while (!heap.isEmpty()) {
            Map.Entry<String, Integer> entry = heap.poll();
            top.add(0, books.get(entry.getKey()));
        }
        return Collections.unmodifiableList(top);
    }

    /** How many times a book was borrowed (0 if never). Main uses it to print the report. */
    public int getBorrowCount(String isbn) {
        Integer count = borrowCounts.get(isbn);
        return count == null ? 0 : count;
    }

    /**
     * A member's current loans.
     * WHY MemberNotFoundException: asking about an unknown member is an error, not an
     * "empty list" - the librarian probably mistyped the id and should be told.
     */
    public List<Loan> getLoansForMember(String memberId) throws MemberNotFoundException {
        Member member = findMember(memberId);
        List<Loan> result = new ArrayList<>();
        for (Loan loan : activeLoans) {
            if (loan.getMemberId().equals(member.getMemberId())) {
                result.add(loan);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /** Read-only view of every active loan (spec: getters return unmodifiable views). */
    public List<Loan> getActiveLoans() {
        return Collections.unmodifiableList(activeLoans);
    }

    // ---------------------------------------------------------------------------------
    //  LOOKUP HELPERS
    // ---------------------------------------------------------------------------------

    /**
     * WHY "find" helpers that throw: the "get from map, if null throw" pattern is needed
     * in many methods. One private method = one place that decides the error message.
     */
    private Book findBook(String isbn) throws BookNotFoundException {
        Book book = books.get(isbn == null ? "" : isbn.trim());
        if (book == null) {
            throw new BookNotFoundException("No book found with ISBN \"" + isbn + "\".");
        }
        return book;
    }

    private Member findMember(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId == null ? "" : memberId.trim());
        if (member == null) {
            throw new MemberNotFoundException("No member found with id \"" + memberId + "\".");
        }
        return member;
    }

    /**
     * WHY these two display helpers exist: a Loan only stores ISBN and member id, but
     * Main wants to print titles and names. These return plain Strings and never throw,
     * so Main stays free of try/catch just for printing.
     */
    public String getBookTitle(String isbn) {
        Book book = books.get(isbn);
        return book == null ? "(unknown book)" : book.getTitle();
    }

    public String getMemberName(String memberId) {
        Member member = members.get(memberId);
        return member == null ? "(unknown member)" : member.getName();
    }

    // ---------------------------------------------------------------------------------
    //  SEED DATA
    // ---------------------------------------------------------------------------------

    /**
     * Fill the library with test data.
     *
     * WHY inside Library and not Main: Main handles only input/output. Seeding uses the
     * same public methods a user would, so it also exercises validation and all indexes.
     *
     * WHY the two pre-made loans: one in the PAST (overdue report has something to show)
     * and one on a single-copy book (the "unavailable" test is ready to run).
     *
     * WHY wrap the checked exceptions in IllegalStateException(message, cause): seed
     * data is fixed by the programmer, so a failure here is a bug, not a user error.
     * We never swallow it silently and the cause keeps the full stack trace.
     */
    public void seedSampleData() {
        addBook("B101", "The Hobbit", "J.R.R. Tolkien", "Fantasy", 3);
        addBook("B102", "Dune", "Frank Herbert", "Sci-Fi", 2);
        addBook("B103", "Clean Code", "Robert C. Martin", "Programming", 1); // single copy
        addBook("B104", "Effective Java", "Joshua Bloch", "Programming", 2);
        addBook("B105", "Sapiens", "Yuval Noah Harari", "History", 3);
        addBook("B106", "Pride and Prejudice", "Jane Austen", "Classic", 2);
        addBook("B107", "1984", "George Orwell", "Sci-Fi", 2);
        addBook("B108", "The Hound of the Baskervilles", "Arthur Conan Doyle", "Mystery", 2);
        addBook("B109", "Foundation", "Isaac Asimov", "Sci-Fi", 2);
        addBook("B110", "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "Fantasy", 4);
        addBook("B111", "The Pragmatic Programmer", "Andrew Hunt and David Thomas", "Programming", 2);
        addBook("B112", "Guns, Germs, and Steel", "Jared Diamond", "History", 2);

        registerMember("M001", "Asha Reddy");
        registerMember("M002", "Ravi Kumar");
        registerMember("M003", "Meera Iyer");

        try {
            LocalDate today = LocalDate.now();
            issueBook("B101", "M001", today.minusDays(20)); // due 6 days ago -> overdue
            issueBook("B103", "M002", today);               // last copy of Clean Code
        } catch (BookNotFoundException | MemberNotFoundException
                 | BookUnavailableException | BorrowLimitExceededException e) {
            throw new IllegalStateException("Seed data is inconsistent: " + e.getMessage(), e);
        }
    }
}

// =====================================================================================
//  PART 5 - MAIN (INPUT / OUTPUT ONLY)
// =====================================================================================

/**
 * Main - the user interface.
 *
 * WHAT Main does: show the menu, read text, convert numbers, call Library, catch
 * exceptions, print messages. WHAT Main never does: apply a business rule.
 * WHY only one Library instance, created here: it is the "database". Creating it in
 * main() and PASSING it to methods avoids static global state.
 * WHY try-with-resources on Scanner: the Scanner is closed automatically when the
 * block ends, even if an exception escapes.
 */
public class Main {

    public static void main(String[] args) {
        Library library = new Library();
        library.seedSampleData();

        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(library, scanner);
        }
    }

    /**
     * The menu loop.
     *
     * WHY ONE try/catch around the whole switch: every menu action can fail the same
     * ways, so one set of catch blocks gives consistent messages and the app NEVER
     * crashes. After an error the loop simply shows the menu again.
     * WHY two catch groups: checked "business" problems and unchecked "bad input"
     * problems are separated so you can word them differently if you want.
     * WHY catch NoSuchElementException: if input ends (Ctrl+D / piped input runs out)
     * nextLine() throws it; we exit politely instead of crashing or looping forever.
     * No catch block is ever empty: each one prints something useful.
     */
    private static void runMenu(Library library, Scanner scanner) {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = readInt(scanner, "Choose an option: ");
                switch (choice) {
                    case 1:
                        handleAddBook(library, scanner);
                        break;
                    case 2:
                        handleRegisterMember(library, scanner);
                        break;
                    case 3:
                        handleIssueBook(library, scanner);
                        break;
                    case 4:
                        handleReturnBook(library, scanner);
                        break;
                    case 5:
                        handleSearch(library, scanner);
                        break;
                    case 6:
                        handleListSorted(library, scanner);
                        break;
                    case 7:
                        handleGenres(library);
                        break;
                    case 8:
                        handleOverdue(library);
                        break;
                    case 9:
                        handleTopBooks(library);
                        break;
                    case 10:
                        handleMemberLoans(library, scanner);
                        break;
                    case 11:
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Please choose a number from 1 to 11.");
                }
            } catch (BookNotFoundException | MemberNotFoundException
                     | BookUnavailableException | BorrowLimitExceededException e) {
                System.out.println("Sorry: " + e.getMessage());
            } catch (InvalidInputException | DuplicateEntryException e) {
                System.out.println("Not allowed: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Input closed. Exiting.");
                running = false;
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========== LIBRARY MANAGER ==========");
        System.out.println(" 1. Add book");
        System.out.println(" 2. Register member");
        System.out.println(" 3. Issue book");
        System.out.println(" 4. Return book");
        System.out.println(" 5. Search books (title or author)");
        System.out.println(" 6. List all books (sorted)");
        System.out.println(" 7. List books by genre");
        System.out.println(" 8. Show overdue loans");
        System.out.println(" 9. Show top 3 most borrowed books");
        System.out.println("10. Show a member's current loans");
        System.out.println("11. Exit");
    }

    // ------------------------------- menu handlers ------------------------------------
    // WHY one small method per menu item: the switch stays readable, and each handler
    // does exactly: ask -> call Library -> print result.

    private static void handleAddBook(Library library, Scanner scanner) {
        String isbn = readLine(scanner, "ISBN: ");
        String title = readLine(scanner, "Title: ");
        String author = readLine(scanner, "Author: ");
        String genre = readLine(scanner, "Genre: ");
        int copies = readInt(scanner, "Total copies: ");
        Book book = library.addBook(isbn, title, author, genre, copies);
        System.out.println("Added: " + book);
    }

    private static void handleRegisterMember(Library library, Scanner scanner) {
        String id = readLine(scanner, "Member id: ");
        String name = readLine(scanner, "Member name: ");
        Member member = library.registerMember(id, name);
        System.out.println("Registered: " + member);
    }

    private static void handleIssueBook(Library library, Scanner scanner)
            throws BookNotFoundException, MemberNotFoundException,
            BookUnavailableException, BorrowLimitExceededException {
        String isbn = readLine(scanner, "ISBN to issue: ");
        String memberId = readLine(scanner, "Member id: ");
        Loan loan = library.issueBook(isbn, memberId);
        System.out.println("Issued \"" + library.getBookTitle(loan.getIsbn()) + "\" to "
                + library.getMemberName(loan.getMemberId()) + ". Due on " + loan.getDueDate() + ".");
    }

    private static void handleReturnBook(Library library, Scanner scanner)
            throws BookNotFoundException, MemberNotFoundException {
        String isbn = readLine(scanner, "ISBN to return: ");
        String memberId = readLine(scanner, "Member id: ");
        long daysLate = library.returnBook(isbn, memberId);
        if (daysLate > 0) {
            System.out.println("Returned LATE by " + daysLate + " day(s).");
        } else {
            System.out.println("Returned on time. Thank you!");
        }
    }

    private static void handleSearch(Library library, Scanner scanner) {
        String query = readLine(scanner, "Search text: ");
        List<Book> results = library.searchBooks(query);
        if (results.isEmpty()) {
            System.out.println("No books match \"" + query + "\".");
            return;
        }
        System.out.println(results.size() + " match(es):");
        printBooks(results);
    }

    private static void handleListSorted(Library library, Scanner scanner) {
        System.out.println("1. Sort by title");
        System.out.println("2. Sort by author");
        int sub = readInt(scanner, "Choose: ");
        if (sub == 1) {
            printBooks(library.listBooksSortedByTitle());
        } else if (sub == 2) {
            printBooks(library.listBooksSortedByAuthor());
        } else {
            System.out.println("Please choose 1 or 2.");
        }
    }

    private static void handleGenres(Library library) {
        Map<String, List<Book>> byGenre = library.getBooksByGenre();
        for (Map.Entry<String, List<Book>> entry : byGenre.entrySet()) {
            System.out.println("== " + entry.getKey() + " ==");
            printBooks(entry.getValue());
        }
    }

    private static void handleOverdue(Library library) {
        LocalDate today = LocalDate.now();
        List<Loan> overdue = library.getOverdueLoans(today);
        if (overdue.isEmpty()) {
            System.out.println("No overdue loans.");
            return;
        }
        System.out.println("Overdue loans:");
        for (Loan loan : overdue) {
            System.out.println("  " + describeLoan(library, loan, today));
        }
    }

    private static void handleTopBooks(Library library) {
        List<Book> top = library.getTopBorrowedBooks();
        if (top.isEmpty()) {
            System.out.println("Nothing has been borrowed yet.");
            return;
        }
        int rank = 1;
        for (Book book : top) {
            System.out.println("  #" + rank + " " + book.getTitle() + " - "
                    + library.getBorrowCount(book.getIsbn()) + " borrow(s)");
            rank++;
        }
    }

    private static void handleMemberLoans(Library library, Scanner scanner) throws MemberNotFoundException {
        String memberId = readLine(scanner, "Member id: ");
        List<Loan> loans = library.getLoansForMember(memberId);
        if (loans.isEmpty()) {
            System.out.println("That member has no books on loan.");
            return;
        }
        LocalDate today = LocalDate.now();
        for (Loan loan : loans) {
            System.out.println("  " + describeLoan(library, loan, today));
        }
    }

    // ------------------------------- print helpers ------------------------------------

    private static void printBooks(List<Book> books) {
        for (Book book : books) {
            System.out.println("  " + book);
        }
    }

    /** WHY here: it is pure formatting (output), so it belongs in Main, not Library. */
    private static String describeLoan(Library library, Loan loan, LocalDate today) {
        String text = "\"" + library.getBookTitle(loan.getIsbn()) + "\" -> "
                + library.getMemberName(loan.getMemberId())
                + " | issued " + loan.getIssueDate() + " | due " + loan.getDueDate();
        if (loan.isOverdue(today)) {
            text += " | OVERDUE by " + loan.daysOverdue(today) + " day(s)";
        }
        return text;
    }

    // ------------------------------- input helpers ------------------------------------

    /**
     * Prompt and read one line. WHY nextLine() for EVERYTHING: mixing nextInt() and
     * nextLine() leaves a stray newline in the buffer and makes the next nextLine()
     * return an empty string. Reading whole lines and parsing ourselves avoids that.
     */
    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Prompt until the user types a whole number.
     * WHY a loop + try/catch: Integer.parseInt("abc") throws NumberFormatException. We
     * catch it, explain, and ask again, so letters never crash the app (test case 8).
     */
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            String text = readLine(scanner, prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("  \"" + text + "\" is not a whole number. Please try again.");
            }
        }
    }
}