package gui.settingsDialogs;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;

/*
 * Classe definissant la fenetre de dialogue permettant de parametrer l'algorithme
 * de One Dollar lors de la reconnaissance
 */
public class OneDollarSettings extends JPanel {
	
	private JFormattedTextField knnField; //nombre de plus proche voisins
	public int knn;
    JFrame frame, mainFrame;
	
	boolean validated = false;
    
    /** Creates the GUI shown inside the frame's content pane. */
    public OneDollarSettings(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        knn = 1;
     
        JPanel generalPanel = createPane();

        add(generalPanel, BorderLayout.CENTER);
        
    }

    public OneDollarSettings(JFrame frame, JFrame mainFrame) {
        super(new BorderLayout());
        this.frame = frame;
        this.mainFrame = mainFrame;
        knn = 1;
     
        JPanel generalPanel = createPane();

        add(generalPanel, BorderLayout.CENTER);
        
    }
    

   
    
    private JPanel createPane() {

		JPanel box = new JPanel();
		
		box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
		
		knnField = new JFormattedTextField(knn);
		box.add(new JLabel(" knn ( >0 ) : "));
		box.add(knnField);
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		
		GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes;
		JPanel buttons = new JPanel();
		
		buttons.setLayout(grabRepartiteur);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		knnField.setText(knn+"");
        		validated = false;
        		mainFrame.setEnabled(true);
        		frame.setVisible(false);
        	}
    	});
		
		JButton validate = new JButton("Validate");
		validate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
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
		
		pane.add(buttons, BorderLayout.PAGE_END);
		return pane;
    }

    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("OneDollar settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        OneDollarSettings newContentPane = new OneDollarSettings(frame);
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        frame.pack();
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

