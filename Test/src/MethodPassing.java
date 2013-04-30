import java.util.concurrent.Callable;


public class MethodPassing {
	
	class TestClass2 implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			return 2;
		}
		
	}
	
	public void OuterMethod() {
		System.out.println(method(new TestClass2()));
		System.out.println(method(new TestClass2()));
	}
	
	public int method(Callable<Integer> i) {
		
		try {
			return 7* i.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) {
		MethodPassing p = new MethodPassing();
		p.OuterMethod();
	}
}
