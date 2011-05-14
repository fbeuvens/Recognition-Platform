package gui.settingsDialogs;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

/*
 * Classe definissant la fenetre de dialogue permettant de parametrer l'algorithme
 * de Rubine lors de la reconnaissance
 */
public class RubineSettings extends JPanel {
	
	private JLabel dataProcessing;
	private JCheckBox resamplingCB; //pretraitement: reechantillonnage
	private JCheckBox rotationCB; //pretraitement: rotation
	private JCheckBox rescalingCB; //pretraitement: redimensionnement
	private JCheckBox translationCB; //pretraitement: translation
    JFrame frame, mainFrame;
	
	boolean validated = false;
	public boolean resampling, rotation, rescaling, translation;
    
    /** Creates the GUI shown inside the frame's content pane. */
    public RubineSettings(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
     
        JPanel generalPanel = createPane();

        add(generalPanel, BorderLayout.CENTER);
        
    }

    
    public RubineSettings(JFrame frame, JFrame mainFrame) {
        super(new BorderLayout());
        this.frame = frame;
        this.mainFrame = mainFrame;
     
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
		
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(dpPanel2, BorderLayout.PAGE_START);
		
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
		
		pane.add(buttons, BorderLayout.PAGE_END);
		return pane;
    }


    private static void createAndShowGUI() {
        JFrame frame = new JFrame("RubineSettings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RubineSettings newContentPane = new RubineSettings(frame);
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setSize(230, 170);
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

