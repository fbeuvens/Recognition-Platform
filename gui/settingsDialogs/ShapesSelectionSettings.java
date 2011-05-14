package gui.settingsDialogs;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

/*
 * Classe definissant la fenetre de dialogue permettant de parametrer les classes
 * d'exemples d'entrainement a prendre en compte lors de la reconnaissance
 */
public class ShapesSelectionSettings extends JPanel {
	final JCheckBox[] generalCheckBoxes = new JCheckBox[6]; //familles de classes (lettres, chiffres, gestes d'actions, formes geometriques, arrobas, signature)
	final JCheckBox[] lettersCheckBoxes = new JCheckBox[26]; //toutes les lettres
	final JCheckBox[] digitsCheckBoxes = new JCheckBox[10]; //tous les chiffres
	final JCheckBox[] shapesCheckBoxes = new JCheckBox[8]; //toutes les formes geometriques
	final JCheckBox[] gesturesCheckBoxes = new JCheckBox[16]; //tous les gestes d'action
	
	JLabel label;
    JFrame frame, mainFrame;
    
    public boolean[] generalSelection, lettersSelection, digitsSelection, shapesSelection, gesturesSelection;
	
	boolean validated = false;

	public ShapesSelectionSettings(JFrame frame){
    	this(frame, null, new boolean[6], new boolean[26], new boolean[10], new boolean[8], new boolean[16]);
    }
	
    public ShapesSelectionSettings(JFrame frame, JFrame mainFrame){
    	this(frame, mainFrame, new boolean[6], new boolean[26], new boolean[10], new boolean[8], new boolean[16]);
    }
    
   
    public ShapesSelectionSettings(JFrame frame, JFrame mainFrame, boolean[] generalSelection, boolean[] lettersSelection, boolean[] digitsSelection, boolean[] shapesSelection, boolean[] gesturesSelection) {
        super(new BorderLayout());
        this.frame = frame;
        this.mainFrame = mainFrame;
        this.generalSelection = generalSelection;
        this.lettersSelection = lettersSelection;
        this.digitsSelection = digitsSelection;
        this.shapesSelection = shapesSelection;
        this.gesturesSelection = gesturesSelection;
        
        if(generalSelection.length!= 6 || lettersSelection.length!= 26 || 
        		digitsSelection.length != 10 || shapesSelection.length!= 8 ||
        			gesturesSelection.length != 16){
        	System.out.println("Bad format for template checkboxes.");
        	System.exit(0);
        }

        //Create the components.
        JPanel generalPanel = createGeneralDialogBox();
        JPanel lettersPanel = createLettersDialogBox();
        JPanel digitsPanel = createDigitsDialogBox();
        JPanel shapesPanel = createShapesDialogBox();
        JPanel gesturesPanel = createGesturesDialogBox();
        label = new JLabel("Choose the used features.",
                           JLabel.CENTER);

        
        //Lay them out.
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", null,
                          generalPanel,
                          "General choice"); //tooltip text
        tabbedPane.addTab("Letters", null,
                lettersPanel,
                "Letters choice");
       tabbedPane.addTab("Digits", null,
                digitsPanel,
                "Digits choice");
       tabbedPane.addTab("Shapes", null,
               shapesPanel,
               "Shapes choice");
       tabbedPane.addTab("Gestures", null,
               gesturesPanel,
               "Gestures choice");
       
        add(tabbedPane, BorderLayout.CENTER);
        add(label, BorderLayout.PAGE_END);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    /** Sets the text displayed at the bottom of the frame. */
    void setLabel(String newText) {
        label.setText(newText);
    }
    
