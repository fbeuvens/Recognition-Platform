package jwintab;


public class WintabTest {
	
	public static void main(String argv[]) {
		System.out.println("version = " + Jwintab.getVersion());
		
		Jwintab.open();
		
		int arg[] = new int[6];
		
		while (true) {
			int res = Jwintab.getPacket(arg);
			if (res > 0) {
				System.out.println("(" + arg[0] + ", " + arg[1] + ")" +
					"button= " + arg[2] + 
					" <" + arg[3] + "," + arg[4] + ">" + 
					"Press=" + arg[5]);
				if (arg[0] < 100 || arg[1] < 100) 
				break;
			} else if (res < 0) {
				System.out.println("error " + res);
			}
		}
		
		Jwintab.close();
	}
}

			