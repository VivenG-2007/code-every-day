import java.util.*;
class queuelist{
    static void main(String[] args) {
        Queue<Integer> list=new LinkedList<>();
        list.offer(10);
        list.offer(20);
        System.out.println(list);
        System.out.println(list.peek());
        while (!list.isEmpty()) {
            System.out.println(list.poll());
        }
        System.out.println(list.poll());
    }
}