package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import tablet.TabletDrawingArea;

/*
 * Classe permettant de parametrer l'objet TabletDrawingArea en vue de lui permettre
 * d'enregistrer les données qu'il recoit au bon endroit. Permet egalement de creer, 
 * lire et mettre à jour les fichiers texte contenant les informations sur les templates
 * restant à entrainer. 
 */

public class DataRecorder {
	
	private int currentID; //id de l'utilisateur en cours de traitement
	private String previousTemplate, currentTemplate; //template en cours de reproduction et template precedent pour le mode entrainement
	
	private TabletDrawingArea tda; //permet de recevoir les notifications envoyées par le stylet de l'utilisateur
	private TemplateSet ts; //ensemble de tous les templates disponibles
	private int remainingNumberTemplates; //nombre de templates encore à reproduire pour le mode entrainement
	
	/*
	 * pre: specialMode vaut "trainer" ou "templateAdder"
	 * post: tous les dossiers pemettant de recueillier les donnees sont crees dans le dossier "records" et sont classes
	 * 		 par id (valant dernier id du dossier +1 par défaut, 0 dans le mode entrainement de l'interface graphiqhe, -1
	 * 		 dans le mode d'ajout de template un a un), puis par classe.
	 */
	public DataRecorder(TabletDrawingArea tda, String specialMode){
		if(specialMode.equals("trainer"))
			currentID = 0;
		else if(specialMode.equals("templateAdder"))
				currentID = -1;
		else
			currentID = lastID() + 1;
		
		this.tda = tda;
		ts = new TemplateSet();
		
		File file = new File("records\\"+currentID); 
		
		cleanFolder(file);
		
		file.mkdir();
		
		Enumeration<Template> templates = ts.getTemplatesEnumeration();
		while(templates.hasMoreElements())
				(new File("records\\"+currentID+"\\"+templates.nextElement().toString())).mkdir();
		
		
		if(currentID!=-1){
			try{
				String toWrite = ts.stringFormat();
				BufferedWriter bw = new BufferedWriter(new FileWriter("records\\"+currentID+"\\templates.txt"));
				bw.write(toWrite);
				bw.close();
				BufferedWriter bw2 = new BufferedWriter(new FileWriter("records\\"+currentID+"\\templatesSave.txt"));
				bw2.write(toWrite);
				bw2.close();
			}
			catch(IOException ioe){System.out.println("Le fichier texte des templates n'a pu etre ecrit.");}
		}
		
		remainingNumberTemplates = ts.initialSize();
	}
	
	
	/*
	 * pre: idToRecover un id d'utilisateur ayant deja produit une partie d'entrainement
	 * post: recupere les donnees de l'utilisateur idToRecover et reprend l'entrainement ou il s'etait interrompu
	 */
	public DataRecorder(TabletDrawingArea tda, int idToRecover){
		if(idToRecover != -1){
			this.tda = tda;
			ts = new TemplateSet(idToRecover);
			currentID = idToRecover;
		}
	}
	
	
	
	/*
	 * pre: -
	 * post: paramètre le TabletDrawingArea en lui signifiant le fichier d'enregistrement pour le template template afin d'ajouter un exemple
	 */
	public void setForAdditionTemplate(String template){
		tda.setRecorder("records\\-1\\" + template + "\\" + template + (numberElements("records\\-1\\" + template)+1) + ".txt");
		tda.repaint();
	}
	
