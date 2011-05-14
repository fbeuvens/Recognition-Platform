package tablet;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class TabletData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 807947807438339642L;
	private volatile long timestamp_millis;
	private volatile long timestamp_nano;
	private volatile int x, y;
	private volatile int button;
	private volatile int orientation, angle;
	private volatile int pression;
	private volatile boolean valid;
	
	public static final int WRITING = 1;
	private static final Pattern pattern = Pattern.compile ("^time_millis:(\\d+) time_nano:(\\d+) tablet \\((\\d+),(\\d+)\\) but=(\\d+) or=(\\d+) ang=(\\d+) pression=(\\d+)$");

	public TabletData() {
		inValidateData();
	}
	
	public TabletData(String str){
		
		Matcher matcher = pattern.matcher (str);
		inValidateData();
		if (matcher.matches())
		{
			timestamp_millis = Long.parseLong(matcher.group(1));
			timestamp_nano = Long.parseLong(matcher.group(2));
			x = Integer.parseInt(matcher.group(3));
			y = Integer.parseInt(matcher.group(4));
			button = Integer.parseInt(matcher.group(5));
			orientation = Integer.parseInt(matcher.group(6));
			angle = Integer.parseInt(matcher.group(7));
			pression = Integer.parseInt(matcher.group(8));
			valid = true;
		}

	}

	public void set(int arg[]) {
		timestamp_millis = System.currentTimeMillis();
		timestamp_nano = System.nanoTime();
		x = arg[0];
		y = arg[1];
		button = arg[2];
		orientation = arg[3];
		angle = arg[4];
		pression = arg[5];
		valid = true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(isValid()){
			sb.append("time_millis:");
			sb.append(timestamp_millis);
			sb.append(" time_nano:");
			sb.append(timestamp_nano);
			sb.append(" tablet (");
			sb.append(x);
			sb.append(",");
			sb.append(y);
			sb.append(") but=");
			sb.append(button);
			sb.append(" or=");
			sb.append(orientation);
			sb.append(" ang=");
			sb.append(angle);
			sb.append(" pression=");
			sb.append(pression);
		}
		else {
			sb.append("Not valid");
		}
		return sb.toString();
		
	}

	public long getTimeStampMillis() {
		return timestamp_millis;
	}
	
	public long getTimeStampNano(){
		return timestamp_nano;
	}

	public int getX() {
		return x;
	}
	

	public int getY() {
		return y;
	}

	public int getPressure() {
		return pression;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getAngle() {
		return angle;
	}

	public int getButton() {
		return button;
	}
	
	private void inValidateData(){
		valid = false;
	}
	
	public boolean isValid(){
		return valid;
	}


}