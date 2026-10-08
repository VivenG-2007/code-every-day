import java.util.*;
class stackdeq{
    static void main(String[] args) {
        ArrayDeque<Integer> stack=new ArrayDeque<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println(stack);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());

        ArrayDeque<Integer> qu=new ArrayDeque<>();
        qu.offer(10);
        qu.offer(20);
        qu.offer(70);
        System.out.println(qu);
        System.out.println(qu.poll());
        System.out.println(qu.poll());
        System.out.println(qu.poll());
    }
}