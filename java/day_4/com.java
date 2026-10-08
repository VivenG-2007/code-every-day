import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class animal implements Comparable<animal>{
    int age;
    String name;
    int weight;

    public animal(int age, String name, int weight) {
        this.age = age;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "animal{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", weight=" + weight +"\n"+
                '}';
    }

    @Override
    public int compareTo(animal o) {
        return 0;
    }
}
public class com{
    public static void main(String[] args){
        animal a1=new animal(1,"a",10);
        animal a2=new animal(2,"b",20);
        animal a3=new animal(3,"c",30);
        animal a4=new animal(4,"d",40);
        List<animal> arr= new ArrayList<>();
        arr.add(a1);
        arr.add(a2);
        arr.add(a3);
        arr.add(a4);
        System.out.println(arr);
        //arr.sort();
        Collections.sort(arr);
    }
}
