package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * Classe definissant un conteneur pour les templates retenus dans un ordre aleatoire
 */
public class TemplateSet extends LinkedList<data.Template> {
	
	private int initialSize;
	private int numberTemplates = 10;
	private int numberSignatures = 30;
	private Hashtable<data.Template, Integer> templatesTable;
	private Hashtable<String, data.Template> templatesTableByString;
	
	public TemplateSet(){
		super();
		
		initialSize = 0;
		
		templatesTable = new Hashtable<data.Template, Integer>();
		templatesTable.put(data.Template.zero, new Integer(numberTemplates));
		templatesTable.put(data.Template.one, new Integer(numberTemplates));
		templatesTable.put(data.Template.two, new Integer(numberTemplates));
		templatesTable.put(data.Template.three, new Integer(numberTemplates));
		templatesTable.put(data.Template.four, new Integer(numberTemplates));
		templatesTable.put(data.Template.five, new Integer(numberTemplates));
		templatesTable.put(data.Template.six, new Integer(numberTemplates));
		templatesTable.put(data.Template.seven, new Integer(numberTemplates));
		templatesTable.put(data.Template.eight, new Integer(numberTemplates));
		templatesTable.put(data.Template.nine, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.a, new Integer(numberTemplates));
		templatesTable.put(data.Template.b, new Integer(numberTemplates));
		templatesTable.put(data.Template.c, new Integer(numberTemplates));
		templatesTable.put(data.Template.d, new Integer(numberTemplates));
		templatesTable.put(data.Template.e, new Integer(numberTemplates));
		templatesTable.put(data.Template.f, new Integer(numberTemplates));
		templatesTable.put(data.Template.g, new Integer(numberTemplates));
		templatesTable.put(data.Template.h, new Integer(numberTemplates));
		templatesTable.put(data.Template.i, new Integer(numberTemplates));
		templatesTable.put(data.Template.j, new Integer(numberTemplates));
		templatesTable.put(data.Template.k, new Integer(numberTemplates));
		templatesTable.put(data.Template.l, new Integer(numberTemplates));
		templatesTable.put(data.Template.m, new Integer(numberTemplates));
		templatesTable.put(data.Template.n, new Integer(numberTemplates));
		templatesTable.put(data.Template.o, new Integer(numberTemplates));
		templatesTable.put(data.Template.p, new Integer(numberTemplates));
		templatesTable.put(data.Template.q, new Integer(numberTemplates));
		templatesTable.put(data.Template.r, new Integer(numberTemplates));
		templatesTable.put(data.Template.s, new Integer(numberTemplates));
		templatesTable.put(data.Template.t, new Integer(numberTemplates));
		templatesTable.put(data.Template.u, new Integer(numberTemplates));
		templatesTable.put(data.Template.v, new Integer(numberTemplates));
		templatesTable.put(data.Template.w, new Integer(numberTemplates));
		templatesTable.put(data.Template.x, new Integer(numberTemplates));
		templatesTable.put(data.Template.y, new Integer(numberTemplates));
		templatesTable.put(data.Template.z, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.triangle, new Integer(numberTemplates));
		templatesTable.put(data.Template.square, new Integer(numberTemplates));
		templatesTable.put(data.Template.rectangle, new Integer(numberTemplates));
		templatesTable.put(data.Template.circle, new Integer(numberTemplates));
		templatesTable.put(data.Template.pentagon, new Integer(numberTemplates));
		templatesTable.put(data.Template.parallelogram, new Integer(numberTemplates));
		templatesTable.put(data.Template.doubleSquare, new Integer(numberTemplates));
		templatesTable.put(data.Template.doubleCircle, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.rightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightArrowDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrowDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightArrowUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrowUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrowRightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrowLeftArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrowRightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrowLeftArrow, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.arrobas, new Integer(numberTemplates));
		templatesTable.put(data.Template.signature, new Integer(numberTemplates));
		
		
		Enumeration<data.Template> ttKeys = templatesTable.keys();
		
		
		templatesTableByString = new Hashtable<String, data.Template>();
		while(ttKeys.hasMoreElements()){
			Template curTemp = ttKeys.nextElement();
			templatesTableByString.put(curTemp.toString(), curTemp);
		}
		
		for(int i = 0; i < numberTemplates; i++){
			ttKeys = templatesTable.keys();
			while(ttKeys.hasMoreElements()){
				add((int)(Math.random()*size()),ttKeys.nextElement());
			}
		}
		
		for(int i = 0; i < numberSignatures-numberTemplates; i++)
			add((int)(Math.random()*size()),data.Template.signature);
		
	}
	
	
	
