import java.util.*;
class vector{
    static void main() {
        List<Integer> list=new Vector<>();
        list.add(10);
        list.add(20);
        list.add(30);
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}