import java.util.ArrayList;
import java.util.List;
class list{
    static void main() {
        List<Integer> list=new ArrayList<>();
        List<Integer> list2=new ArrayList<>();



        list.add(10);
        list.add(20);
        list.add(30);


        System.out.println(list.get(1));
        System.out.println(list);


        list.set(1,200);

        list.remove(Integer.valueOf(200));

        System.out.println(list);
        System.out.println(list.size());
        System.out.println(list.contains(200));

        //list.remove(Integer.valueOf(10));

        list2.addAll(list);
        System.out.println(list2);
    }
}