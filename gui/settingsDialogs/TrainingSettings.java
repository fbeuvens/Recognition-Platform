package gui.settingsDialogs;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import java.awt.*;
import java.awt.event.*;

/*
 * Classe definissant la fenetre de dialogue permettant de parametrer les options 
 * d'entrainement lors de la reconnaissance
 */
public class TrainingSettings extends JPanel {
	
	private JCheckBox udCheckBox; //choix de l'ajout des exemples d'entrainement de l'utilisateur testeur
	private JCheckBox uiCheckBox; //choix de l'ajout des exemples d'entrainement compris dans la base de donnees
	private JRadioButton signature; //choix de l'ajout des signatures
	private ButtonGroup bg;
	
	
	JFormattedTextField udField; //nombre d'exemples d'entrainement maximum par classe pour l'utilisateur testeur a prendre en compte 
	private JComboBox uiUsersBox; //nombre d'utilisateurs de la base de donnees a ajouter 
	private JComboBox uiNteBox; //nombre d'exemples d'entrainement maximum par classe pour les utilisateurs compris dans la base de donnees a prendre en compte
	
    private JFrame frame, mainFrame;
	
	boolean validated = false;
    
	public boolean ud, ui;
	public int udNte, uiNte, uiNu;
	
    public TrainingSettings(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        udNte = uiNte = uiNu = 1;
        
        JPanel generalPanel = createPane();

        add(generalPanel, BorderLayout.CENTER);
    }
    
    public TrainingSettings(JFrame frame, JFrame mainFrame) {
        super(new BorderLayout());
        this.frame = frame;
        this.mainFrame = mainFrame;
        udNte = uiNte = uiNu = 1;
        
        JPanel generalPanel = createPane();

        add(generalPanel, BorderLayout.CENTER);
    }
  
    

   
    
    private JPanel createPane() {

    	
    	JPanel udPanel = new JPanel();
    	udPanel.setLayout(new BoxLayout(udPanel, BoxLayout.LINE_AXIS));
    	udCheckBox = new JCheckBox("User dependent : ");
    	JLabel lab = new JLabel("number training examples per class = ");
    	udField = new JFormattedTextField(udNte+"");
    	udPanel.add(udCheckBox);
    	udPanel.add(lab);
    	udPanel.add(udField);
    	
    	
    	JPanel uiPanel = new JPanel();
    	uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.LINE_AXIS));
    	uiCheckBox = new JCheckBox("User independent : ");
    	
    	
    	JPanel uiPanelPart = new JPanel();
    	uiPanelPart.setLayout(new BoxLayout(uiPanelPart, BoxLayout.PAGE_AXIS));
    	GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes;
    	JPanel uiPanelPart1 = new JPanel(grabRepartiteur);
    	JLabel usersLabel = new JLabel("number users = ");
    	String[] uiUsersStrings = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
    	uiUsersBox = new JComboBox(uiUsersStrings);
    	grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		//grabContraintes.fill = GridBagConstraints.NONE; 
		grabRepartiteur.setConstraints(usersLabel, grabContraintes);
    	uiPanelPart1.add(usersLabel);
    	grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 0;
		grabContraintes.fill = GridBagConstraints.NONE; 
		grabRepartiteur.setConstraints(uiUsersBox, grabContraintes);
    	uiPanelPart1.add(uiUsersBox);
    	
    	
    	JPanel uiPanelPart2 = new JPanel();
    	uiPanelPart2.setLayout(new BoxLayout(uiPanelPart2, BoxLayout.LINE_AXIS));
    	JLabel nteLabel = new JLabel("number training examples per class = ");
    	String[] uiNteStrings = {"1","2","3","4","5","6","7","8","9","10"};
    	uiNteBox = new JComboBox(uiNteStrings);
    	uiPanelPart2.add(nteLabel);
    	uiPanelPart2.add(uiNteBox);
    	
    	uiPanelPart.add(uiPanelPart1);
    	uiPanelPart.add(uiPanelPart2);
    	
    	uiPanel.add(uiCheckBox);
    	uiPanel.add(uiPanelPart);
    	
    	
    	signature = new JRadioButton("Signature");

    	
    	bg = new ButtonGroup();
    	//bg.add(udCheckBox);
    	//bg.add(uiCheckBox);
    	bg.add(signature);
    	
    	
    	grabRepartiteur = new GridBagLayout();
		JPanel buttons = new JPanel();
		
		buttons.setLayout(grabRepartiteur);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		udCheckBox.setText(ud+"");
        		uiCheckBox.setText(ui+"");
        		udField.setText(udNte+"");
        		uiNteBox.setSelectedIndex(uiNte-1);
        		uiUsersBox.setSelectedIndex(uiNu-1);
        		validated = false;
        		mainFrame.setEnabled(true);
        		frame.setVisible(false);
        	}
    	});
		
		JButton validate = new JButton("Validate");
		validate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ud = udCheckBox.isSelected();
        		ui = uiCheckBox.isSelected();
        		udNte = Integer.parseInt(udField.getText());
        		uiNte = uiNteBox.getSelectedIndex()+1;
        		uiNu = uiUsersBox.getSelectedIndex()+1;
        		validated = true;
        		mainFrame.setEnabled(true);
        		frame.setVisible(false);
        	}
    	});
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(cancel, grabContraintes);
		buttons.add(cancel);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(validate, grabContraintes);
		buttons.add(validate);
    	
		
		
		grabRepartiteur = new GridBagLayout();
		JPanel pane = new JPanel(grabRepartiteur);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(udPanel, grabContraintes);
		pane.add(udPanel);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.NONE; 
		grabRepartiteur.setConstraints(uiPanel, grabContraintes);
		pane.add(uiPanel);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 2;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(signature, grabContraintes);
		//pane.add(signature);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 2;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.NONE; 
		grabRepartiteur.setConstraints(buttons, grabContraintes);
		pane.add(buttons);
		
		return pane;
    }


   
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Recognizer settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TrainingSettings newContentPane = new TrainingSettings(frame);
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setSize(480, 200);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

