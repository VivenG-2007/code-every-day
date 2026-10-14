public class lambda{
    public static void main(String[] args) {
        Thread t=new Thread(lambda::printmessage); //lambda::printmessage ==== ()->printmessage()
        t.start();
    }

    private static void printmessage() {
        System.out.println("Hello");
    }
}