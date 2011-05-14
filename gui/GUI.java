package gui;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * Definition de la fenetre principale de l'interface graphique
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = -4174229613749147509L;
	
	private JTabbedPane jtp; //conteneur des onglets
	
	private Trainer trainer; //contenu du premier onglet correspondant a l'interface permettant d'entrainer un ensemble de gestes suivant des templates a reproduire
	private TemplateAdder templateAdder; //contenu du deuxieme onglet correspondant a l'interface permettant d'ajouter un geste quelconque a l'ensemble d'entrainement
	private Recognizer recognizer; //contenu du troisieme onglet correspondant a l'interface permettant de faire la reconaissance proprement dite

	
	/*
	 * pre: -
	 * post: les onglets ainsi que leur interieur sont crees et associes
	 */
	public GUI(){
		System.out.println("début");
		setSize((int)getToolkit().getScreenSize().getWidth(),(int)getToolkit().getScreenSize().getHeight()-30);
		setResizable(false);
		
		jtp = new JTabbedPane(); 
		
		trainer = new Trainer(false, 0);
		templateAdder = new TemplateAdder();
		recognizer = new Recognizer(this);
		
		jtp.addTab("Training", trainer);
		jtp.addTab("Template Addition", templateAdder);
		templateAdder.disable();
		jtp.addTab("Recognition", recognizer);
		recognizer.disable();
		jtp.addChangeListener(new changeTabListener());
		add(jtp);
		
		
		
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				close();
				System.exit(0);
			}
		});

		setTitle("Recognizer");
		
	}

	/*
	 * pre: -
	 * post: tous les onglets sont fermes
	 */
	public void close(){
		trainer.close();
		templateAdder.close();
		recognizer.close();
		System.exit(0);
	}
	
	/*
	 * pre: -
	 * post: lorsqu'un onglet est affiche, les autres sont desactives
	 */
	private class changeTabListener implements ChangeListener {
		public void stateChanged(ChangeEvent e){
			if(jtp.getSelectedComponent() == trainer){
				templateAdder.disable();
				recognizer.disable();
				trainer.enable();
			}
			else
				if(jtp.getSelectedComponent() == templateAdder){
					trainer.disable();
					recognizer.disable();
					templateAdder.enable();
				}
				else{
					trainer.disable();
					templateAdder.disable();
					recognizer.enable();
				}
		}
	}
	

	
	public static void main(String args[]){
		
		GUI frame;
		
		frame = new GUI();

		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setResizable(true);
		

		frame.setVisible(true);
		
		frame.setResizable(false);

	}
	


}

