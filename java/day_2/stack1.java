import java.util.*;

class stack1 {
    public static void main(String[] args) {
        Stack<Integer> list = new Stack<>();
        list.push(10);
        list.push(20);
        list.push(30);
        while (!list.empty()) {
            System.out.println(list.pop());
        }
    }
}