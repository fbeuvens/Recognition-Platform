package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import tablet.TabletData;
import tablet.TabletDrawingArea;
import data.DataRecorder;
import data.Template;


/*
 * Classe definissant l'interieur de l'onglet permettant l'entrainement de l'utilisateur testeur pour 30 signatures
 * et 10 exemples pour chacune des autres classes
 */
public class Trainer extends JPanel {
	
	private static final long serialVersionUID = -4174229613749147509L;
	public ImagePanel ip, ip2;
	private JPanel buttons, jp1, jp2;

	private JButton cancel, save, previous ;
	
	private JProgressBar progressBar;


	public static JTextArea console;

	private JScrollPane scrollConsole;
	private TabletDrawingArea tda;

	private DataRecorder dr;
	
	int i, j;
	
	private boolean trainingMode = false;
	
	private InputMap im;
	private ActionMap am;
	private SaveAction sa;
	private EraseAction ea;
	private PreviousAction pa;
	private MappingAction ma;
	
	public Trainer(boolean trainingMode, int idToRecover){
		i = 0;
		j = 1;
		this.trainingMode = trainingMode;
		setSize((int)getToolkit().getScreenSize().getWidth(),(int)getToolkit().getScreenSize().getHeight()-30);

		ip = new ImagePanel();
		ip.setMinimumSize(new Dimension((int)(getSize().getWidth()*(4.0/5.0))-20,(int)(getSize().getHeight()*(4.0/5.0))-20));
	
		jp1 = new JPanel(new GridLayout(1,1));
		//jp1.add(ip);
		tda = new TabletDrawingArea();
		tda.setMinimumSize(new Dimension((int)(getSize().getWidth()*(4.0/5.0))-20,(int)(getSize().getHeight()*(4.0/5.0))-20));
		(new Thread(tda)).start();
		dr = new DataRecorder(tda, "trainer");
		jp1.add(tda);
		jp1.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Drawing area"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		
		im = jp1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		am = jp1.getActionMap();
		sa = new SaveAction();
		ea = new EraseAction();
		pa = new PreviousAction();
		ma = new MappingAction();

		
		im.put(KeyStroke.getKeyStroke('N'), "save");
		am.put("save", sa);
		im.put(KeyStroke.getKeyStroke('E'), "erase");
		am.put("erase", ea);
		im.put(KeyStroke.getKeyStroke('M'), "map");
		am.put("map", ma);
		im.put(KeyStroke.getKeyStroke('P'), "previous");
		am.put("previous", pa);

		ip2 = new ImagePanel();
		ip2.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(getSize().getWidth()*(1.0/5.0))-20));
		
		ip2.load("templates\\priseEnMain.jpg");

		jp2 = new JPanel(new GridLayout(1,1));
		jp2.add(ip2);
		jp2.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Template"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		

		console = new JTextArea();
		console.setEditable(false);
	    
		scrollConsole = new JScrollPane(console);
		scrollConsole.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollConsole.setMinimumSize(new Dimension((int)(getSize().getWidth())-40,(int)(getSize().getHeight()*(1.0/5.0))-40));
		scrollConsole.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Informations"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								scrollConsole.getBorder()));

		
		cancel = new JButton("Clear");
		cancel.addActionListener(ea);
		save = new JButton("Next");
		save.addActionListener(sa);
		previous = new JButton("Previous");
	
		previous.addActionListener(pa);
		
		

		GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes;
		buttons = new JPanel(new GridLayout(1,2));
		buttons.setLayout(grabRepartiteur);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(cancel, grabContraintes);
		buttons.add(cancel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(previous, grabContraintes);
		buttons.add(previous);
			


		progressBar = new JProgressBar(0,100);
		progressBar.setValue(0);
        progressBar.setStringPainted(true);




		GridBagLayout repartiteur = new GridBagLayout();
		GridBagConstraints contraintes;
		
		setLayout(repartiteur); 


		contraintes = new GridBagConstraints();
		contraintes.gridx = 0;
		contraintes.gridy = 0;
		contraintes.gridwidth = 4;
		contraintes.gridheight = 4;
		//contraintes.fill = GridBagConstraints.BOTH;
		//contraintes.weightx = 1;
		//contraintes.weighty = 1;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(jp1, contraintes);
		add(jp1);



		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 0;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 0.4;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(jp2, contraintes);
		add(jp2);

		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 1;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 20;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(save, contraintes);
		add(save);
		
		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 2;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(buttons, contraintes);
		add(buttons);
		
		
		
		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 3;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 1;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(progressBar, contraintes);
		add(progressBar);


		contraintes = new GridBagConstraints();
		contraintes.gridx = 0;
		contraintes.gridy = 4;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.gridheight = 1;
		contraintes.gridwidth = 5;
		contraintes.weightx = 1;
		contraintes.weighty = 1;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(scrollConsole, contraintes);
		add(scrollConsole);
		
		
		

		
		console.setText("Click on the next button when you are ready.\n");
		
		//setTitle("Enregistrement des donnees : id = " + dr.getID());
		
	}


	public void close(){
		tda.close();
		System.exit(0);
	}
	
	public void disable(){
		tda.disable();
	}
	
	public void enable(){
		tda.enable();
	}
	

	private class SaveAction extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8367546107173034601L;

		public void actionPerformed(ActionEvent e) {
			System.out.println("button trainer ersave pressed");
			if(!trainingMode)
				tda.saveDatas();
			jp2.removeAll();
			Template curTemplate = dr.next();
			if(curTemplate != null){
				ip2 = new ImagePanel();
				ip2.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(getSize().getWidth()*(1.0/5.0))-20));
				ip2.load("templates\\" + curTemplate.toString() + ".jpg");
				jp2.add(ip2);
				console.setText(console.getText() + curTemplate.getInformation() + "\n");
				progressBar.setValue( (int)(((double)(dr.getTemplateSet().initialSize()-dr.remainingNumberTemplates()-1))/dr.getTemplateSet().initialSize()*100) );
				if(!previous.isEnabled())
					previous.setEnabled(true);
			}
			else{
				tda.repaint();
				ip2 = new ImagePanel();
				ip2.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(getSize().getWidth()*(1.0/5.0))-20));
				jp2.add(ip2);
				console.setText("The tests are now finished. Thank you for your participation.");
				progressBar.setValue(100);
				cancel.setEnabled(false);
				save.setEnabled(false);
			}
			jp2.getParent().validate();
		}
		
	}
	
	private class EraseAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 170824460915017677L;

		public void actionPerformed(ActionEvent e){
			System.out.println("button trainer erase pressed");
			tda.clearRecorder();
			tda.repaint();
		}
	}
	
	private class PreviousAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 170824460915017677L;

		public void actionPerformed(ActionEvent e){
			System.out.println("button trainer previous pressed");	
			Template curTemplate = dr.previous();
			if(curTemplate==null) return;
			jp2.removeAll();
			ip2 = new ImagePanel();
			ip2.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(getSize().getWidth()*(1.0/5.0))-20));
			ip2.load("templates\\" + curTemplate.toString() + ".jpg");	
			jp2.add(ip2);
			console.setText(console.getText() + curTemplate.getInformation() + "\n");
			progressBar.setValue( (int)(((double)(dr.getTemplateSet().initialSize()-dr.remainingNumberTemplates()-1))/dr.getTemplateSet().initialSize()*100) );
			jp2.getParent().validate();
			previous.setEnabled(false);
			//tda.loadRecorder("records\\65\\" + ((char)(i+97)) + "\\" + ((char)(i+97)) + j + ".txt");
			//console.setText(console.getText() + ((char)(i+97)) + j + "\n");
			progressBar.setValue( (int)(((double)(dr.getTemplateSet().initialSize()-dr.remainingNumberTemplates()-1))/dr.getTemplateSet().initialSize()*100) );
			jp2.getParent().validate();
			/*if(i == 25 && j == 10)
				return;
			
			if(j == 10){
				j = 1;
				i++;
			}
			else 
				j++;*/
					
		}
	}
	
	private class MappingAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4462148955032660824L;

		public void actionPerformed(ActionEvent e){
			tda.mapping(new TabletData());
		}
	}
	
	
	public static void main (String[] args){
		Frame frame = new Frame();
		Trainer trainer;
		if(args.length == 1 && args[0].equals("-training"))
			trainer = new Trainer(true, -1);
		else 
			if(args.length == 2 && args[0].equals("-recover"))
				trainer = new Trainer(false, Integer.parseInt(args[1]));
			else trainer = new Trainer(false, -1);

		frame.add(trainer);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//trainer.close();
				System.exit(0);
			}
		});
		
		frame.setSize((int)frame.getToolkit().getScreenSize().getWidth(),(int)frame.getToolkit().getScreenSize().getHeight()-30);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setResizable(true);
		//System.out.println(frame.ip.getMinimumSize().getWidth());
		//System.out.println(frame.ip.getMinimumSize().getHeight());

		frame.setVisible(true);
		
		frame.setResizable(false);
		System.out.println("coucou");
		//System.out.println(frame.ip.getSize().getWidth());
		//System.out.println(frame.ip.getSize().getHeight());
	}
	


}
