package temp;

import java.lang.reflect.InvocationTargetException;

public class Test implements TestInterface {

	public static String[] hello() {
		return new String[]{"hello","world"};
	}
	
	public void test() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<? extends TestInterface> c = Test.class;
		System.out.println(c.getDeclaredMethod("hello").invoke(null));
	}
}
