package jwintab;


import java.awt.*;
import java.awt.event.*;



class TabletData{
	int x, y;
	int button;
	int orientation, angle;
	
	static int vals[][] = {
				{0, 600, 670, 3590},  // A
				{3, 600, 670, 3590},  // B
				{0, 260, 440, 3515},  // C
				{1, 660, 810, 2655},  // D
				{1, 250, 350, 930},  // E
				{3, 270, 340, 940},  // F
	};
				  
	static String label[] = {"A", "B", "C", "D", "E", "F"};
	
	
	public TabletData() {}
	
	public void set(int arg[]) {
		x = arg[0];  y = arg[1];
		button = arg[2];
		orientation = arg[3];
		angle = arg[4];
	}
	
	public String toString() {
		return "tablet (" + x + ", " + y + ")" + "or=" + orientation;
	}
	
	public String findFace() {
		for (int i = 0; i < vals.length; i++) {
			if (vals[i][0] == this.button &&
				vals[i][1] <= this.angle &&  this.angle <= vals[i][2])
					return label[i];
		}
		return "";
	}

}



public class Test extends Component implements ActionListener, Runnable, KeyListener {
	Button reset_b;
	TabletData tablet;
	Thread thread;
	boolean quit_frag = false;
	


	public Test() {
		Jwintab.open();
		resetMaxMin();
		tablet = new TabletData();
		addKeyListener(this);
		//thread = new Thread(this);
		//thread.start();
	}
	


	static int max_a = -100000;
	static int min_a = 100000;
	static int max_o = -1000000;
	static int min_o = 100000;

	public void resetMinMax() {
		max_o = max_a = -10000;
		min_o = min_a = 10000;
	}


	public void updateMinMax(TabletData t) {
		int a = t.angle;
		int o = t.orientation;

		if (max_a < a) max_a = a;
		if (a < min_a) min_a = a;
		
		if (max_o < o) max_o = o;
		if (o < min_o) min_o = o;
	}



	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode() + "lettre : '" + e.getKeyChar()+"'");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Q:
			quit_frag = true; break;
		case KeyEvent.VK_SPACE:
			resetMinMax();
			break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void run() {
		try {Thread.sleep(500);}catch (Exception e) {}
		
		System.out.println("start wintab:" + Jwintab.getVersion());//XXX
		Jwintab.open();
		int arg[] = new int[6];
		int i = 0;
		while (!quit_frag) {
			i++;
			//System.out.println("wait...." + i);//XXXXX
			int res = Jwintab.getPacket(arg);
			if (res > 0) {
				tablet.set(arg);
				System.out.println(tablet.toString());//XXXX
				updateMinMax(tablet);
				repaint();
				if (tablet.x < 10 && tablet.y < 10) break;
			} else {
				try {
					Thread.sleep(50);
				} catch (Exception e) {}
			}
		}
		System.out.println("close");
		Jwintab.close();
		
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	public Dimension getPreferredSize() {
		return new Dimension(480, 320);
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	public double maxlen, minlen;
	
	void resetMaxMin() {
		maxlen = -1000.0;
		minlen = 1000.0;
	}

	public void paint(Graphics g) {
		g.setColor(Color.blue);
		g.drawString("Test $Id: Test.java,v 1.1 2000/01/12 12:31:34 rekimoto Exp $", 5, 320);

		// position
		g.setColor(Color.green);
		g.drawRect(50, 50, 228, 151);
		g.setColor(Color.red);
		int x = 50 + tablet.x / 100;
		int y = 50 + tablet.y / 100;
		g.fillOval(x - 2, y - 2, 5, 5);
		g.setColor(Color.blue);
		g.drawString(""+tablet.x + ", " + tablet.y, 50, 20);
		
		g.drawString("button = " + tablet.button, 100, 40);
		


		/*
		g.setColor(Color.red);		
		g.fillRect(300, 15, evt.as / 2, 5);
		g.setColor(Color.green);
		g.drawRect(300, 15, 127/2, 5);
		g.drawRect(300-128/2, 15, 128/2, 5);
		g.setColor(Color.blue);
		g.drawString("Press"+evt.as, 250, 30);
		
		if (evt.func)
			g.fillRect(10, 10, 10, 10);
		else
			g.drawRect(10, 10, 10, 10);
			
	 	if ((evt.f & 0x8) != 0)
	 		g.fillRect(30, 10, 10, 10);
	 	else
	 		g.drawRect(30, 10, 10, 10);
	 		
	 	if ((evt.f & 0x4) != 0)
	 		g.fillRect(40, 10, 10, 10);
	 	else
	 		g.drawRect(40, 10, 10, 10);
	 		
	 	if ((evt.f & 0x2) != 0)
	 		g.fillRect(50, 10, 10, 10);
	 	else
	 		g.drawRect(50, 10, 10, 10);
	 		
	 	if ((evt.f & 0x1) != 0)
	 		g.fillRect(60, 10, 10, 10);
	 	else
	 		g.drawRect(60, 10, 10, 10);
	 		
	 	*/
	 		
	 	int xc = 350, yc = 150;
	 	
	 	g.drawString("Face=" + tablet.findFace(), xc - 100, yc - 120);
	 	
	 	
		g.drawString("ang min: " + min_a + " max: " + max_a, 
				xc - 100, yc - 100);
				
		g.drawString("org min: " + min_o + " max: " + max_o, 
				xc - 100, yc - 80);
				
	 	g.setColor(Color.red);	
	 	g.fillRect(xc-2, yc-2, 2, 2);
	 	g.drawLine(xc, yc, xc + tablet.orientation/100, 
	 		yc + tablet.angle/10);
	 	g.setColor(Color.blue);	 	
	}
	
	public static void main(String argv[]) {
		Frame f =  new Frame("WacomDrv Test");
		Test app = new Test();
		f.add(app);
		f.pack();
		f.setVisible(true);
		
		app.run(); 
	}
}

	