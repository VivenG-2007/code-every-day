import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class temp{
    public static void main(String[] args){
        List<Integer> ls= Arrays.asList(4,5,6,7,8,9);
        //Stream<Integer> data=ls.stream();
        //Stream<Integer> mapped=data.map(n->n*2);

        //mapped.forEach(n->System.out.println(n));
        ls.stream().filter(n->n%2==0)
                .sorted()
                .map(n->n*2)
                .forEach(n->System.out.println(n));
    }
}
