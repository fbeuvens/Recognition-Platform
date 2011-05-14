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
 * Classe definissant la fenetre de dialogue permettant de parametrer l'algorithme
 * de Levenshtein lors de la reconnaissance
 */
public class LevenshteinSettings extends JPanel {

	private JLabel dataProcessing;
	private JCheckBox resamplingCB; //pretraitement: reechantillonnage
	private JCheckBox rotationCB; //pretraitement: rotation
	private JCheckBox rescalingCB; //pretraitement: redimensionnement
	private JCheckBox translationCB; //pretraitement: translation

	private JComboBox normalisationCB; //choix de la normalisation
	public JComboBox featuresCB; //choix des caracteristiques a prendre en compte (direction, pression, angle, orientation)
	private JComboBox costsCB;

	//prise en compte des trous ou non
	private JRadioButton withHoleRB;
	private JRadioButton withoutHoleRB;

	private ButtonGroup bg;

	private JFormattedTextField knnField; //nombre de plus proche voisins


	private JFrame frame, mainFrame;

	boolean validated = false;

	public boolean resampling, rotation, rescaling, translation, withHoles;
	public int normalisation, features, costs, knn;

	
	public LevenshteinSettings(JFrame frame, JFrame mainFrame) {
		super(new BorderLayout());
		this.frame = frame;
		this.mainFrame = mainFrame;
		knn = 1;

		JPanel generalPanel = createPane();

		add(generalPanel, BorderLayout.CENTER);
	}



