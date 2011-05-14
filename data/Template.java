package data;

/*
 * Classe permettant de definir tous les templates utilises
 */
public class Template {
	
	public final static Template zero = new Template("zero", "Le chiffre zéro.");
	public final static Template one = new Template("one", "Le chiffre un.");
	public final static Template two = new Template("two", "Le chiffre deux.");
	public final static Template three = new Template("three", "Le chiffre trois.");
	public final static Template four = new Template("four", "Le chiffre quatre.");
	public final static Template five = new Template("five", "Le chiffre cinq.");
	public final static Template six = new Template("six", "Le chiffre six.");
	public final static Template seven = new Template("seven", "Le chiffre sept.");
	public final static Template eight = new Template("eight", "Le chiffre huit.");
	public final static Template nine = new Template("nine", "Le chiffre neuf.");
	
	public final static Template a = new Template("a", "La lettre minuscule a.");
	public final static Template b = new Template("b", "La lettre minuscule b.");
	public final static Template c = new Template("c", "La lettre minuscule c.");
	public final static Template d = new Template("d", "La lettre minuscule d.");
	public final static Template e = new Template("e", "La lettre minuscule e.");
	public final static Template f = new Template("f", "La lettre minuscule f.");
	public final static Template g = new Template("g", "La lettre minuscule g.");
	public final static Template h = new Template("h", "La lettre minuscule h.");
	public final static Template i = new Template("i", "La lettre minuscule i.");
	public final static Template j = new Template("j", "La lettre minuscule j.");
	public final static Template k = new Template("k", "La lettre minuscule k.");
	public final static Template l = new Template("l", "La lettre minuscule l.");
	public final static Template m = new Template("m", "La lettre minuscule m.");
	public final static Template n = new Template("n", "La lettre minuscule n.");
	public final static Template o = new Template("o", "La lettre minuscule o.");
	public final static Template p = new Template("p", "La lettre minuscule p.");
	public final static Template q = new Template("q", "La lettre minuscule q.");
	public final static Template r = new Template("r", "La lettre minuscule r.");
	public final static Template s = new Template("s", "La lettre minuscule s.");
	public final static Template t = new Template("t", "La lettre minuscule t.");
	public final static Template u = new Template("u", "La lettre minuscule u.");
	public final static Template v = new Template("v", "La lettre minuscule v.");
	public final static Template w = new Template("w", "La lettre minuscule w.");
	public final static Template x = new Template("x", "La lettre minuscule x.");
	public final static Template y = new Template("y", "La lettre minuscule y.");
	public final static Template z = new Template("z", "La lettre minuscule z.");
	
	public final static Template A = new Template("Amaj", "La lettre majuscule A.");
	public final static Template B = new Template("Bmaj", "La lettre majuscule B.");
	public final static Template C = new Template("Cmaj", "La lettre majuscule C.");
	public final static Template D = new Template("Dmaj", "La lettre majuscule D.");
	public final static Template E = new Template("Emaj", "La lettre majuscule E.");
	public final static Template F = new Template("Fmaj", "La lettre majuscule F.");
	public final static Template G = new Template("Gmaj", "La lettre majuscule G.");
	public final static Template H = new Template("Hmaj", "La lettre majuscule H.");
	public final static Template I = new Template("Imaj", "La lettre majuscule i.");
	public final static Template J = new Template("Jmaj", "La lettre majuscule J.");
	public final static Template K = new Template("Kmaj", "La lettre majuscule K.");
	public final static Template L = new Template("Lmaj", "La lettre majuscule L.");
	public final static Template M = new Template("Mmaj", "La lettre majuscule M.");
	public final static Template N = new Template("Nmaj", "La lettre majuscule N.");
	public final static Template O = new Template("Omaj", "La lettre majuscule O.");
	public final static Template P = new Template("Pmaj", "La lettre majuscule P.");
	public final static Template Q = new Template("Qmaj", "La lettre majuscule Q.");
	public final static Template R = new Template("Rmaj", "La lettre majuscule R.");
	public final static Template S = new Template("Smaj", "La lettre majuscule S.");
	public final static Template T = new Template("Tmaj", "La lettre majuscule T.");
	public final static Template U = new Template("Umaj", "La lettre majuscule U.");
	public final static Template V = new Template("Vmaj", "La lettre majuscule V.");
	public final static Template W = new Template("Wmaj", "La lettre majuscule W.");
	public final static Template X = new Template("Xmaj", "La lettre majuscule X.");
	public final static Template Y = new Template("Ymaj", "La lettre majuscule Y.");
	public final static Template Z = new Template("Zmaj", "La lettre majuscule Z.");
	
