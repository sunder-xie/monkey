/**
 * Created by zxg on 15/12/14.
 */
public class Test{
    public static int k=0;
    public static Test t1=new Test("t1");
    public static Test t2=new Test("t2");
    public static int i=print("i");
    public static int n=99;
    public int j=print("j");
    static {
        print("静态块");
    }
    public Test(String str){
        System.out.println((++k)+":"+str+"  i="+i+" n="+n);
        ++i;
        ++n;
    }
    {
        print("构造块");
    }
    public static int print(String str){
        System.out.println((++k)+":"+str+"  i="+i+" n="+n);
        ++n;
        return ++i;
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Test t=new Test("init");
    }
}
