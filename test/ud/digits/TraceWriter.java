package test.ud.digits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TraceWriter {
	
	private BufferedWriter bw;
	
	public TraceWriter(String fileName){
		String recover = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader("trace/digits/ud/"+fileName));
			recover = "";
			String tmp = br.readLine();
			while(tmp != null){
				recover += tmp+"\n";
				tmp = br.readLine();
			}
			br.close();
		}
		catch(IOException ioe){}
		
		try{
			bw = new BufferedWriter(new FileWriter("traces/digits/ud/"+fileName));
			if(recover != null)
				bw.write(recover);
		}
		catch(IOException ioe){System.out.println("Trace for file "+fileName+" not created.");}
	}
	
	public void print(String str){
		try{
			bw.write(str);
		}
		catch(IOException ioe){System.out.println(str+" couldn't be written.");}
	}
	
	public void println(String str){
		try{
			bw.write(str+"\n");
		}
		catch(IOException ioe){System.out.println(str+" couldn't be written.");}
	}
	
	public void println(){
		println("");
	}
	
	
	public void close(){
		try{
			bw.close();
		}
		catch(IOException ioe){System.out.println("Trace couldn't be closed.");}
	}
	
	public static void main(String[] args){
		TraceWriter tw = new TraceWriter("test.txt");
		tw.println("coucou");
		tw.println("coucou3");
		tw.close();
	}

}