	public TemplateSet(int idToRecover){
		super();
		initialSize = 0;
		
		templatesTable = new Hashtable<data.Template, Integer>();
		templatesTable.put(data.Template.zero, new Integer(numberTemplates));
		templatesTable.put(data.Template.one, new Integer(numberTemplates));
		templatesTable.put(data.Template.two, new Integer(numberTemplates));
		templatesTable.put(data.Template.three, new Integer(numberTemplates));
		templatesTable.put(data.Template.four, new Integer(numberTemplates));
		templatesTable.put(data.Template.five, new Integer(numberTemplates));
		templatesTable.put(data.Template.six, new Integer(numberTemplates));
		templatesTable.put(data.Template.seven, new Integer(numberTemplates));
		templatesTable.put(data.Template.eight, new Integer(numberTemplates));
		templatesTable.put(data.Template.nine, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.a, new Integer(numberTemplates));
		templatesTable.put(data.Template.b, new Integer(numberTemplates));
		templatesTable.put(data.Template.c, new Integer(numberTemplates));
		templatesTable.put(data.Template.d, new Integer(numberTemplates));
		templatesTable.put(data.Template.e, new Integer(numberTemplates));
		templatesTable.put(data.Template.f, new Integer(numberTemplates));
		templatesTable.put(data.Template.g, new Integer(numberTemplates));
		templatesTable.put(data.Template.h, new Integer(numberTemplates));
		templatesTable.put(data.Template.i, new Integer(numberTemplates));
		templatesTable.put(data.Template.j, new Integer(numberTemplates));
		templatesTable.put(data.Template.k, new Integer(numberTemplates));
		templatesTable.put(data.Template.l, new Integer(numberTemplates));
		templatesTable.put(data.Template.m, new Integer(numberTemplates));
		templatesTable.put(data.Template.n, new Integer(numberTemplates));
		templatesTable.put(data.Template.o, new Integer(numberTemplates));
		templatesTable.put(data.Template.p, new Integer(numberTemplates));
		templatesTable.put(data.Template.q, new Integer(numberTemplates));
		templatesTable.put(data.Template.r, new Integer(numberTemplates));
		templatesTable.put(data.Template.s, new Integer(numberTemplates));
		templatesTable.put(data.Template.t, new Integer(numberTemplates));
		templatesTable.put(data.Template.u, new Integer(numberTemplates));
		templatesTable.put(data.Template.v, new Integer(numberTemplates));
		templatesTable.put(data.Template.w, new Integer(numberTemplates));
		templatesTable.put(data.Template.x, new Integer(numberTemplates));
		templatesTable.put(data.Template.y, new Integer(numberTemplates));
		templatesTable.put(data.Template.z, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.triangle, new Integer(numberTemplates));
		templatesTable.put(data.Template.square, new Integer(numberTemplates));
		templatesTable.put(data.Template.rectangle, new Integer(numberTemplates));
		templatesTable.put(data.Template.circle, new Integer(numberTemplates));
		templatesTable.put(data.Template.pentagon, new Integer(numberTemplates));
		templatesTable.put(data.Template.parallelogram, new Integer(numberTemplates));
		templatesTable.put(data.Template.doubleSquare, new Integer(numberTemplates));
		templatesTable.put(data.Template.doubleCircle, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.rightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightArrowDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrowDownArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.rightArrowUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.leftArrowUpArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrowRightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.downArrowLeftArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrowRightArrow, new Integer(numberTemplates));
		templatesTable.put(data.Template.upArrowLeftArrow, new Integer(numberTemplates));
		
		templatesTable.put(data.Template.arrobas, new Integer(numberTemplates));
		templatesTable.put(data.Template.signature, new Integer(numberTemplates));
		
		Enumeration<data.Template> ttKeys = templatesTable.keys();
		
		
		templatesTableByString = new Hashtable<String, data.Template>();
		while(ttKeys.hasMoreElements()){
			Template curTemp = ttKeys.nextElement();
			templatesTableByString.put(curTemp.toString(), curTemp);
		}
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("records\\"+idToRecover+"\\templates.txt"));
			String currentTemplate = br.readLine();
			while(currentTemplate != null){
				add(templatesTableByString.get(currentTemplate.split(" ")[0]));
				currentTemplate = br.readLine();
			}
		}
		catch(IOException ioe){System.out.println("Le recovering du template set n'a pu etre fait.");}
		
	}
	
	
	public int initialSize(){
		return initialSize;
	}
	
	public Enumeration getTemplatesEnumeration(){
		return templatesTable.keys();
	} 
	
	public Hashtable<String, data.Template> templatesTableByString(){
		return templatesTableByString;
	}
	
	public boolean add(data.Template template){
		initialSize++;
		return super.add(template);
	}
	
	public void add(int index, data.Template template){
		initialSize++;
		super.add(index, template);
	}
	
	public String stringFormat(){
		String toReturn = "";
		Iterator<Template> templates = this.iterator();
		while(templates.hasNext()){
			Template tmp = templates.next();
			toReturn += tmp.toString() + " " + (numberTemplates - templatesTable.get(tmp).intValue() + 1) + "\n";
			templatesTable.put(tmp, templatesTable.get(tmp)-1);
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		System.out.println((new TemplateSet()).stringFormat());
	}
	
	
	
}
