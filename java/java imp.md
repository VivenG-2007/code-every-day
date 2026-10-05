**Need of Wrapper Classes**



Wrapper classes are needed because Java’s java.util classes and the Collection Framework work with objects rather than primitive data types. Data structures such as ArrayList and Vector can store only objects (reference types), not primitive types. Wrapper classes also provide objects for primitive values when an object is required, such as for synchronization in multithreading.



**Autoboxing**



Autoboxing is the automatic conversion of a primitive data type into its corresponding wrapper class object. For example, int is converted to Integer, long to Long, and double to Double.



int a = 10;

Integer b = a;   // Autoboxing



**Unboxing**



Unboxing is the reverse of autoboxing. It is the automatic conversion of a wrapper class object into its corresponding primitive data type. For example, Integer is converted to int, Long to long, and Double to double.



Integer a = 10;

int b = a;       // Unboxing





**ArrayList** is a resizable array in Java. Unlike a normal array, its size can grow or shrink dynamically when elements are added or removed.



It is part of the java.util package.



import java.util.ArrayList;



ArrayList<Integer> numbers = new ArrayList<>();



numbers.add(10);

numbers.add(20);

numbers.add(30);



System.out.println(numbers);



Output:



\[10, 20, 30]







package genericsAndWrapperClasses;



public class WrapperClasses {



&#x20;   public static void main(String\[] args) {



&#x20;       Integer obj = 12;



&#x20;       Integer obj2 = Integer.valueOf("12");



&#x20;       System.out.println(obj2 \* 4);



&#x20;       Boolean myBoolean = Boolean.valueOf("false");



&#x20;       Integer obj3 = 12;  // autoboxing



&#x20;       int age = obj3;     // unboxing

&#x20;   }

} 









**What are Generics?**



Generics in Java allow us to create classes, interfaces, and methods that can work with different data types while providing type safety.



Instead of specifying a particular data type, we use a type parameter such as T.



**Why use Generics?**



Provides type safety

Avoids unnecessary type casting

Makes code reusable

Detects type errors at compile time

Commonly used with Collections like ArrayList, HashSet, HashMap

Without Generics

ArrayList list = new ArrayList();



list.add(10);

list.add("Hello");



Integer x = (Integer) list.get(0);



Here, type casting is required.



With Generics

ArrayList<Integer> list = new ArrayList<>();



list.add(10);

list.add(20);



Integer x = list.get(0);



No casting is required.



Generic Class

class Box<T> {

&#x20;   T value;



&#x20;   void set(T value) {

&#x20;       this.value = value;

&#x20;   }



&#x20;   T get() {

&#x20;       return value;

&#x20;   }

}



class Main {

&#x20;   public static void main(String\[] args) {

&#x20;       Box<Integer> b = new Box<>();



&#x20;       b.set(10);



&#x20;       System.out.println(b.get());

&#x20;   }

}



Here, T represents the type.



Generic Method

class Demo {

&#x20;   public static <T> void print(T value) {

&#x20;       System.out.println(value);

&#x20;   }



&#x20;   public static void main(String\[] args) {

&#x20;       print(10);

&#x20;       print("Hello");

&#x20;       print(10.5);

&#x20;   }

}



The same method can work with different data types.







**Bounded Generic Types**



use bounded genric to restrict to certain datatypes only





In general, the type parameter can accept any data types (except primitive types). However, if we want to use generics for some specific types (such as number types) only, then we can use bounded types.



In the case of bound types, we use the extends keyword.



Here, GenericsClass is created with a bounded type. This means GenericsClass can only work with data types that are children of Number (Integer, Double, and so on).



Example:

class GenericsClass<T extends Number> {  //this only allows integer,double and not string



&#x20;   public void display() {

&#x20;       System.out.println("This is a bounded type generics class.");

&#x20;   }

}









**Java Collection Framework**



The Java Collections Framework provides a set of interfaces and classes to implement various data structures and algorithms.



These interfaces include several methods to perform different operations on collections.



Main Interfaces

&#x20;                   Collection

&#x20;                  /    |     \\

&#x20;               List   Set    Queue

&#x20;                      |       |

&#x20;                 SortedSet   Deque





&#x20;                    Map

&#x20;                     |

&#x20;                 SortedMap





&#x20;                  Iterator

&#x20;                     |

&#x20;               ListIterator

Hierarchy

Collection

List

Set

SortedSet

Queue

Deque

Map

SortedMap

Iterator

ListIterator





**Java Collection Interface**



The Collection interface includes various methods that can be used to perform different operations on objects.



int size(): Returns the number of elements in the collection.

