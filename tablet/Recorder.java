package tablet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Recorder implements TabletListener {

	private ArrayList<TabletData> datas;
	private String filename;
	private TabletData lastTD;
	private TabletDrawingArea tda;

	public Recorder(TabletDrawingArea tda, String filename) {
		this.tda = tda;
		this.filename = filename;
		datas = new ArrayList<TabletData>();
		lastTD = new TabletData();
	}
	
	public Recorder(TabletDrawingArea tda, String filename, ArrayList<TabletData> d){
		this(tda, filename);
		datas = d;
	}

	
	public void dataArrival(TabletData td) {
		if (tda.isMapped()){
			if ((td.getButton() & TabletData.WRITING) > 0){
				datas.add(td);
				lastTD = td;
			}
			else {
				if (lastTD.isValid()) {
					lastTD = new TabletData();
					datas.add(lastTD);
				}
			}
		}
		else{
			if (lastTD.isValid()){
				lastTD = new TabletData();
				datas.add(lastTD);
			}
		}
	}

	
	// il faudrait peut être tenir compte des duplicats de timestamp
	public boolean save() {
		boolean saved = true;
		PrintWriter file = null;
		FileWriter temp = null;
		try {
			temp = new FileWriter(filename);
		}
		catch (IOException e) {
			System.err.println("Erreur lors de la creation du fichier");
			saved = false;
		}
		if (temp != null){
			file = new PrintWriter(new BufferedWriter(temp));
			for (TabletData d : datas) {
				
				file.println(d.toString());

				if (file.checkError()) {
					saved = false;
					break;
				}
				
			}
			file.close();
			saved = !file.checkError();
		}
		else{
			saved = false;
		}
		return saved;
	}
	
	public void erase(){
		datas = new ArrayList<TabletData>();
	}
	
	public ArrayList<TabletData> getDrawingData(){
		return datas;
	}
	
	
}
