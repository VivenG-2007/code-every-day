import java.util.*;
class deque{
    static void main(String[] args) {
        ArrayDeque<Integer> l=new ArrayDeque<>();
        l.offerFirst(10);
        l.offerFirst(20);
        l.offerLast(30);
        System.out.println(l);
        System.out.println(l.pollFirst());
        System.out.println(l.pollLast());
        System.out.println(l.poll());

    }
}