boolean isEmpty(): Returns true if the collection contains no elements.

boolean contains(Object o): Returns true if the collection contains the specified element.

boolean add(E e): Adds the specified element to the collection. Returns true if the collection changed as a result.

boolean remove(Object o): Removes a single instance of the specified element from the collection, if it is present.

boolean containsAll(Collection<?> c): Returns true if the collection contains all elements of the specified collection.







**Java List Interface**



The List interface extends the Collection interface and adds methods that are specific to lists. Lists are ordered collections that allow duplicate elements.



Implementations of List Interface:

ArrayList

LinkedList

Stack

Vector



Here are some methods that are present in the List interface but not in the Collection interface:



get(int index): Retrieves the element at the specified index in the list.

set(int index, E element): Replaces the element at the specified index with the given element.

add(int index, E element): Inserts the specified element at the specified position in the list, shifting the current elements to the right.

remove(int index): Removes the element at the specified index from the list and shifts the remaining elements to the left.

indexOf(Object o): Returns the index of the first occurrence of the specified element in the list, or -1 if the element is not present.

lastIndexOf(Object o): Returns the index of the last occurrence of the specified element in the list, or -1 if the element is not present.

listIterator(): Returns a list iterator over the elements in the list.

listIterator(int index): Returns a list iterator over the elements in the list, starting at the specified index.

subList(int fromIndex, int toIndex): Returns a view of the portion of the list between the specified fromIndex (inclusive) and toIndex (exclusive).





import java.util.ArrayList;

import java.util.List;

class list{

&#x20;   static void main() {

&#x20;       List<Integer> list=new ArrayList<>();

&#x20;       List<Integer> list2=new ArrayList<>();







&#x20;       list.add(10);

&#x20;       list.add(20);

&#x20;       list.add(30);





&#x20;       System.out.println(list.get(1));

&#x20;       System.out.println(list);





&#x20;       list.set(1,200);



&#x20;       list.remove(Integer.valueOf(200));



&#x20;       System.out.println(list);

&#x20;       System.out.println(list.size());

&#x20;       System.out.println(list.contains(200));



&#x20;       //list.remove(Integer.valueOf(10));

&#x20;       

&#x20;       list2.addAll(list);

&#x20;       System.out.println(list2);

&#x20;   }

}



**ArrayList in Java**



In Java, we need to declare the size of an array before we can use it. Once the size of an array is declared, it is hard to change it.

To handle this issue, we can use the ArrayList class. It allows us to create resizable arrays.

Unlike arrays, ArrayLists can automatically adjust their capacity when we add or remove elements from them. Hence, ArrayLists are also known as dynamic arrays.



initially, the array has a certain capacity, and as elements are added, it fills up.



When the capacity is reached, the ArrayList creates a new larger array and copies the elements from the old array to the new one. This process of resizing and copying is transparent to the user.

However, frequent resizing operations can lead to performance overhead, so the ArrayList increases its capacity by a certain factor to minimize the frequency of resizing.



ArrayList is a class in Java that implements the List interface and is used to store elements in a dynamically resizable array. Unlike a normal array, whose size is fixed once created, an ArrayList can automatically increase or decrease its size when elements are added or removed. Internally, an ArrayList uses an array to store its elements. It maintains two important concepts: size, which represents the number of elements currently stored, and capacity, which represents the amount of storage available in the internal array.



When an element is added using add(), the ArrayList first checks whether there is enough space in its internal array. If space is available, the element is directly inserted. If the internal array becomes full, the ArrayList creates a new larger array, copies all the existing elements from the old array into the new array, and then adds the new element. The exact growth strategy is an implementation detail; in modern OpenJDK implementations, the capacity typically grows by about 1.5 times. This resizing process is handled automatically and is transparent to the programmer.



ArrayList provides fast index-based access because it uses an array internally. The get() and set() operations generally take O(1) time. Adding an element at the end is O(1) amortized, although resizing occasionally takes O(n) time because all elements must be copied. However, inserting or removing an element from the beginning or middle takes O(n) time because the remaining elements have to be shifted. Searching using contains() or indexOf() generally takes O(n) time because elements are checked one by one.



For example, if an ArrayList contains \[10, 20, 30, 40] and we execute add(1, 50), the elements after index 1 are shifted to the right, resulting in \[10, 50, 20, 30, 40]. Similarly, if remove(1) is performed, the elements after index 1 are shifted to the left. Therefore, ArrayList is best when we need frequent access using indexes and additions at the end, but it is less efficient when frequent insertions or deletions occur at the beginning or middle.



