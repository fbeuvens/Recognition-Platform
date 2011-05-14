package tablet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TabletPacketFetcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TabletPacketFetcher tpf = new TabletPacketFetcher();

		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream();
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			pos.connect(pis);
			//pis.connect(pos);
			oos = new ObjectOutputStream(pos);
			ois = new ObjectInputStream(pis);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}

		tpf.setOutputStream(oos);

		(new Thread(tpf)).start();
		
		int i = 0;
		try {
		Object o = ois.readObject();
		
			while (o != null && i < 1000) {

				System.out.println((TabletData) o);

				i++;
				o = ois.readObject();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} catch (ClassNotFoundException c) {
			System.out.println(c.getMessage());
			c.printStackTrace();
			System.exit(0);
		}

		tpf.close();
	}

}
