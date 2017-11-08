package testexample;

public class SimpleTest {
	public SimpleTest () {
		int a = 3;
		int b = 2;
		System.out.print(a+b);
	}
	
	public void cycle() {
		for(int i = 0; i < 3; i++) {
			if(i > 1) {
				System.out.print(1);
			}
		}
	} // 32, 20
}