	private JPanel createPane() {

		JPanel dpPanel1 = new JPanel();
		dpPanel1.setLayout(new BoxLayout(dpPanel1, BoxLayout.PAGE_AXIS));

		resamplingCB = new JCheckBox("Resampling");
		rotationCB = new JCheckBox("Rotation");
		rescalingCB = new JCheckBox("Rescaling");
		translationCB = new JCheckBox("Translation");
		dpPanel1.add(resamplingCB);
		dpPanel1.add(rotationCB);
		dpPanel1.add(rescalingCB);
		dpPanel1.add(translationCB);


		dataProcessing = new JLabel(" Data processing :    ");
		JPanel dpPanel2 = new JPanel();
		dpPanel2.setLayout(new BoxLayout(dpPanel2, BoxLayout.LINE_AXIS));

		GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes;
		dpPanel2.add(dataProcessing);
		dpPanel2.add(dpPanel1);


		JPanel holePanel = new JPanel();
		holePanel.setLayout(new BoxLayout(holePanel, BoxLayout.LINE_AXIS));
		withHoleRB = new JRadioButton("with");
		withHoleRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//resamplingCB.setSelected(false);
				rotationCB.setSelected(false);
				rescalingCB.setSelected(false);
				translationCB.setSelected(false);
				//resamplingCB.setEnabled(false);
				rotationCB.setEnabled(false);
				rescalingCB.setEnabled(false);
				translationCB.setEnabled(false);
				costsCB.setEnabled(false);
			}
		});
		withoutHoleRB = new JRadioButton("without");
		withoutHoleRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(featuresCB.getSelectedIndex() == 0){
					//resamplingCB.setEnabled(true);
					rotationCB.setEnabled(true);
					rescalingCB.setEnabled(true);
					translationCB.setEnabled(true);
					costsCB.setEnabled(true);
				}
			}
		});
		withoutHoleRB.setSelected(true);
		bg = new ButtonGroup();
		bg.add(withHoleRB);
		bg.add(withoutHoleRB);


		holePanel.add(new JLabel("Holes : "));
		holePanel.add(withHoleRB);
		holePanel.add(withoutHoleRB);


		grabRepartiteur = new GridBagLayout();
		JPanel normPanel = new JPanel(grabRepartiteur);
		JLabel normLabel = new JLabel("Normalisation :     ");
		String[] normStrings = {"None","1","2","3"};
		normalisationCB = new JComboBox(normStrings);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabRepartiteur.setConstraints(normLabel, grabContraintes);
		normPanel.add(normLabel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(normalisationCB, grabContraintes);
		normPanel.add(normalisationCB);


		grabRepartiteur = new GridBagLayout();
		JPanel featurePanel = new JPanel(grabRepartiteur);
		JLabel featureLabel = new JLabel("Features :               ");
		String[] featureStrings = {"Direction","Pressure","Angle","Orientation","Pressure difference","Angle difference","Orientation difference","Direction and pressure difference"};
		featuresCB = new JComboBox(featureStrings);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabRepartiteur.setConstraints(featureLabel, grabContraintes);
		featurePanel.add(featureLabel);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(featuresCB, grabContraintes);
		featurePanel.add(featuresCB);
		
		featuresCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(featuresCB.getSelectedIndex()==0){
					if(withoutHoleRB.isSelected()){
						resamplingCB.setEnabled(true);
						rotationCB.setEnabled(true);
						rescalingCB.setEnabled(true);
						translationCB.setEnabled(true);
						costsCB.setEnabled(true);
						withHoleRB.setEnabled(true);
					}
				}
				else{
					resamplingCB.setSelected(false);
					rotationCB.setSelected(false);
					rescalingCB.setSelected(false);
					translationCB.setSelected(false);
					resamplingCB.setEnabled(false);
					rotationCB.setEnabled(false);
					rescalingCB.setEnabled(false);
					translationCB.setEnabled(false);
					costsCB.setSelectedIndex(0);
					costsCB.setEnabled(false);
					withoutHoleRB.setSelected(true);
					withHoleRB.setEnabled(false);
				}
			}
		});

		
		
		grabRepartiteur = new GridBagLayout();
		JPanel costsPanel = new JPanel(grabRepartiteur);
		JLabel costsLabel = new JLabel("Costs :                    ");
		String[] costsStrings = {"01 substitution","01234 substitution"};
		costsCB = new JComboBox(costsStrings);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabRepartiteur.setConstraints(costsLabel, grabContraintes);
		costsPanel.add(costsLabel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(costsCB, grabContraintes);
		costsPanel.add(costsCB);

		


		grabRepartiteur = new GridBagLayout();
		JPanel knnPanel = new JPanel(grabRepartiteur);

		knnField = new JFormattedTextField(knn);
		JLabel knnLabel = new JLabel(" knn ( >0 ) :   ");

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		//grabContraintes.weightx = 0.5;
		//grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(knnLabel, grabContraintes);
		knnPanel.add(knnLabel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 1;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(knnField, grabContraintes);
		knnPanel.add(knnField);



		grabRepartiteur = new GridBagLayout();
		JPanel buttons = new JPanel();

		buttons.setLayout(grabRepartiteur);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resamplingCB.setSelected(resampling);
				rotationCB.setSelected(rotation);
				rescalingCB.setSelected(rescaling);
				translationCB.setSelected(translation);
				withHoleRB.setSelected(withHoles);
				withoutHoleRB.setSelected(!withHoles);
				normalisationCB.setSelectedIndex(normalisation);
				featuresCB.setSelectedIndex(features);
				costsCB.setSelectedIndex(costs);
				knnField.setText(knn+"");
				validated = false;
				mainFrame.setEnabled(true);
				frame.setVisible(false);
			}
		});

		JButton validate = new JButton("Validate");
		validate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resampling = resamplingCB.isSelected();
				rotation = rotationCB.isSelected();
				rescaling = rescalingCB.isSelected();
				translation = translationCB.isSelected();
				withHoles = withHoleRB.isSelected();
				normalisation = normalisationCB.getSelectedIndex();
				features = normalisationCB.getSelectedIndex();
				costs = costsCB.getSelectedIndex();
				knn = Integer.parseInt(knnField.getText());
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
		grabRepartiteur.setConstraints(dpPanel2, grabContraintes);
		pane.add(dpPanel2);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(holePanel, grabContraintes);
		pane.add(holePanel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 2;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(featurePanel, grabContraintes);
		pane.add(featurePanel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 3;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(normPanel, grabContraintes);
		pane.add(normPanel);
		
		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 4;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(costsPanel, grabContraintes);
		pane.add(costsPanel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 5;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(knnPanel, grabContraintes);
		pane.add(knnPanel);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = GridBagConstraints.PAGE_END;
		grabContraintes.weighty = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL; 
		grabRepartiteur.setConstraints(buttons, grabContraintes);
		pane.add(buttons);

		return pane;
	}


	
	
	private static void createAndShowGUI() {
	
		JFrame frame = new JFrame("Levenshtein settings");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		LevenshteinSettings newContentPane = new LevenshteinSettings(frame, frame);
		newContentPane.setOpaque(true); 
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setSize(350, 340);
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

