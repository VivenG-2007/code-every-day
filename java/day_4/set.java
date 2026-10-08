import java.util.*;
class temp{
    int id;
    String name;

    public temp(int id,String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        temp temp = (temp) o;
        return id == temp.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    @Override
    public String toString() {
        return id + " " + name;
    }
}
class set{
    public static void main(String[] args) {
        /*Set<Integer> set=new HashSet<>();
        Set<Integer> set=new LinkedHashSet<>();
        set.add(10);
        set.add(10);
        set.add(20);
        set.add(30);
        set.add(90);
        set.remove(10);
        System.out.println(set);
         */
        Set<String> st=new HashSet<>();
        st.add("Viven");
        st.add("narshi");
        st.add("sanja");
        st.add("Viven");
        System.out.println(st);
        Set<temp> s=new HashSet<>();
        s.add(new temp(1,"vivu1"));
        s.add(new temp(1,"vivu2"));
        s.add(new temp(3,"vivu3"));
        System.out.println(s);
    }
}