	/*
	 * pre: -
	 * post: paramètre le TabletDrawingArea en lui signifiant le fichier d'enregistrement pour le template template afin de recommencer le dernier exemple
	 */
	public void setForRedrawingTemplate(String template){
		tda.loadRecorder("records\\-1\\" + template + "\\" + template + numberElements("records\\-1\\" + template) + ".txt");
		tda.repaint();
	}

	
	/*
	 * pre: -
	 * post: le prochain template a reproduire est depile et renvoye, 
	 * 		 le TabletDrawingArea est parametre en fonction
	 */
	public Template next(){
		try{
			previousTemplate = currentTemplate;
			BufferedReader br = new BufferedReader(new FileReader("records\\"+currentID+"\\templates.txt"));
			currentTemplate = br.readLine();
			if(currentTemplate == null) return null;
			String otherTemplates = "";
			String tmp = br.readLine();
			remainingNumberTemplates = 0;
			while(tmp != null){
				remainingNumberTemplates++;
				otherTemplates += tmp +"\n";
				tmp = br.readLine();
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter("records\\"+currentID+"\\templates.txt"));
			bw.write(otherTemplates);
			bw.close();
			String[] currentTemplateSplitted = currentTemplate.split(" ");
			tda.setRecorder("records\\" + currentID + "\\" + currentTemplateSplitted[0] + "\\" + currentTemplateSplitted[0] + currentTemplateSplitted[1] + ".txt");
			tda.repaint();
			return ts.templatesTableByString().get(currentTemplateSplitted[0]);
		}
		catch(IOException ioe){System.out.println("Le nombre de templates faits n'a pu etre mis a jour.");}
		return null;
	}
	
	
	/*
	 * pre: -
	 * post: le precedent template a reproduire est renvoye, 
	 * 		 le TabletDrawingArea est parametre en fonction
	 */
	public Template previous(){
		try{
			if(previousTemplate != null){
				BufferedReader br = new BufferedReader(new FileReader("records\\"+currentID+"\\templates.txt"));
				if(currentTemplate == null) return null;
				String otherTemplates = "";
				String tmp = br.readLine();
				remainingNumberTemplates = 1;
				while(tmp != null){
					remainingNumberTemplates++;
					otherTemplates += tmp +"\n";
					tmp = br.readLine();
				}
				br.close();
				BufferedWriter bw = new BufferedWriter(new FileWriter("records\\"+currentID+"\\templates.txt"));
				bw.write(currentTemplate + "\n" + otherTemplates);
				bw.close();
				currentTemplate = previousTemplate;
				previousTemplate = null;
				String[] currentTemplateSplitted = currentTemplate.split(" ");
				tda.loadRecorder("records\\" + currentID + "\\" + currentTemplateSplitted[0] + "\\" + currentTemplateSplitted[0] + currentTemplateSplitted[1] + ".txt");
				//tda.setRecorder("records\\" + currentID + "\\" + currentTemplateSplitted[0] + "\\" + currentTemplateSplitted[0] + currentTemplateSplitted[1] + ".txt");
				tda.repaint();
				return ts.templatesTableByString().get(currentTemplateSplitted[0]);
			}
		}
		catch(IOException ioe){System.out.println("Le nombre de templates faits n'a pu etre mis a jour.");}
		return null;
	}
	
	/*
	 * pre: -
	 * post: le nombre de templates restants est renvoye
	 */
	public int remainingNumberTemplates(){
		return remainingNumberTemplates;
	}
	
	/*
	 * pre: -
	 * post: l'id de l'utilisateur courant est renvoye
	 */
	public int getID(){
		return currentID;
	}
	
	/*
	 * pre: -
	 * post: l'id le plus eleve contenu dans le dossier "records" est renvoye
	 */
	private int lastID(){
		int lastID = 0;
		String[] idFolders = (new File("records").list());
		for(int i = 0; i < idFolders.length; i++){
			try{
				if(Integer.parseInt(idFolders[i]) > lastID)
					lastID = Integer.parseInt(idFolders[i]);
			}
			catch(NumberFormatException nfe){};
		}
		return lastID;
	}
	
	/*
	 * pre: -
	 * post: le nombre d'elements du dossier "folder" est renvoye
	 */
	public static int numberElements(String folder){
		System.out.println(folder);
		return (new File(folder).list()).length;
	}
	
	
	/*
	 * pre: -
	 * post: le dossier folderID est nettoye
	 */
	private void cleanFolder(File folderID){
		
		if(folderID.exists()){
			String[] elements = folderID.list();
			for(int i = 0; i < elements.length; i++){
				File file = new File(folderID.getAbsolutePath()+"\\"+elements[i]);
				if(file.isDirectory()){
					String[] elements2 = file.list();
					for(int j = 0; j < elements2.length; j++){
						(new File(file.getAbsolutePath()+"\\"+elements2[j])).delete();
					}
				}
				else
					file.delete();
			}
		}
	}
	
	public TemplateSet getTemplateSet(){
		return ts;
	}
	
	public static void main(String[] args){
		DataRecorder dr = new DataRecorder(null, null);
		dr.next();
	}

}
