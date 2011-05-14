package gui;

import gui.settingsDialogs.LevenshteinSettings;
import gui.settingsDialogs.OneDollarSettings;
import gui.settingsDialogs.TrainingSettings;
import gui.settingsDialogs.RubineSettings;
import gui.settingsDialogs.ShapesSelectionSettings;
import gui.settingsDialogs.StochasticLevenshteinSettings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import algorithm.levenshtein.Levenshtein;
import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;
import algorithm.stochasticLevenshtein.ConditionalRecognizer;


import tablet.TabletData;
import tablet.TabletDrawingArea;
import test.ui.letters.Utils;
import data.DataRecorder;
import data.Dot;
import data.RecordParser;
import data.Template;


/*
 * Classe definissant l'interieur de l'onglet permettant la reconnaissance
 */
public class Recognizer extends JPanel {
	private static final long serialVersionUID = -4174229613749147509L;

	private JFrame rubineFrame, oneDollarFrame, levenshteinFrame, stochasticLevenshteinFrame, shapesFrame, recognitionFrame; 

	private JPanel jp, algos, devices, shapes;

	private JButton algosButton, shapesButton, train, recognize, clear, trainingSettings;

	private JComboBox algosBox, devicesBox, shapesBox;

	private JTextArea console;

	private JScrollPane scrollConsole;

	private TabletDrawingArea tda;

	private DataRecorder dr;

	private InputMap im;
	private ActionMap am;
	private SaveAction sa;
	private EraseAction ea;
	private PreviousAction pa;
	private MappingAction ma;

	private JFrame mainFrame;

	//classifieur pour les quatre algorithmes possibles
	private algorithm.rubine.Classifier rubineClassifier;
	private algorithm.oneDollar.Recognizer oneDollarClassifier;
	private algorithm.levenshtein.Levenshtein levenshteinClassifier;
	private algorithm.stochasticLevenshtein.ConditionalRecognizer stochasticLevenshteinClassifier;

	//variables correspondant a toutes les options possibles pour tous les algorithmes
	private boolean rubineResampling, rubineRotation, rubineRescaling, rubineTranslation;

	private int oneDollarKnn;

	private boolean levenshteinResampling, levenshteinRotation, levenshteinRescaling, levenshteinTranslation, levenshteinWithHoles;
	private int levenshteinNormalisation, levenshteinKnn, levenshteinFeatures, levenshteinCosts;

	private boolean stochasticLevenshteinResampling, stochasticLevenshteinRotation, stochasticLevenshteinRescaling, stochasticLevenshteinTranslation, stochasticLevenshteinWithHoles;
	private int stochasticLevenshteinNormalisation, stochasticLevenshteinKnn, stochasticLevenshteinFeatures;

