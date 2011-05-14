package tablet;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import jwintab.Jwintab;



public class TabletDrawingArea extends Component implements Runnable, TabletListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -60502139581988779L;

	// private Button reset_b;
	//private TabletData tablet;
	// Thread thread;
	private volatile boolean quit_frag = false;
	private Robot r;

	private static int MAXWIDTH = 60960;
	private static int MAXHEIGHT = 45720; // should be 46120, why not the case???

	private ArrayList<TabletListener> listeners = new ArrayList<TabletListener>();
	
	private boolean mapping = false;
	private boolean unpressed = true;
	private boolean enabled = true;
	private TabletData previous_data = null;
	private Dimension previous_location = null;

	private volatile Recorder recorder;

	// with our tablet?

	public TabletDrawingArea() {
		Jwintab.open();
		//tablet = new TabletData();
		setPreferredSize(new Dimension(600, (int) 600.0 * MAXHEIGHT / MAXWIDTH));
		addListener(this);
		setRecorder("DefaultFile");
	}

	public TabletDrawingArea(Dimension d) {
		super();
		setPreferredSize(new Dimension(d.width, (int) d.width * MAXHEIGHT
				/ MAXWIDTH));
	}



	
	public void dataArrival(TabletData td) {

		
		mapping(td);
		
		handlePointer(td);

		// draw points
		
		if (mapping) 
			drawPoints(td, this.getGraphics());

		// quit appli
		/*if (td.getX() < 10 && td.getY() < 10)
		quit_frag = !quit_frag;*/
		/*if (td.getAngle() < 0)
			close();*/
	}
	
	private void handlePointer(TabletData td){
		
		int x = 0, y = 0;
		Dimension d;
		if (mapping) {

			d = getXY(td, mapping);
			x = d.width;
			y = d.height;
			r.mouseMove(x + this.getLocationOnScreen().x, y
					+ this.getLocationOnScreen().y);

		} 
		else {
			d = getXY(td, mapping);
			x = d.width;
			y = d.height;
			r.mouseMove(x, y);
		}
		
	}
	
	private Dimension getXY(TabletData td, boolean mapped){
		Dimension d;
		int x=0, y=0;
		if (mapped){
			// x = this.getLocationOnScreen().x;
			// System.out.println(x);
			x = x + this.getSize().width * td.getX() / MAXWIDTH;
			// y = this.getLocationOnScreen().y;
			// System.out.println(y);
			y = y + this.getSize().height - this.getSize().height * td.getY()
					/ MAXHEIGHT;
		}
		else {
			x = Toolkit.getDefaultToolkit().getScreenSize().width * td.getX()
					/ MAXWIDTH;
			y = Toolkit.getDefaultToolkit().getScreenSize().height;
			y = y - y * td.getY() / MAXHEIGHT;
		}
		
		d = new Dimension(x, y);
		return d; 
	}
	
	private void drawPoints(TabletData td, Graphics g){
		Dimension d = getXY(td, true);
		int x = d.width;
		int y = d.height;
		if (((td.getButton() & TabletData.WRITING) > 0 ) && td.isValid()) {
			//Graphics g = this.getGraphics();
			g.setColor(Color.blue);
			if (previous_location != null) {
				g.drawLine(x , y ,
						previous_location.width,
						previous_location.height);
				/*g.drawLine(x - this.getLocation().x, y - this.getLocation().y,
						previous_location.width - this.getLocation().x,
						previous_location.height - this.getLocation().y);*/
				previous_location = new Dimension(x, y);
			} else {
				g.fillOval(x , y ,
						1, 1);
				/*g.fillOval(x - this.getLocation().x, y - this.getLocation().y,
						1, 1);*/
				previous_data = td;
				previous_location = new Dimension(x, y);
			}
		} else {
			previous_data = null;
			previous_location = null;
		}
	}
	
	public void mapping(TabletData td){
		boolean pressing = false;
		Cursor c = Cursor.getDefaultCursor();
		// repaint();
		pressing = false;//(td.getButton() == 2 || td.getButton() == 3);
		if ((pressing && unpressed) || !td.isValid()) {
			mapping = !mapping;
			unpressed = false;
			if (mapping) {
				c = new Cursor(Cursor.HAND_CURSOR);
				this.setCursor(c);
			} else {
				c = new Cursor(Cursor.DEFAULT_CURSOR);
				this.setCursor(c);
			}
		}
		if (!unpressed && !pressing) {
			unpressed = true;
		}
	}
	
	
	public void run() {
		try {
			r = new Robot();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}

			//System.out.println("start wintab:" + Jwintab.getVersion());// XXXX
			Jwintab.open();
			int arg[] = new int[6];
			int i = 0;
			
			repaint();

			while (!quit_frag) {
				while (!enabled) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
				TabletData tabletdata = new TabletData();
				i++;
				// System.out.println("wait...." + i);//XXXX
				int res = Jwintab.getPacket(arg);
				if (res > 0) {
					tabletdata.set(arg);
					//System.out.println(tabletdata.toString());// XXXX
					notifyAll(tabletdata);

				} 
				
				else {
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
				}
			}
			//System.out.println("close");
			Jwintab.close();

			//System.exit(0);
		} catch (AWTException err) {
			System.err.println("Could not create robot");
		}
	}
	
	public boolean isMapped(){
		return mapping;
	}
	
	/**
	 * @pre  -
	 * @post renvoie la taille pour l'image du panel préférée
	 */
    public Dimension getPreferredSize() { return getMinimumSize(); }
	
	/**
	* @pre  -
	* @post renvoie la taille maximale pour l'image du panel d'image 
	*/
   // public Dimension getMaximumSize() { return getMinimumSize(); }
    
	
	public void setPreferredSize(Dimension d) {
		//System.out.println("Tablet ratio assured");
		super.setPreferredSize(new Dimension(d.width, (int) d.width * MAXHEIGHT
				/ MAXWIDTH));
	}

	
	
	

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width - 1,
				getSize().height - 1);
		/*
		 * g.setColor(Color.blue); g.drawString( " ", 5, this.getHeight());
		 */
		
		
		if (this.recorder != null){
			g.setColor(Color.blue);
			ArrayList<TabletData> d = recorder.getDrawingData();
			for (TabletData td : d) {
				drawPoints(td, g);
			}
		}
		
	}

	public final void addListener(TabletListener tl) {
		listeners.add(tl);
	}

	private void notifyAll(TabletData td) {
		synchronized (this) {
			for (TabletListener tl : listeners) {
				tl.dataArrival(td);
			}
		}
	}




	
	public void close(){
		quit_frag = !quit_frag;
	}
	
	
	public void setRecorder(String str){
		synchronized (this) {
			if (recorder!=null){
				listeners.remove(recorder);
			}
			recorder = new Recorder(this, str);
			addListener(recorder);
		}
	}
	
	private void setRecorder(Recorder r){
		synchronized (this){
			this.recorder = r;
		}
	}
	
	public boolean loadRecorder(String file){
		boolean load = true;
		
		if (file!=null && !file.equals("")){
			try {
				BufferedReader b = new BufferedReader(new FileReader(file));	
				
				ArrayList<TabletData> altd = new ArrayList<TabletData>();
				String line = b.readLine();
				boolean fileError = false;
				
				while (line !=null && !fileError){
					TabletData td;
					if (line.equals("Not valid")){
						td = new TabletData();
					}
					else {
						td = new TabletData(line);
					}
					altd.add(td);
					line = b.readLine();
				}
				
				Recorder new_recorder = new Recorder(this, file, altd);
				synchronized(this){
					this.addListener(new_recorder);
				}
				setRecorder(new_recorder);
				load=true;
				
				b.close();
				
				
			}
			catch (IOException ioe){
				load = false;
				setRecorder(new Recorder(this, file));
			}
		}
		else{
			load = false;
		}
		this.repaint();
		
		return load;
	}
	
	
	
	public void clearRecorder(){
		if (recorder != null){
			recorder.erase();
			this.repaint();
		}
	}
	
	public void saveDatas(){
		recorder.save();
	}
	
	public void disable(){
		listeners.remove(this);
		enabled = false;
	}
	
	public void enable(){
		listeners.add(this);
		enabled = true;
	}
	
	


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Frame f = new Frame("WacomDrv Test");
		TabletDrawingArea draw = new TabletDrawingArea();
		f.add(draw);
		f.pack();
		f.setVisible(true);
		f.setSize(1200*3/4, 900*3/4);
		
		draw.setRecorder("test");

		(new Thread(draw)).start();

	}


}
