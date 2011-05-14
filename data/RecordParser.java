package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/*
 * Classe permettant de parser un fichier texte au format adequat et de le transformer
 * en ensemble de Dot
 */
public class RecordParser {
	
	List<Dot> dotSet;
	
	public RecordParser(String pathFile){
		try{
			dotSet = new LinkedList<Dot>();
			
			BufferedReader br = new BufferedReader(new FileReader(pathFile));
			
			String tmp = br.readLine();
			while(tmp != null){
				dotSet.add(new Dot(tmp));
				tmp = br.readLine();
			}
		}
		catch(IOException ioe){System.out.println("Impossible de parser le fichier "+pathFile);System.out.println(ioe);}
	}
	
	public List<Dot> parse(){
		return dotSet;
	}
	
	public String toString(){
		String toReturn = "";
		Iterator<Dot> it = dotSet.iterator();
		while(it.hasNext()){
			toReturn += it.next() + "\n";
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		System.out.println(new RecordParser("records\\1\\i\\i1.txt"));
	}
	
}