	public final static Template triangle = new Template("triangle", "Le triangle.");
	public final static Template square = new Template("square", "Le carré.");
	public final static Template rectangle = new Template("rectangle", "Le rectangle.");
	public final static Template circle = new Template("circle", "Le cercle.");
	public final static Template pentagon = new Template("pentagon", "Le pentagone.");
	public final static Template parallelogram = new Template("parallelogram", "Le parallélogramme.");
	public final static Template doubleSquare = new Template("doubleSquare", "Le double carré.");
	public final static Template doubleCircle = new Template("doubleCircle", "Le double cercle.");
	
	public final static Template rightArrow = new Template("rightArrow", "Ligne (SANS LES FLECHES) tracée vers la droite.");
	public final static Template leftArrow = new Template("leftArrow", "Ligne (SANS LES FLECHES) tracée vers la gauche.");
	public final static Template downArrow = new Template("downArrow", "Ligne (SANS LES FLECHES) tracée vers le bas.");
	public final static Template upArrow = new Template("upArrow", "Ligne (SANS LES FLECHES) tracée vers le haut.");
	public final static Template rightDownArrow = new Template("rightDownArrow", "Ligne (SANS LES FLECHES) tracée vers la droite et le bas.");
	public final static Template leftDownArrow = new Template("leftDownArrow", "Ligne (SANS LES FLECHES) tracée vers la gauche et le bas.");
	public final static Template rightUpArrow = new Template("rightUpArrow", "Ligne (SANS LES FLECHES) tracée vers la droite et le haut.");
	public final static Template leftUpArrow = new Template("leftUpArrow", "Ligne (SANS LES FLECHES) tracée vers la gauche et le haut.");
	public final static Template rightArrowDownArrow = new Template("rightArrowDownArrow", "Lignes (SANS LES FLECHES) tracées vers la droite puis vers le bas.");
	public final static Template leftArrowDownArrow = new Template("leftArrowDownArrow", "Lignes (SANS LES FLECHES) tracées vers la gauche puis vers le bas.");
	public final static Template rightArrowUpArrow = new Template("rightArrowUpArrow", "Lignes (SANS LES FLECHES) tracées vers la droite puis vers le haut.");
	public final static Template leftArrowUpArrow = new Template("leftArrowUpArrow", "Lignes (SANS LES FLECHES) tracées vers la gauche puis vers le haut.");
	public final static Template downArrowRightArrow = new Template("downArrowRightArrow", "Lignes (SANS LES FLECHES) tracées vers le bas puis vers la droite.");
	public final static Template downArrowLeftArrow = new Template("downArrowLeftArrow", "Lignes (SANS LES FLECHES) tracées vers le bas puis vers la gauche.");
	public final static Template upArrowRightArrow = new Template("upArrowRightArrow", "Lignes (SANS LES FLECHES) tracées vers le haut puis vers la droite.");
	public final static Template upArrowLeftArrow = new Template("upArrowLeftArrow", "Lignes (SANS LES FLECHES) tracées vers le haut puis vers la gauche.");
	
	public final static Template arrobas = new Template("arrobas", "L'arrobas.");
	
	public final static Template signature = new Template("signature", "Votre signature.");
	
	
	
	
	
	private String name, info;
	
	public Template(String name, String info){
		this.name = name;
		this.info = info;
	}
	
	public String getInformation(){
		return info;
	}
	
	public String toString(){
		return name;
	}

}
