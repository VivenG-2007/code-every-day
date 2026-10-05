//genetic class
class Dog<E>{
    E id;
    Dog(E id){
        this.id=id;
    }
     void getid(){
        System.out.println(id);
    }
}

class generics{
    public  static void main(String args[]){
        Dog<String> d1=new Dog<String>("12233");
        Dog<Integer> d2=new Dog<Integer>(12);
        d1.getid();
        d2.getid();
        printdata("hi");
    }
    //generic method
    static <t>void printdata(t data){
        System.out.println(data);
    }
}