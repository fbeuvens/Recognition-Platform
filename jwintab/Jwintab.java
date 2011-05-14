package jwintab;
/**
 *  Java interface to Wintab
 *
 *  @version $Id: Jwintab.java,v 1.1 2000/01/12 12:31:34 rekimoto Exp rekimoto 
 *  @author rekimoto
 *  Log:
 *    1.29.2000 rekimoto: add pressure parameter
 *    1.8.2000  rekimoto:  initial creation
 *
 **/


public class Jwintab {
	static {System.loadLibrary("src/jwintab/jwintab");}

	/*static {
		try {
			if ((new File("dll/jwintab.dll")).exists()) {
				try {
					System.loadLibrary("dll/jwintab");
					System.out.println(System.getProperty("java.version"));
				} catch (UnsatisfiedLinkError e) {
					e.printStackTrace();
				}
			}
			else{System.out.println("file not found");}
		} catch (Exception e) {
			System.out.println("hop\n"+e.getMessage());
		}
	}*/

	Jwintab() {
	}

	/**
	 * returns current version number
	 */
	public static native int getVersion();

	public static native int open();

	/**
	 * call this before closing the application, otherwise tablet does not work.
	 */
	public static native int close();

	/**
	 * val[0..5] = {x, y, button, orientation, angle, pressure}
	 */
	public static native int getPacket(int val[]);

}