    /** Creates the panel shown by the first tab. */
    private JPanel createGeneralDialogBox() {
    	generalCheckBoxes[0] = new JCheckBox("Letters");
    	generalCheckBoxes[0].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < lettersCheckBoxes.length; i++){
        			lettersCheckBoxes[i].setSelected(generalCheckBoxes[0].isSelected());
        		}
        	}
    	});
    	
    	generalCheckBoxes[1] = new JCheckBox("Digits");
    	generalCheckBoxes[1].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < digitsCheckBoxes.length; i++){
        			digitsCheckBoxes[i].setSelected(generalCheckBoxes[1].isSelected());
        		}
        	}
    	});
    	
    	generalCheckBoxes[2] = new JCheckBox("Shapes");
    	generalCheckBoxes[2].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < shapesCheckBoxes.length; i++){
        			shapesCheckBoxes[i].setSelected(generalCheckBoxes[2].isSelected());
        		}
        	}
    	});
    	
    	generalCheckBoxes[3] = new JCheckBox("Gestures");
    	generalCheckBoxes[3].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < gesturesCheckBoxes.length; i++){
        			gesturesCheckBoxes[i].setSelected(generalCheckBoxes[3].isSelected());
        		}
        	}
    	});
    	
    	generalCheckBoxes[4] = new JCheckBox("Arrobas");
    	generalCheckBoxes[5] = new JCheckBox("Signature");
    	
    	for(int i = 0; i < generalSelection.length; i++)
    		generalCheckBoxes[i].setSelected(generalSelection[i]);
    	
    	JButton button = new JButton("Select/unselect all");
    	button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean allSelected = true;
        		for(int i = 0; i < generalCheckBoxes.length; i++){
        			allSelected = allSelected && generalCheckBoxes[i].isSelected();
        		}
        		for(int i = 0; i < generalCheckBoxes.length; i++){
        			generalCheckBoxes[i].setSelected(!allSelected);
        		}
        		for(int i = 0; i < lettersCheckBoxes.length; i++){
        			lettersCheckBoxes[i].setSelected(!allSelected);
        		}
        		for(int i = 0; i < digitsCheckBoxes.length; i++){
        			digitsCheckBoxes[i].setSelected(!allSelected);
        		}
        		for(int i = 0; i < shapesCheckBoxes.length; i++){
        			shapesCheckBoxes[i].setSelected(!allSelected);
        		}
        		for(int i = 0; i < gesturesCheckBoxes.length; i++){
        			gesturesCheckBoxes[i].setSelected(!allSelected);
        		}
        		
        	}
    	});
    	
    	return createGeneralPane(generalCheckBoxes, button);
   
    }

    private JPanel createLettersDialogBox() {
    	lettersCheckBoxes[0] = new JCheckBox("a");
    	lettersCheckBoxes[1] = new JCheckBox("b");
    	lettersCheckBoxes[2] = new JCheckBox("c");
    	lettersCheckBoxes[3] = new JCheckBox("d");
    	lettersCheckBoxes[4] = new JCheckBox("e");
    	lettersCheckBoxes[5] = new JCheckBox("f");
    	lettersCheckBoxes[6] = new JCheckBox("g");
    	lettersCheckBoxes[7] = new JCheckBox("h");
    	lettersCheckBoxes[8] = new JCheckBox("i");
    	lettersCheckBoxes[9] = new JCheckBox("j");
    	lettersCheckBoxes[10] = new JCheckBox("k");
    	lettersCheckBoxes[11] = new JCheckBox("l");
    	lettersCheckBoxes[12] = new JCheckBox("m");
    	lettersCheckBoxes[13] = new JCheckBox("n");
    	lettersCheckBoxes[14] = new JCheckBox("o");
    	lettersCheckBoxes[15] = new JCheckBox("p");
    	lettersCheckBoxes[16] = new JCheckBox("q");
    	lettersCheckBoxes[17] = new JCheckBox("r");
    	lettersCheckBoxes[18] = new JCheckBox("s");
    	lettersCheckBoxes[19] = new JCheckBox("t");
    	lettersCheckBoxes[20] = new JCheckBox("u");
    	lettersCheckBoxes[21] = new JCheckBox("v");
    	lettersCheckBoxes[22] = new JCheckBox("w");
    	lettersCheckBoxes[23] = new JCheckBox("x");
    	lettersCheckBoxes[24] = new JCheckBox("y");
    	lettersCheckBoxes[25] = new JCheckBox("z");
    	
    	for(int i = 0; i < lettersCheckBoxes.length; i++){
    		final int ifinal = i;
	    	lettersCheckBoxes[i].addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		if(lettersCheckBoxes[ifinal].isSelected())
	        			generalCheckBoxes[0].setSelected(true);
	        		else{
	        			boolean oneSelected = false;
		        		for(int j = 0; j < lettersCheckBoxes.length; j++)
		        			oneSelected = oneSelected || lettersCheckBoxes[j].isSelected();
		        		generalCheckBoxes[0].setSelected(oneSelected);
	        		}
	        	}
	    	});
    	}
    	
    	for(int i = 0; i < lettersSelection.length; i++)
    		lettersCheckBoxes[i].setSelected(lettersSelection[i]);
    	
    	JButton button = new JButton("Select/unselect all");
    	button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean allSelected = true;
        		for(int i = 0; i < lettersCheckBoxes.length; i++){
        			allSelected = allSelected && lettersCheckBoxes[i].isSelected();
        		}
        		for(int i = 0; i < lettersCheckBoxes.length; i++){
        			lettersCheckBoxes[i].setSelected(!allSelected);
        		}
        		generalCheckBoxes[0].setSelected(!allSelected);
        	}
    	});
    	
    	return createColPane(5, lettersCheckBoxes, button);
    }
    
    private JPanel createDigitsDialogBox() {
    	digitsCheckBoxes[0] = new JCheckBox("0");
    	digitsCheckBoxes[1] = new JCheckBox("1");
    	digitsCheckBoxes[2] = new JCheckBox("2");
    	digitsCheckBoxes[3] = new JCheckBox("3");
    	digitsCheckBoxes[4] = new JCheckBox("4");
    	digitsCheckBoxes[5] = new JCheckBox("5");
    	digitsCheckBoxes[6] = new JCheckBox("6");
    	digitsCheckBoxes[7] = new JCheckBox("7");
    	digitsCheckBoxes[8] = new JCheckBox("8");
    	digitsCheckBoxes[9] = new JCheckBox("9");
    	
    	for(int i = 0; i < digitsCheckBoxes.length; i++){
    		final int ifinal = i;
	    	digitsCheckBoxes[i].addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		if(digitsCheckBoxes[ifinal].isSelected())
	        			generalCheckBoxes[1].setSelected(true);
	        		else{
	        			boolean oneSelected = false;
		        		for(int j = 0; j < digitsCheckBoxes.length; j++)
		        			oneSelected = oneSelected || digitsCheckBoxes[j].isSelected();
		        		generalCheckBoxes[1].setSelected(oneSelected);
	        		}
	        	}
	    	});
    	}
    	
    	for(int i = 0; i < digitsSelection.length; i++)
    		digitsCheckBoxes[i].setSelected(digitsSelection[i]);
    	
    	
    	JButton button = new JButton("Select/unselect all");
    	button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean allSelected = true;
        		for(int i = 0; i < digitsCheckBoxes.length; i++){
        			allSelected = allSelected && digitsCheckBoxes[i].isSelected();
        		}
        		for(int i = 0; i < digitsCheckBoxes.length; i++){
        			digitsCheckBoxes[i].setSelected(!allSelected);
        		}
        		generalCheckBoxes[1].setSelected(!allSelected);
        	}
    	});
    	
    	return createColPane(2, digitsCheckBoxes, button);
    }
    
    private JPanel createShapesDialogBox() {
    	shapesCheckBoxes[0] = new JCheckBox("triangle");
    	shapesCheckBoxes[1] = new JCheckBox("square");
    	shapesCheckBoxes[2] = new JCheckBox("rectangle");
    	shapesCheckBoxes[3] = new JCheckBox("circle");
    	shapesCheckBoxes[4] = new JCheckBox("pentagon");
    	shapesCheckBoxes[5] = new JCheckBox("parallelogram");
    	shapesCheckBoxes[6] = new JCheckBox("double square");
    	shapesCheckBoxes[7] = new JCheckBox("double circle");
    	
    	for(int i = 0; i < shapesCheckBoxes.length; i++){
    		final int ifinal = i;
	    	shapesCheckBoxes[i].addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		if(shapesCheckBoxes[ifinal].isSelected())
	        			generalCheckBoxes[2].setSelected(true);
	        		else{
	        			boolean oneSelected = false;
		        		for(int j = 0; j < shapesCheckBoxes.length; j++)
		        			oneSelected = oneSelected || lettersCheckBoxes[j].isSelected();
		        		generalCheckBoxes[2].setSelected(oneSelected);
	        		}
	        	}
	    	});
    	}
    	
    	for(int i = 0; i < shapesSelection.length; i++)
    		shapesCheckBoxes[i].setSelected(shapesSelection[i]);
    	
    	
    	JButton button = new JButton("Select/unselect all");
    	button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean allSelected = true;
        		for(int i = 0; i < shapesCheckBoxes.length; i++){
        			allSelected = allSelected && shapesCheckBoxes[i].isSelected();
        		}
        		for(int i = 0; i < shapesCheckBoxes.length; i++){
        			shapesCheckBoxes[i].setSelected(!allSelected);
        		}
        		generalCheckBoxes[2].setSelected(!allSelected);
        	}
    	});
    	
    	return createColPane(2, shapesCheckBoxes, button);
    }
    
    private JPanel createGesturesDialogBox() {
    	gesturesCheckBoxes[0] = new JCheckBox("right");
    	gesturesCheckBoxes[1] = new JCheckBox("left");
    	gesturesCheckBoxes[2] = new JCheckBox("down");
    	gesturesCheckBoxes[3] = new JCheckBox("up");
    	gesturesCheckBoxes[4] = new JCheckBox("right-down (diagonal)");
    	gesturesCheckBoxes[5] = new JCheckBox("left-down (diagonal)");
    	gesturesCheckBoxes[6] = new JCheckBox("right-up (diagonal)");
    	gesturesCheckBoxes[7] = new JCheckBox("left-up (diagonal)");
    	gesturesCheckBoxes[8] = new JCheckBox("right then down");
    	gesturesCheckBoxes[9] = new JCheckBox("left then down");
    	gesturesCheckBoxes[10] = new JCheckBox("right then up");
    	gesturesCheckBoxes[11] = new JCheckBox("left then down");
    	gesturesCheckBoxes[12] = new JCheckBox("down then right");
    	gesturesCheckBoxes[13] = new JCheckBox("down then left");
    	gesturesCheckBoxes[14] = new JCheckBox("up then right");
    	gesturesCheckBoxes[15] = new JCheckBox("up then left");
    	
    	for(int i = 0; i < gesturesCheckBoxes.length; i++){
    		final int ifinal = i;
	    	gesturesCheckBoxes[i].addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		if(gesturesCheckBoxes[ifinal].isSelected())
	        			generalCheckBoxes[3].setSelected(true);
	        		else{
	        			boolean oneSelected = false;
		        		for(int j = 0; j < gesturesCheckBoxes.length; j++)
		        			oneSelected = oneSelected || digitsCheckBoxes[j].isSelected();
		        		generalCheckBoxes[3].setSelected(oneSelected);
	        		}
	        	}
	    	});
    	}
    	
    	for(int i = 0; i < gesturesSelection.length; i++)
    		gesturesCheckBoxes[i].setSelected(gesturesSelection[i]);
    	
    	
    	JButton button = new JButton("Select/unselect all");
    	button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean allSelected = true;
        		for(int i = 0; i < gesturesCheckBoxes.length; i++){
        			allSelected = allSelected && gesturesCheckBoxes[i].isSelected();
        		}
        		for(int i = 0; i < gesturesCheckBoxes.length; i++){
        			gesturesCheckBoxes[i].setSelected(!allSelected);
        		}
        		generalCheckBoxes[3].setSelected(!allSelected);
        	}
    	});
    	
    	return createColPane(2, gesturesCheckBoxes, button);
    }

    

   
    
    private JPanel createGeneralPane(JCheckBox[] checkBoxes,
            JButton selectAll) {

		int numChoices = checkBoxes.length;
		JPanel box = new JPanel();
		
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		
		for (int i = 0; i < numChoices; i++) {
		box.add(checkBoxes[i]);
		}
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		GridBagLayout grabRepartiteur = new GridBagLayout();
		GridBagConstraints grabContraintes;
		JPanel buttons = new JPanel();
		
		buttons.setLayout(grabRepartiteur);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < generalSelection.length; i++)
        			generalCheckBoxes[i].setSelected(generalSelection[i]);
        		for(int i = 0; i < lettersSelection.length; i++)
        			lettersCheckBoxes[i].setSelected(lettersSelection[i]);
        		for(int i = 0; i < digitsSelection.length; i++)
        			digitsCheckBoxes[i].setSelected(digitsSelection[i]);
        		for(int i = 0; i < shapesSelection.length; i++)
        			shapesCheckBoxes[i].setSelected(shapesSelection[i]);
        		for(int i = 0; i < gesturesSelection.length; i++)
        			gesturesCheckBoxes[i].setSelected(gesturesSelection[i]);
        		validated = false;
        		mainFrame.setEnabled(true);
        		frame.setVisible(false);
        	}
    	});
		
		JButton validate = new JButton("Validate");
		validate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < generalSelection.length; i++)
        			generalSelection[i] = generalCheckBoxes[i].isSelected();
        		for(int i = 0; i < lettersSelection.length; i++)
        			lettersSelection[i] = lettersCheckBoxes[i].isSelected();
        		for(int i = 0; i < digitsSelection.length; i++)
        			digitsSelection[i] = digitsCheckBoxes[i].isSelected();
        		for(int i = 0; i < shapesSelection.length; i++)
        			shapesSelection[i] = shapesCheckBoxes[i].isSelected();
        		for(int i = 0; i < gesturesSelection.length; i++)
        			gesturesSelection[i] = gesturesCheckBoxes[i].isSelected();
        		validated = true;
        		mainFrame.setEnabled(true);
        		frame.setVisible(false);
        	}
    	});

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.gridwidth = 2;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(selectAll, grabContraintes);
		buttons.add(selectAll);

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

   
     
     private JPanel createColPane(int numberColumns,
             JCheckBox[] checkBoxes,
             JButton showButton) {
		int numPerColumn = checkBoxes.length/numberColumns;
		int remainingBoxes = checkBoxes.length%numberColumns;
		
		
		JPanel grid = new JPanel(new GridLayout(0, numberColumns));
				
		for(int i = 0; i < numPerColumn; i++){
			for (int j = i; j < checkBoxes.length; j+=numPerColumn) {
				grid.add(checkBoxes[j]);
				if(remainingBoxes > 0){
					j++;
					remainingBoxes--;
				}
			}
			remainingBoxes = checkBoxes.length%numberColumns;
		}
		
		for(int i = numPerColumn; i < checkBoxes.length && remainingBoxes > 0; i+=numberColumns){
			grid.add(checkBoxes[i]);
			remainingBoxes--;
		}
		
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		grid.setAlignmentX(0.0f);
		box.add(grid);
		
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		pane.add(showButton, BorderLayout.PAGE_END);
		
		return pane;
}

    

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ShapesSettings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ShapesSelectionSettings newContentPane = new ShapesSelectionSettings(frame);
        //ShapeSettingsDialog newContentPane = new ShapeSettingsDialog(frame, new boolean[6], new boolean[26], new boolean[10], new boolean[8], new boolean[16]);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setSize(350, 350);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

