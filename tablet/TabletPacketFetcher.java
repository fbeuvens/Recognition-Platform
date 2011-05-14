package tablet;

import java.io.IOException;
import java.io.ObjectOutputStream;

import jwintab.Jwintab;

public class TabletPacketFetcher implements Runnable {
	
	private volatile boolean quit = false;
	private ObjectOutputStream oos;

	public void run() {
		
		
		Jwintab.open();
		
		
		int arg[] = new int[6];
		while (!quit){
			TabletData tabletdata = new TabletData();
			// System.out.println("wait...." + i);//XXXX
			int res = Jwintab.getPacket(arg);
			if (res > 0) {
				tabletdata.set(arg);
				//System.out.println(tabletdata.toString());// XXXX
				try{
				oos.writeObject(tabletdata);
				}
				catch(IOException ie){
					System.out.println(ie.getMessage());
					ie.printStackTrace();
					System.exit(0);
				}

			} 
			
			else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}
//			try {
//				Thread.sleep(1);
//			} catch (Exception e) {
//			}
			
		}
		try{
			oos.close();
		}
		catch (IOException e){
			System.out.println("problem with closing outputstream of tablet");
			System.exit(0);
		}
		Jwintab.close();

	}
	
	public void close(){
		quit = !quit;
	}
	
	public void setOutputStream(ObjectOutputStream oos){
		this.oos = oos;
	}

}
