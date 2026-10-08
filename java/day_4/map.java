import java.util.*;

class map{
    public static void main(String[] args) {
        Map<String,String> mp=new HashMap<>();
        mp.put("us","united states");
        mp.put("in","india");
        mp.put("en","india");
        mp.putIfAbsent("in","india");
        mp.put("in","india1");
        System.out.println(mp);
        mp.remove("en","india");
        System.out.println(mp.containsKey("in"));
    }
}