package data;

/*
 * Classe definissant un point avec toutes ses caracteristiques
 */
public class Dot {
	
	public boolean valid;
	public long time_millis, time_nano;
	public int posX, posY, button, orientation, angle, pressure;
	public String device;
	
	public Dot(long time_millis, long time_nano, String device, int posX, int posY, int button, int orientation, int angle, int pressure){
		this.valid = true;
		this.time_millis = time_millis;
		this.device = device;
		this.time_nano = time_nano;
		this.posX = posX;
		this.posY = posY;
		this.button = button;
		this.orientation = orientation;
		this.angle = angle;
		this.pressure = pressure;
	}
	
	public Dot(String tabletDataLine){
		if(tabletDataLine.equals("Not valid")){
			valid = false;
			time_millis = time_nano = posX = posY = button = orientation = angle = pressure = -1;
			device = "tablet";
		}
		else{
			String[] tabletDataLineSplitted = tabletDataLine.split(" ");
			valid = true;
			time_millis = Long.parseLong(tabletDataLineSplitted[0].split(":")[1]);
			time_nano = Long.parseLong(tabletDataLineSplitted[1].split(":")[1]);
			device = tabletDataLineSplitted[2];
			String[] pos = tabletDataLineSplitted[3].split(",");
			posX = Integer.parseInt(pos[0].substring(1));
			posY = Integer.parseInt(pos[1].substring(0,pos[1].length()-1));
			button = Integer.parseInt(tabletDataLineSplitted[4].split("=")[1]);
			orientation = Integer.parseInt(tabletDataLineSplitted[5].split("=")[1]);
			angle = Integer.parseInt(tabletDataLineSplitted[6].split("=")[1]);
			pressure = Integer.parseInt(tabletDataLineSplitted[7].split("=")[1]);	
		}
	}
	
	public String toString(){
		if(valid)
			return "time_millis="+time_millis+" time_nano="+time_nano+" "+device+" pos=("+posX+","+posY+") button="+button+" orientation="+orientation+" angle="+angle+" pressure="+pressure;
		else
			return "Not valid";
	}
	
	public static void main(String[] args){
		String tmp = "30713)456";
		System.out.println(tmp.split("\\)")[0]);
	}

}
