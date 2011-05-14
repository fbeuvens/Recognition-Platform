package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import tablet.TabletData;
import tablet.TabletDrawingArea;
import data.DataRecorder;

/*
 * Classe definissant l'interieur de l'onglet permettant d'ajouter un exemple d'entrainement quelconque
 */
public class TemplateAdder extends JPanel {

private static final long serialVersionUID = -4174229613749147509L;

	private JPanel buttons, jp1, jp2;
	
	private ImagePanel ip;

	private JButton cancel, save, erase;
	
	private JComboBox symbolSelectBox;

	private JTextArea console;

	private JScrollPane scrollConsole;
	
	private TabletDrawingArea tda;

	private DataRecorder dr;
	
	private String lastTemplate, currentTemplate;
	
	private InputMap im;
	private ActionMap am;
	private SaveAction sa;
	private EraseAction ea;
	private PreviousAction pa;
	private MappingAction ma;


	public TemplateAdder(){
		setSize((int)getToolkit().getScreenSize().getWidth(),(int)getToolkit().getScreenSize().getHeight()-30);
		
		jp1 = new JPanel(new GridLayout(1,1));
		tda = new TabletDrawingArea();
		tda.setMinimumSize(new Dimension((int)(getSize().getWidth()*(4.0/5.0))-20,(int)(getSize().getHeight()*(4.0/5.0))-20));
		(new Thread(tda)).start();
		
		dr = new DataRecorder(tda, "templateAdder");
		
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
		
		
		

		ip = new ImagePanel();
		ip.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(getSize().getWidth()*(1.0/5.0))-20));
		
		ip.load("templates\\a.jpg");
		lastTemplate = null;
		currentTemplate = "a";
		dr.setForAdditionTemplate(currentTemplate);

		GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes = new GridBagConstraints();;
		
		String[] symbolList = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","triangle","square","rectangle","circle","pentagon","parallelogram","doubleSquare","doubleCircle","rightArrow","leftArrow","downArrow","upArrow","rightDownArrow","leftDownArrow","rightUpArrow","leftUpArrow","rightArrowDownArrow","leftArrowDownArrow","rightArrowUpArrow","leftArrowUpArrow","downArrowRightArrow","downArrowLeftArrow","upArrowRightArrow","upArrowLeftArrow","arrobas","signature"};
		symbolSelectBox = new JComboBox(symbolList);
		symbolSelectBox.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(symbolSelectBox.getSelectedItem().equals("0"))
        			currentTemplate = "zero";
        		else if(symbolSelectBox.getSelectedItem().equals("1"))
        			currentTemplate = "one";
        		else if(symbolSelectBox.getSelectedItem().equals("2"))
        			currentTemplate = "two";
        		else if(symbolSelectBox.getSelectedItem().equals("3"))
        			currentTemplate = "three";
        		else if(symbolSelectBox.getSelectedItem().equals("4"))
        			currentTemplate = "four";
        		else if(symbolSelectBox.getSelectedItem().equals("5"))
        			currentTemplate = "five";
        		else if(symbolSelectBox.getSelectedItem().equals("6"))
        			currentTemplate = "six";
        		else if(symbolSelectBox.getSelectedItem().equals("7"))
        			currentTemplate = "seven";
        		else if(symbolSelectBox.getSelectedItem().equals("8"))
        			currentTemplate = "eight";
        		else if(symbolSelectBox.getSelectedItem().equals("9"))
        			currentTemplate = "nine";
        		else
        			currentTemplate = symbolSelectBox.getSelectedItem().toString();
        		
        		ip.load("templates\\" + currentTemplate + ".jpg");
        		dr.setForAdditionTemplate(currentTemplate);
        	}
    	});
		
		
		jp2 = new JPanel(new GridLayout(2,1));
		jp2.setLayout(grabRepartiteur);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.BOTH;
		grabRepartiteur.setConstraints(ip, grabContraintes);
		jp2.add(ip);

		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(symbolSelectBox, grabContraintes);
		jp2.add(symbolSelectBox);
		
		
		
		
		jp2.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Template"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		//templateAdderJp2.setMaximumSize(ip.getMinimumSize());
		

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
		save = new JButton("Add template");
		save.addActionListener(sa);
		erase = new JButton("Redraw last example");
		erase.setEnabled(false);
		erase.addActionListener(pa);
		
		
		

		grabRepartiteur = new GridBagLayout();
		grabContraintes = new GridBagConstraints();;
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
		grabRepartiteur.setConstraints(erase, grabContraintes);
		buttons.add(erase);
		
		/*grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 2;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(erase, grabContraintes);
		buttons.add(erase);*/

		//buttons.setMinimumSize(new Dimension((int)(getSize().getWidth()*(1.0/5.0))-20,(int)(0.5*((getSize().getHeight())-(getSize().getWidth()*(1.0/5.0)-20)-(getSize().getHeight()*(1.0/5.0))-20))));
		
		

		

		GridBagLayout repartiteur = new GridBagLayout();
		GridBagConstraints contraintes;
		setLayout(repartiteur);
		//interieur.setLayout(repartiteur); 


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
			System.out.println("button templateadd save pressed");
			tda.saveDatas();
			lastTemplate = currentTemplate;
			console.append("\n"+symbolSelectBox.getSelectedItem()+" recorded");
			
			dr.setForAdditionTemplate(currentTemplate);
			jp2.getParent().validate();
			erase.setEnabled(true);
			symbolSelectBox.setEnabled(true);
		}
		
	}
	
		
	private class EraseAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 170824460915017677L;

		public void actionPerformed(ActionEvent e){
			System.out.println("button templateadd erase pressed");
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
			currentTemplate = lastTemplate;
			console.append("\nRedraw example: "+currentTemplate);
			symbolSelectBox.setSelectedItem(currentTemplate);
			symbolSelectBox.setEnabled(false);
			dr.setForRedrawingTemplate(currentTemplate);
			jp2.getParent().validate();
			erase.setEnabled(false);
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
	
	
}
