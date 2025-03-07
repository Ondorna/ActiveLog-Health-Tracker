public class Main {
    public static void main(String[] args) {
        System.out.println("Main start");
        TestClass testClass = new TestClass();
        testClass.protectedMethod();
        testClass.privateMethod(); // Why does this cause error?
        System.out.println("Main end");
    }
}

class TestClass{
    private void privateMethod(){
        System.out.println("private method run successfully!");
    }

    protected void protectedMethod(){
        System.out.println("protected method run successully!");
    }
}