	public Recognizer(JFrame frame){
		this.mainFrame = frame;
		setSize((int)getToolkit().getScreenSize().getWidth(),(int)getToolkit().getScreenSize().getHeight()-30);
		//setResizable(false);

		jp = new JPanel(new GridLayout(1,1));
		tda = new TabletDrawingArea();
		tda.setMinimumSize(new Dimension((int)(getSize().getWidth()*(4.0/5.0))-20,(int)(getSize().getHeight()*(4.0/5.0))-20));
		(new Thread(tda)).start();
		tda.setRecorder("records/toRecognize.txt");
		tda.repaint();

		//dr = new DataRecorder(tda,"recognizer");

		jp.add(tda);
		jp.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Drawing area"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));

		im = jp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		am = jp.getActionMap();
		sa = new SaveAction();
		ea = new EraseAction();
		pa = new PreviousAction();
		ma = new MappingAction();

		//im.put(KeyStroke.getKeyStroke('N',java.awt.event.InputEvent.CTRL_DOWN_MASK + java.awt.event.InputEvent.SHIFT_DOWN_MASK), "save");
		im.put(KeyStroke.getKeyStroke('N'), "save");
		am.put("save", sa);
		im.put(KeyStroke.getKeyStroke('E'), "erase");
		am.put("erase", ea);
		im.put(KeyStroke.getKeyStroke('M'), "map");
		am.put("map", ma);
		im.put(KeyStroke.getKeyStroke('P'), "previous");
		am.put("previous", pa);






		console = new JTextArea();
		console.setEditable(false);
		console.append("\n\n\n\n\n\n");
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

		recognitionFrame = new JFrame("Recognition settings");
		recognitionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		recognitionFrame.setContentPane(new TrainingSettings(recognitionFrame, mainFrame));
		recognitionFrame.setSize(480, 200);
		recognitionFrame.setResizable(false);

		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tda.clearRecorder();
				tda.repaint();
			}
		});


		trainingSettings = new JButton("Training settings");
		trainingSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(false);
				recognitionFrame.setVisible(true);
			}
		});

		train = new JButton("Train");
		train.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				train();
			}
		});

		recognize = new JButton("Recognize");
		recognize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recognize();
			}
		});

		JPanel trainButtons = new JPanel(new GridLayout(2,1));
		trainButtons.add(train);
		trainButtons.add(trainingSettings);

		GridBagLayout grabRepartiteur = new GridBagLayout();

		algos = new JPanel(new GridLayout(1,2));
		algos.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Algorithms"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		algos.setLayout(grabRepartiteur);


		rubineFrame = new JFrame("Rubine settings");
		rubineFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		rubineFrame.setContentPane(new RubineSettings(rubineFrame, mainFrame));
		rubineFrame.setSize(230, 170);
		rubineFrame.setResizable(false);

		oneDollarFrame = new JFrame("OneDollar settings");
		oneDollarFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		oneDollarFrame.setContentPane(new OneDollarSettings(oneDollarFrame, mainFrame));
		oneDollarFrame.pack();
		oneDollarFrame.setResizable(false);

		levenshteinFrame = new JFrame("Levenshtein settings");
		levenshteinFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		levenshteinFrame.setContentPane(new LevenshteinSettings(levenshteinFrame, mainFrame));
		levenshteinFrame.setSize(350,340);
		levenshteinFrame.setResizable(false);

		stochasticLevenshteinFrame = new JFrame("Stochastic Levenshtein settings");
		stochasticLevenshteinFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		stochasticLevenshteinFrame.setContentPane(new StochasticLevenshteinSettings(stochasticLevenshteinFrame, mainFrame));
		stochasticLevenshteinFrame.setSize(350,300);
		stochasticLevenshteinFrame.setResizable(false);

		algosButton = new JButton("Settings");
		algosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String)algosBox.getSelectedItem();
				if(selectedItem.equals("Rubine")){
					mainFrame.setEnabled(false);
					rubineFrame.setVisible(true);
				}
				if(selectedItem.equals("OneDollar")){
					mainFrame.setEnabled(false);
					oneDollarFrame.setVisible(true);
				}
				if(selectedItem.equals("Levenshtein")){
					mainFrame.setEnabled(false);
					levenshteinFrame.setVisible(true);
				}
				if(selectedItem.equals("Stochastic Levenshtein")){
					mainFrame.setEnabled(false);
					stochasticLevenshteinFrame.setVisible(true);
				}
			}
		});
		String[] algosList = {"Rubine", "OneDollar", "Levenshtein", "Stochastic Levenshtein"};
		algosBox = new JComboBox(algosList);

		GridBagConstraints grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(algosBox, grabContraintes);
		algos.add(algosBox);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(algosButton, grabContraintes);
		algos.add(algosButton);


		devices = new JPanel(new GridLayout(1,2));
		devices.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("devices"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		devices.setLayout(grabRepartiteur);

		String[] devicesList = {"Wacom"};
		devicesBox = new JComboBox(devicesList);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(devicesBox, grabContraintes);
		devices.add(devicesBox);

		shapes = new JPanel(new GridLayout(1,2));
		shapes.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Shapes"),
								BorderFactory.createEmptyBorder(1,1,1,1)),
								BorderFactory.createEtchedBorder()));
		shapes.setLayout(grabRepartiteur);
		String[] shapesList = {"All", "Letters", "Digits", "Geometrical shapes", "Gestures", "Signature", "Customize"};
		shapesBox = new JComboBox(shapesList);
		shapesButton = new JButton("Settings");
		shapesButton.setEnabled(false);
		shapesBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(shapesBox.getSelectedItem().equals("Customize"))
					shapesButton.setEnabled(true);
				else
					shapesButton.setEnabled(false);

			}
		});

		shapesFrame = new JFrame("Shapes settings");
		shapesFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		shapesFrame.setContentPane(new ShapesSelectionSettings(shapesFrame, mainFrame));
		shapesFrame.setSize(350,350);
		shapesFrame.setResizable(false);

		shapesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(false);
				shapesFrame.setVisible(true);
			}
		});


		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 0;
		grabContraintes.weightx = 0.5;
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(shapesBox, grabContraintes);
		shapes.add(shapesBox);

		grabContraintes = new GridBagConstraints();
		grabContraintes.gridx = 0;
		grabContraintes.gridy = 1;
		grabContraintes.weightx = 0.5;
		
		grabContraintes.fill = GridBagConstraints.HORIZONTAL;
		grabRepartiteur.setConstraints(shapesButton, grabContraintes);
		shapes.add(shapesButton);



		GridBagLayout repartiteur = new GridBagLayout();
		GridBagConstraints contraintes;
		setLayout(repartiteur);


		contraintes = new GridBagConstraints();
		contraintes.gridx = 0;
		contraintes.gridy = 0;
		contraintes.gridwidth = 4;
		contraintes.gridheight = 6;
		//contraintes.fill = GridBagConstraints.BOTH;
		//contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(jp, contraintes);
		add(jp);


		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 0;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(algos, contraintes);
		add(algos);

		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 1;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(devices, contraintes);
		add(devices);

		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 2;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(shapes, contraintes);
		add(shapes);

		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 3;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(clear, contraintes);
		add(clear);


		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 4;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(trainButtons, contraintes);
		add(trainButtons);

		contraintes = new GridBagConstraints();
		contraintes.gridx = 4;
		contraintes.gridy = 5;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.weightx = 1;
		contraintes.weighty = 10;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(recognize, contraintes);
		add(recognize);

		
		
		contraintes = new GridBagConstraints();
		contraintes.gridx = 0;
		contraintes.gridy = 6;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.gridheight = 1;
		contraintes.gridwidth = 5;
		contraintes.weightx = 1;
		contraintes.weighty = 0.05;
		contraintes.insets = new Insets(10, 10, 0, 10);
		repartiteur.setConstraints(scrollConsole, contraintes);
		add(scrollConsole);
		
		

	}

	/*
	 * pre: -
	 * post: l'entrainement est produit pour l'algorithme specifie ainsi que toutes les options
	 */
	private void train(){

		boolean ud = ((TrainingSettings)recognitionFrame.getContentPane()).ud;
		boolean ui = ((TrainingSettings)recognitionFrame.getContentPane()).ui;

		int udNte = ((TrainingSettings)recognitionFrame.getContentPane()).udNte;
		int uiNu = ((TrainingSettings)recognitionFrame.getContentPane()).uiNu;
		int uiNte = ((TrainingSettings)recognitionFrame.getContentPane()).uiNte;


		List<String> classes = new ArrayList<String>();

		if(shapesBox.getSelectedIndex() == 0){
			for(int i = 0; i < 26; i++)
				classes.add(""+test.ud.letters.Utils.intToChar(i));
			for(int i = 0; i < 10; i++)
				classes.add(test.ud.digits.Utils.intToDigit(i));
			for(int i = 0; i < 8; i++)
				classes.add(test.ud.shapes.Utils.intToShape(i));
			for(int i = 0; i < 16; i++)
				classes.add(test.ud.gestures.Utils.intToGest(i));
			classes.add("arrobas");
			classes.add("signature");
		}

		if(shapesBox.getSelectedIndex() == 1){
			for(int i = 0; i < 26; i++)
				classes.add(""+test.ud.letters.Utils.intToChar(i));
		}

		if(shapesBox.getSelectedIndex() == 2){
			for(int i = 0; i < 10; i++)
				classes.add(test.ud.digits.Utils.intToDigit(i));
		}

		if(shapesBox.getSelectedIndex() == 3){
			for(int i = 0; i < 8; i++)
				classes.add(test.ud.shapes.Utils.intToShape(i));
		}

		if(shapesBox.getSelectedIndex() == 4){
			for(int i = 0; i < 16; i++)
				classes.add(test.ud.gestures.Utils.intToGest(i));
		}

		if(shapesBox.getSelectedIndex() == 5){
			classes.add("signature");
		}

		if(shapesBox.getSelectedIndex() == 6){
			boolean[] generalSelection = ((ShapesSelectionSettings)shapesFrame.getContentPane()).generalSelection;
			boolean[] lettersSelection = ((ShapesSelectionSettings)shapesFrame.getContentPane()).lettersSelection;
			boolean[] digitsSelection = ((ShapesSelectionSettings)shapesFrame.getContentPane()).digitsSelection;
			boolean[] shapesSelection = ((ShapesSelectionSettings)shapesFrame.getContentPane()).shapesSelection;
			boolean[] gesturesSelection = ((ShapesSelectionSettings)shapesFrame.getContentPane()).gesturesSelection;

			if(generalSelection[0])
				for(int i = 0; i < lettersSelection.length; i++)
					if(lettersSelection[i])
						classes.add(""+test.ud.letters.Utils.intToChar(i));

			if(generalSelection[1])
				for(int i = 0; i < digitsSelection.length; i++)
					if(digitsSelection[i])
						classes.add(test.ud.digits.Utils.intToDigit(i));

			if(generalSelection[2])
				for(int i = 0; i < shapesSelection.length; i++)
					if(shapesSelection[i])
						classes.add(test.ud.shapes.Utils.intToShape(i));

			if(generalSelection[3])
				for(int i = 0; i < gesturesSelection.length; i++)
					if(gesturesSelection[i])
						classes.add(test.ud.gestures.Utils.intToGest(i));

			if(generalSelection[4])
				classes.add("arrobas");

			if(generalSelection[5])
				classes.add("signature");
		}

		if(!ud && !ui){
			JOptionPane.showMessageDialog(mainFrame, "No training method selected.","Training issue",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(classes.size() == 0){
			JOptionPane.showMessageDialog(mainFrame, "No class selected.","Recognition issue",JOptionPane.ERROR_MESSAGE);
		}
		else{
			if(ud){
				int nbrExamples = 0;
				for(int i = 0; i < classes.size(); i++)
					nbrExamples += DataRecorder.numberElements("records/-1/"+classes.get(i))+DataRecorder.numberElements("records/0/"+classes.get(i));
				if(nbrExamples == 0){
					JOptionPane.showMessageDialog(mainFrame, "No training examples available for the selected classes.","Training issue",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
				
			
			String trainingConsole = "Recognizer trained for :\n";
			if(ud && ui)
				if(uiNte == 1)
					trainingConsole += "\t-> tester user examples (max "+udNte+" per class) and database's user 1 examples (max "+uiNte+" per class)\n";
				else
					trainingConsole += "\t-> tester user examples (max "+udNte+" per class) and database's users (1 to "+uiNu+") examples (max "+uiNte+" per class)\n";
			else 
				if(ud)
					trainingConsole += "\t-> tester user examples (max "+udNte+" per class)\n";
				else
					if(ui)
						if(uiNte == 1)
							trainingConsole += "\t-> database's user 1 examples (max "+uiNte+" per class)\n";
						else
							trainingConsole += "\t-> database's users (1 to "+uiNu+") examples (max "+uiNte+" per class)\n";

			trainingConsole += "\t-> selected classes ";
			for(int i = 0; i < classes.size(); i++)
				trainingConsole += classes.get(i)+" ";
			trainingConsole += "\n";


			if(algosBox.getSelectedIndex() == 0){

				boolean resampling = ((RubineSettings)rubineFrame.getContentPane()).resampling;
				rubineResampling = resampling;
				boolean rotation = ((RubineSettings)rubineFrame.getContentPane()).rotation;
				rubineRotation = rotation;
				boolean rescaling = ((RubineSettings)rubineFrame.getContentPane()).rescaling;
				rubineRescaling = rescaling;
				boolean translation = ((RubineSettings)rubineFrame.getContentPane()).translation;
				rubineTranslation = translation;




				trainingConsole += "\t-> Rubine's algorithm with";
				if(!resampling && !rotation && !rescaling && !translation)
					trainingConsole += "out preprocessing";
				if(resampling)
					trainingConsole += " resampling";
				if(rotation)
					trainingConsole += " rotation";
				if(rescaling)
					trainingConsole += " rescaling";
				if(translation)
					trainingConsole += " translation";

				if(ud && ui){

					String noTemplateClasses = "";
					for(int i = 0; i < classes.size(); i++)
						if(DataRecorder.numberElements("records/-1/"+classes.get(i))+DataRecorder.numberElements("records/0/"+classes.get(i)) == 0)
							noTemplateClasses += classes.get(i)+" ";
					if(uiNu == 1 && uiNte == 1 && !noTemplateClasses.equals("")){
						JOptionPane.showMessageDialog(mainFrame, "Rubine needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
						console.setText(console.getText()+"\nPlease add example for classes:\n"+noTemplateClasses);
						return;
					}
					else{
						Classifier classifier = new Classifier();
						for(int i = 0; i < classes.size(); i++){
							GestureClass gc = new GestureClass(classes.get(i));
							if(classes.get(i).equals("signature"))
								gc = new GestureClass("your signature");

							for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
								Gesture gest = new Gesture();
								List<Dot> dotList = (new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
								gest.addGesture(dotList, resampling, rotation, rescaling, translation);
								gc.addExample(gest);
							}

							for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
								Gesture gest = new Gesture();
								List<Dot> dotList = (new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
								gest.addGesture(dotList, resampling, rotation, rescaling, translation);
								gc.addExample(gest);
							}
							if(classes.get(i).equals("signature"))
								classifier.addClass(gc);

							for(int j = 1; j <= uiNu; j++){
								if(classes.get(i).equals("signature"))
									gc = new GestureClass("user"+j+" signature");
								for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
									Gesture gest = new Gesture();
									List<Dot> dotList = (new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
									gest.addGesture(dotList, resampling, rotation, rescaling, translation);
									gc.addExample(gest);
								}
								if(classes.get(i).equals("signature"))
									classifier.addClass(gc);
							}
							classifier.addClass(gc);
						}

						classifier.compile();
						rubineClassifier = classifier;
					}


				}
				else
					if(ud){
						if(udNte < 2){
							JOptionPane.showMessageDialog(mainFrame, "Rubine needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
							console.setText(console.getText()+"\nPlease change training settings.");
							return;
						}
						
						String noEnoughTemplateClasses = "";
						for(int i = 0; i < classes.size(); i++)
							if(DataRecorder.numberElements("records/-1/"+classes.get(i))+DataRecorder.numberElements("records/0/"+classes.get(i)) < 2)
								noEnoughTemplateClasses += classes.get(i)+" ";
						if(!noEnoughTemplateClasses.equals("")){
							JOptionPane.showMessageDialog(mainFrame, "Rubine needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
							console.setText(console.getText()+"\nPlease add examples for classes:\n"+noEnoughTemplateClasses);
							return;
						}
						else{
							Classifier classifier = new Classifier();
							for(int i = 0; i < classes.size(); i++){
								GestureClass gc = new GestureClass(classes.get(i));
								if(classes.get(i).equals("signature"))
									gc = new GestureClass("your signature");
								
								

								for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
									Gesture gest = new Gesture();
									List<Dot> dotList = (new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
									gest.addGesture(dotList, resampling, rotation, rescaling, translation);
									gc.addExample(gest);
									System.out.println(classes.get(i));
								}

								for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
									Gesture gest = new Gesture();
									List<Dot> dotList = (new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
									gest.addGesture(dotList, resampling, rotation, rescaling, translation);
									gc.addExample(gest);
									System.out.println(classes.get(i));
								}
								classifier.addClass(gc);
							}

							classifier.compile();
							rubineClassifier = classifier;
						}
					}
					else
						if(ui){
							if(uiNu == 1 && uiNte == 1){
								JOptionPane.showMessageDialog(mainFrame, "Rubine needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
								return;
							}
							else{
								Classifier classifier = new Classifier();
								for(int i = 0; i < classes.size(); i++){
									GestureClass gc = new GestureClass(classes.get(i));

									for(int j = 1; j <= uiNu; j++){
										if(classes.get(i).equals("signature"))
											gc = new GestureClass("user"+j+" signature");
										for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
											Gesture gest = new Gesture();
											List<Dot> dotList = (new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
											gest.addGesture(dotList, resampling, rotation, rescaling, translation);
											gc.addExample(gest);
										}
										if(classes.get(i).equals("signature"))
											classifier.addClass(gc);
									}
									classifier.addClass(gc);
								}

								classifier.compile();
								rubineClassifier = classifier;
							}
						}

			}

			if(algosBox.getSelectedIndex() == 1){
				int knn = ((OneDollarSettings)oneDollarFrame.getContentPane()).knn;
				oneDollarKnn = knn;

				trainingConsole += "\t-> OneDollar's algorithm";

				if(ud && ui){
					algorithm.oneDollar.Recognizer recognizer = new algorithm.oneDollar.Recognizer(new File(""));
					for(int i = 0; i < classes.size(); i++){

						for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/-1/"+classes.get(i))-DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
							ArrayList<Point> points = new ArrayList<Point>();
							List<Dot> dotList = (new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
							Iterator<Dot> it = dotList.iterator();
							while(it.hasNext()){
								Dot dot = it.next();
								if(dot.valid)
									points.add(new Point(dot.posX, dot.posY));
							}
							if(classes.get(i).equals("signature"))
								recognizer.AddTemplate("your signature", points);
							else
								recognizer.AddTemplate(classes.get(i), points);
						}
						
						for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
							ArrayList<Point> points = new ArrayList<Point>();
							List<Dot> dotList = (new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
							Iterator<Dot> it = dotList.iterator();
							while(it.hasNext()){
								Dot dot = it.next();
								if(dot.valid)
									points.add(new Point(dot.posX, dot.posY));
							}
							if(classes.get(i).equals("signature"))
								recognizer.AddTemplate("your signature", points);
							else
								recognizer.AddTemplate(classes.get(i), points);
						}

						for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
							ArrayList<Point> points = new ArrayList<Point>();
							List<Dot> dotList = (new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
							Iterator<Dot> it = dotList.iterator();
							while(it.hasNext()){
								Dot dot = it.next();
								if(dot.valid)
									points.add(new Point(dot.posX, dot.posY));
							}
							if(classes.get(i).equals("signature"))
								recognizer.AddTemplate("your signature", points);
							else
								recognizer.AddTemplate(classes.get(i), points);
						}

						for(int j = 1; j <= uiNu; j++){
							for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
								ArrayList<Point> points = new ArrayList<Point>();
								List<Dot> dotList = (new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
								Iterator<Dot> it = dotList.iterator();
								while(it.hasNext()){
									Dot dot = it.next();
									if(dot.valid)
										points.add(new Point(dot.posX, dot.posY));
								}
								if(classes.get(i).equals("signature"))
									recognizer.AddTemplate("user"+j+" signature", points);
								else
									recognizer.AddTemplate(classes.get(i), points);
							}
						}
					}
					oneDollarClassifier = recognizer;
				}
				else
					if(ud){
						algorithm.oneDollar.Recognizer recognizer = new algorithm.oneDollar.Recognizer(new File(""));
						for(int i = 0; i < classes.size(); i++){

							for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
								ArrayList<Point> points = new ArrayList<Point>();
								List<Dot> dotList = (new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
								Iterator<Dot> it = dotList.iterator();
								while(it.hasNext()){
									Dot dot = it.next();
									if(dot.valid)
										points.add(new Point(dot.posX, dot.posY));
								}
								if(classes.get(i).equals("signature"))
									recognizer.AddTemplate("your signature", points);
								else
									recognizer.AddTemplate(classes.get(i), points);
							}

							for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
								ArrayList<Point> points = new ArrayList<Point>();
								List<Dot> dotList = (new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
								Iterator<Dot> it = dotList.iterator();
								while(it.hasNext()){
									Dot dot = it.next();
									if(dot.valid)
										points.add(new Point(dot.posX, dot.posY));
								}
								if(classes.get(i).equals("signature"))
									recognizer.AddTemplate("your signature", points);
								else
									recognizer.AddTemplate(classes.get(i), points);
							}
						}
						oneDollarClassifier = recognizer;
					}
					else
						if(ui){
							algorithm.oneDollar.Recognizer recognizer = new algorithm.oneDollar.Recognizer(new File(""));
							for(int i = 0; i < classes.size(); i++){

								for(int j = 1; j <= uiNu; j++){
									for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
										ArrayList<Point> points = new ArrayList<Point>();
										List<Dot> dotList = (new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt")).parse();
										Iterator<Dot> it = dotList.iterator();
										while(it.hasNext()){
											Dot dot = it.next();
											if(dot.valid)
												points.add(new Point(dot.posX, dot.posY));
										}
										if(classes.get(i).equals("signature"))
											recognizer.AddTemplate("user"+j+" signature", points);
										else
											recognizer.AddTemplate(classes.get(i), points);
									}
								}
							}
							oneDollarClassifier = recognizer;
						}
			}

			if(algosBox.getSelectedIndex() == 2){
				boolean resampling = ((LevenshteinSettings)levenshteinFrame.getContentPane()).resampling;
				levenshteinResampling = resampling;
				boolean rotation = ((LevenshteinSettings)levenshteinFrame.getContentPane()).rotation;
				levenshteinRotation = rotation;
				boolean rescaling = ((LevenshteinSettings)levenshteinFrame.getContentPane()).rescaling;
				levenshteinRescaling = rescaling;
				boolean translation = ((LevenshteinSettings)levenshteinFrame.getContentPane()).translation;
				levenshteinTranslation = translation;

				boolean withHoles = ((LevenshteinSettings)levenshteinFrame.getContentPane()).withHoles;
				levenshteinWithHoles = withHoles;
				int features = ((LevenshteinSettings)levenshteinFrame.getContentPane()).features;
				levenshteinFeatures = features;

				int normalisation = ((LevenshteinSettings)levenshteinFrame.getContentPane()).normalisation;
				levenshteinNormalisation = normalisation;
				int costs = ((LevenshteinSettings)levenshteinFrame.getContentPane()).costs;
				levenshteinCosts = costs;
				int knn = ((LevenshteinSettings)levenshteinFrame.getContentPane()).knn;
				levenshteinKnn = knn;

				String normString;
				if(normalisation == 0)
					normString = "no normalisation";
				else
					normString = "normalisation "+normalisation;

				trainingConsole += "\t-> Levenshtein's algorithm for "+ ((LevenshteinSettings)levenshteinFrame.getContentPane()).featuresCB.getSelectedItem().toString()+ ", "+ normString + ", with";
				if(!resampling && !rotation && !rescaling && !translation)
					trainingConsole += "out preprocessing";
				if(resampling)
					trainingConsole += " resampling";
				if(rotation)
					trainingConsole += " rotation";
				if(rescaling)
					trainingConsole += " rescaling";
				if(translation)
					trainingConsole += " translation";

				if(withHoles)
					trainingConsole += " and by taking holes into account";
				else
					trainingConsole += " and by not taking holes into account";

				if(ud && ui){
					Levenshtein lev;
					if(costs == 0)
						lev = new Levenshtein();
					else
						lev = new Levenshtein(true);
					for(int i = 0; i < classes.size(); i++){
						String classe = classes.get(i);
						if(classes.get(i).equals("signature"))
							classe = "your signature";
						for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
							if(resampling && withHoles)
								lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
							else
								if(!withHoles && features == 0)
									lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
								else{
									switch(features){
									case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									}
								}
						}

						for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
							if(resampling && withHoles)
								lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
							else
								if(!withHoles && features == 0)
									lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
								else{
									switch(features){
									case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
									}
								}
						}

						for(int j = 1; j <= uiNu; j++){
							if(classes.get(i).equals("signature"))
								classe = "user"+j+"signature";
							for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
								if(resampling && withHoles)
									lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
								else
									if(!withHoles && features == 0)
										lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
									else{
										switch(features){
										case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										}
									}
							}
						}
					}
					//lev.preCompute();
					levenshteinClassifier = lev;
				}
				else
					if(ud){
						Levenshtein lev;
						if(costs == 0)
							lev = new Levenshtein();
						else
							lev = new Levenshtein(true);
						for(int i = 0; i < classes.size(); i++){
							String classe = classes.get(i);
							if(classes.get(i).equals("signature"))
								classe = "your signature";
							for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
								if(resampling && withHoles)
									lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
								else
									if(!withHoles && features == 0)
										lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
									else{
										switch(features){
										case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										}
									}
							}

							for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
								if(resampling && withHoles)
									lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
								else
									if(!withHoles && features == 0)
										lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
									else{
										switch(features){
										case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/01/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										}
									}
							}
						}
						//lev.preCompute();
						levenshteinClassifier = lev;
					}
					else
						if(ui){
							Levenshtein lev;
							if(costs == 0)
								lev = new Levenshtein();
							else
								lev = new Levenshtein(true);
							for(int i = 0; i < classes.size(); i++){
								String classe = classes.get(i);

								for(int j = 1; j <= uiNu; j++){
									if(classes.get(i).equals("signature"))
										classe = "user"+j+"signature";
									for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
										if(resampling && withHoles)
											lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
										else
											if(!withHoles && features == 0)
												lev.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
											else{
												switch(features){
												case 0: lev.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 1: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 2: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 3: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 4: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 5: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 6: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												case 7: lev.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
												}
											}
									}
								}
							}
							//lev.preCompute();
							levenshteinClassifier = lev;
						}
			}

			if(algosBox.getSelectedIndex() == 3){
				boolean resampling = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).resampling;
				stochasticLevenshteinResampling = resampling;
				boolean rotation = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).rotation;
				stochasticLevenshteinRotation = rotation;
				boolean rescaling = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).rescaling;
				stochasticLevenshteinRescaling = rescaling;
				boolean translation = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).translation;
				stochasticLevenshteinTranslation = translation;

				boolean withHoles = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).withHoles;
				stochasticLevenshteinWithHoles = withHoles;
				int features = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).features;
				stochasticLevenshteinFeatures = features;

				int normalisation = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).normalisation;
				stochasticLevenshteinNormalisation = normalisation;
				int knn = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).knn;
				stochasticLevenshteinKnn = knn;

				String normString;
				if(normalisation == 0)
					normString = "no normalisation";
				else
					normString = "normalisation "+normalisation;

				trainingConsole += "\t-> Stochastic Levenshtein's algorithm for "+ ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).featuresCB.getSelectedItem().toString()+ ", "+ normString + ", with";
				if(!resampling && !rotation && !rescaling && !translation)
					trainingConsole += "out preprocessing";
				if(resampling)
					trainingConsole += " resampling";
				if(rotation)
					trainingConsole += " rotation";
				if(rescaling)
					trainingConsole += " rescaling";
				if(translation)
					trainingConsole += " translation";

				if(withHoles)
					trainingConsole += " and by taking holes into account";
				else
					trainingConsole += " and by not taking holes into account";

				if(ud && ui){
					String noTemplateClasses = "";
					for(int i = 0; i < classes.size(); i++)
						if(DataRecorder.numberElements("records/-1/"+classes.get(i))+DataRecorder.numberElements("records/0/"+classes.get(i)) == 0)
							noTemplateClasses += classes.get(i)+" ";
					if(uiNu == 1 && uiNte == 1 && noTemplateClasses.equals("")){
						JOptionPane.showMessageDialog(mainFrame, "Stochastic Levenshtein needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
						console.setText(console.getText()+"\nPlease add example for classes:\n"+noTemplateClasses);
						return;
					}
					else{
						String[] alphabet = {"0","1","2","3","4","5","6","7"};
						String[] alphabet2 = {"0","1","2","3","4","5","6","7","x"};
						String[] alphabet3 = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y"};
						ConditionalRecognizer cr;
						if(features == 7)
							cr = new ConditionalRecognizer(alphabet3);
						else if(withHoles)
							cr = new ConditionalRecognizer(alphabet2);
						else
							cr = new ConditionalRecognizer(alphabet);
						for(int i = 0; i < classes.size(); i++){
							String classe = classes.get(i);
							if(classes.get(i).equals("signature"))
								classe = "your signature";
							for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
								if(resampling && withHoles)
									cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
								else
									if(!withHoles && features == 0)
										cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
									else{
										switch(features){
										case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										}
									}
							}

							for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
								if(resampling && withHoles)
									cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
								else
									if(!withHoles && features == 0)
										cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
									else{
										switch(features){
										case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
										}
									}
							}

							for(int j = 1; j <= uiNu; j++){
								if(classes.get(i).equals("signature"))
									classe = "user"+j+"signature";
								for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
									if(resampling && withHoles)
										cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
									else
										if(!withHoles && features == 0)
											cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
										else{
											switch(features){
											case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											}
										}
								}
							}
						}
						cr.compile();
						stochasticLevenshteinClassifier = cr;
					}
				}
				else
					if(ud){
						if(udNte < 2){
							JOptionPane.showMessageDialog(mainFrame, "Stochastic Levenshtein needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
							console.setText(console.getText()+"\nPlease change training settings.");
							return;
						}
						String noEnoughTemplateClasses = "";
						for(int i = 0; i < classes.size(); i++)
							if(DataRecorder.numberElements("records/-1/"+classes.get(i))+DataRecorder.numberElements("records/0/"+classes.get(i)) < 2)
								noEnoughTemplateClasses += classes.get(i)+ " ";
						if(!noEnoughTemplateClasses.equals("")){
							JOptionPane.showMessageDialog(mainFrame, "Stochastic Levenshtein needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
							console.setText(console.getText()+"\nPlease add example for classes:\n"+noEnoughTemplateClasses);
							return;
						}
						else{
							String[] alphabet = {"0","1","2","3","4","5","6","7"};
							String[] alphabet2 = {"0","1","2","3","4","5","6","7","x"};
							String[] alphabet3 = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y"};
							ConditionalRecognizer cr;
							if(features == 7)
								cr = new ConditionalRecognizer(alphabet3);
							else if(withHoles)
								cr = new ConditionalRecognizer(alphabet2);
							else
								cr = new ConditionalRecognizer(alphabet);
							for(int i = 0; i < classes.size(); i++){
								String classe = classes.get(i);
								if(classes.get(i).equals("signature"))
									classe = "your signature";
								for(int k = 1; k <= Math.min(udNte-DataRecorder.numberElements("records/0/"+classes.get(i)),DataRecorder.numberElements("records/-1/"+classes.get(i))); k++){
									if(resampling && withHoles)
										cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
									else
										if(!withHoles && features == 0)
											cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
										else{
											switch(features){
											case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/-1/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											}
										}
								}

								for(int k = 1; k <= Math.min(udNte,DataRecorder.numberElements("records/0/"+classes.get(i))); k++){
									if(resampling && withHoles)
										cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
									else
										if(!withHoles && features == 0)
											cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
										else{
											switch(features){
											case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/0/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
											}
										}
								}
							}
							cr.compile();
							stochasticLevenshteinClassifier = cr;
						}
					}
					else
						if(ui){
							if(uiNu == 1 && uiNte == 1){
								JOptionPane.showMessageDialog(mainFrame, "Stochastic Levenshtein needs 2 training examples by class at minimum.","Training issue",JOptionPane.ERROR_MESSAGE);
								return;
							}
							else{
								String[] alphabet = {"0","1","2","3","4","5","6","7"};
								String[] alphabet2 = {"0","1","2","3","4","5","6","7","x"};
								String[] alphabet3 = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y"};
								ConditionalRecognizer cr;
								if(features == 7)
									cr = new ConditionalRecognizer(alphabet3);
								else if(withHoles)
									cr = new ConditionalRecognizer(alphabet2);
								else
									cr = new ConditionalRecognizer(alphabet);
								for(int i = 0; i < classes.size(); i++){
									String classe = classes.get(i);

									for(int j = 1; j <= uiNu; j++){
										if(classes.get(i).equals("signature"))
											classe = "user"+j+"signature";
										for(int k = 1; k <= Math.min(uiNte,DataRecorder.numberElements("records/"+j+"/"+classes.get(i))); k++){
											if(resampling && withHoles)
												cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse()));
											else
												if(!withHoles && features == 0)
													cr.addTemplate(classe, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),resampling, rotation, rescaling, translation));
												else{
													switch(features){
													case 0: cr.addTemplate(classe, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 1: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 2: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 3: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 4: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 5: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 6: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													case 7: cr.addTemplate(classe, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+j+"/"+classes.get(i)+"/"+classes.get(i)+k+".txt").parse(),withHoles));break;
													}
												}
										}
									}
								}
								cr.compile();
								stochasticLevenshteinClassifier = cr;
							}
						}
			}
			if(console.getText().equals("\n\n\n\n\n\n"))
				console.setText("");
			console.append("\n"+trainingConsole);
		}
	}

	public void recognize(){
		tda.saveDatas();
		List<Dot> dotList = (new RecordParser("records/toRecognize.txt")).parse();
		if(dotList.size()==0){
			JOptionPane.showMessageDialog(mainFrame, "No example drawn for recognition.","Recognition issue",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(algosBox.getSelectedIndex() == 0){
			if(rubineClassifier == null){
				JOptionPane.showMessageDialog(mainFrame, "Rubine classifier not trained.","Recognition issue",JOptionPane.ERROR_MESSAGE);
			}
			else{
				Gesture toRecognize = new Gesture();
				toRecognize.addGesture(dotList, rubineResampling, rubineRotation, rubineRescaling, rubineTranslation);
				String result = rubineClassifier.classify(toRecognize).getName();
				console.append("\n\nClass found: "+result);
			}
		}

		if(algosBox.getSelectedIndex() == 1){
			if(oneDollarClassifier == null){
				JOptionPane.showMessageDialog(mainFrame, "OneDollar classifier not trained.","Recognition issue",JOptionPane.ERROR_MESSAGE);
			}
			else{
				ArrayList<Point> points = new ArrayList<Point>();
				Iterator<Dot> it = dotList.iterator();
				while(it.hasNext()){
					Dot dot = it.next();
					if(dot.valid)
						points.add(new Point(dot.posX, dot.posY));
				}
				oneDollarKnn = ((OneDollarSettings)oneDollarFrame.getContentPane()).knn;
				String result = oneDollarClassifier.RecognizeKnn(points, oneDollarKnn);
				console.append("\n\nClass found for knn="+oneDollarKnn+": "+result);
			}
		}

		if(algosBox.getSelectedIndex() == 2){
			if(levenshteinClassifier == null){
				JOptionPane.showMessageDialog(mainFrame, "Levenshtein classifier not trained.","Recognition issue",JOptionPane.ERROR_MESSAGE);
			}
			else{
				String result = "";
				levenshteinKnn = ((LevenshteinSettings)levenshteinFrame.getContentPane()).knn;
				levenshteinCosts = ((LevenshteinSettings)levenshteinFrame.getContentPane()).costs;
				if(levenshteinResampling && levenshteinWithHoles)
					result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformWithResampleAndHoles(dotList),levenshteinNormalisation,levenshteinKnn);
				else
					if(!levenshteinWithHoles && levenshteinFeatures == 0)
						result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformWithProcessing(dotList,levenshteinResampling, levenshteinRotation, levenshteinRescaling, levenshteinTranslation),levenshteinNormalisation,levenshteinKnn);
					else{
						switch(levenshteinFeatures){
						case 0: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transform(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 1: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformPressure(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 2: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformAngle(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 3: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformOrientation(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 4: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformPressureDifference(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 5: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformAngleDifference(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 6: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformOrientationDifference(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						case 7: result = levenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformDirectionAndPressure(dotList,levenshteinWithHoles),levenshteinNormalisation,levenshteinKnn);break;
						}
					}
				String costsString;
				if(levenshteinCosts == 0)
					costsString = "01 substitution costs";
				else
					costsString = "01234 substitution costs";
				console.append("\n\nClass found for knn="+levenshteinKnn+" and "+costsString+": "+result);
			}
		}

		if(algosBox.getSelectedIndex() == 3){
			if(stochasticLevenshteinClassifier == null){
				JOptionPane.showMessageDialog(mainFrame, "Stochastic Levenshtein classifier not trained.","Recognition issue",JOptionPane.ERROR_MESSAGE);
			}
			else{
				String result = "";
				stochasticLevenshteinKnn = ((StochasticLevenshteinSettings)stochasticLevenshteinFrame.getContentPane()).knn;
				if(stochasticLevenshteinResampling && stochasticLevenshteinWithHoles)
					result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformWithResampleAndHoles(dotList),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);
				else
					if(!stochasticLevenshteinWithHoles && stochasticLevenshteinFeatures == 0)
						result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformWithProcessing(dotList,stochasticLevenshteinResampling, stochasticLevenshteinRotation, stochasticLevenshteinRescaling, stochasticLevenshteinTranslation),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);
					else{
						switch(stochasticLevenshteinFeatures){
						case 0: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transform(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 1: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformPressure(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 2: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformAngle(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 3: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformOrientation(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 4: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformPressureDifference(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 5: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformAngleDifference(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 6: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformOrientationDifference(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						case 7: result = stochasticLevenshteinClassifier.recognize(algorithm.levenshtein.Utils.transformDirectionAndPressure(dotList,stochasticLevenshteinWithHoles),stochasticLevenshteinNormalisation,stochasticLevenshteinKnn);break;
						}
					}
				console.append("\n\nClass found for knn="+stochasticLevenshteinKnn+": "+result);
			}
		}
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
			System.out.println("button RecognizerSave pressed");
			recognize();
		}

	}

	private class EraseAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 170824460915017677L;

		public void actionPerformed(ActionEvent e){
			System.out.println("button Recognizererase pressed");
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
			System.out.println("button recognizerPrevious pressed");	
			Template curTemplate = dr.previous();
			if(curTemplate==null) return;
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
