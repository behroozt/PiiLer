package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class Convertor extends JFrame{

	String type;
	String[] splitor = {"comma","tab","space","semicolon","dash"};
	JComboBox separatorCombo;
	JComboBox arraysCombo;
	JFrame mainFrame;
	JPanel northPanel, southPanel, containerPanel, centerPanel;
	JLabel inputLabel, outputLabel, separatorLabel, typeLabel, dictionaryLabel, processLabel ;
	JTextField inputText, outputText, dictionaryText;
	JButton loadInput, loadOutput, loadDictionary, convertButton, exitButton;
	GridBagConstraints gridConstraints = new GridBagConstraints();
	static JProgressBar progressBar;
	final ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
	JFileChooser fileSelector = new JFileChooser();
	static String openedDirectory = System.getProperty("user.home");
	String splitBy = ",";
	int rowCounter;
	File inputFile, outputFile, dictionaryFile;
	String line;
	HashMap<String, String[]> cpgMap = new HashMap<String, String[]>();
	int maximumProgress = 487000;
	
	public Convertor(){
		initiateForm();
		Object[] options = { "Close"};
		int result = JOptionPane.showConfirmDialog(null, containerPanel, "Please choose ...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
		
	}
	
	private void initiateForm() {
		mainFrame = new JFrame();
		mainFrame.setSize(700, 400);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPos = (dim.width / 2) - (mainFrame.getWidth() / 2);
		int yPos = (dim.height / 2) - (mainFrame.getHeight() / 2);
		mainFrame.setLocation(xPos,yPos);
		mainFrame.setAlwaysOnTop(true);
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		
		mainFrame.setTitle("Convert raw files to PiiL format");
		northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.green);
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setBackground(Color.CYAN);
		inputLabel = new JLabel("Input file:");
		outputLabel = new JLabel("Output file:");
		separatorLabel = new JLabel("Columns are separated by:");
		typeLabel = new JLabel("The data is from:");
		dictionaryLabel = new JLabel("The dictionary file:");
		loadInput = new JButton("Load");
		loadInput.setPreferredSize(new Dimension(100,32));
		loadOutput = new JButton("Change");
		loadOutput.setPreferredSize(new Dimension(100,32));
		loadDictionary = new JButton("Change");
		loadDictionary.setPreferredSize(new Dimension(100,32));
		inputText = new JTextField();
		inputText.setPreferredSize(new Dimension(400,20));
		outputText = new JTextField();
		outputText.setPreferredSize(new Dimension(400,20));
		dictionaryText = new JTextField();
		dictionaryText.setPreferredSize(new Dimension(400,20));
		
		convertButton = new JButton("Convert");
		convertButton.setPreferredSize(new Dimension(100,32));
		exitButton = new JButton("Close");
		exitButton.setPreferredSize(new Dimension(100,32));
		progressBar = new JProgressBar(0, 100);
		progressBar.setEnabled(false);
        progressBar.setValue(0);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(400,20));
        processLabel = new JLabel();
        processLabel.setPreferredSize(new Dimension(400,20));
        ListenForButton buttonclick = new ListenForButton();
        loadInput.addActionListener(buttonclick);
        loadOutput.addActionListener(buttonclick);
        loadDictionary.addActionListener(buttonclick);
        convertButton.addActionListener(buttonclick);
        arraysCombo = new JComboBox();
        arraysCombo.addItem(new ComboItem("Infinium HumanMethylation27K", "27k_annotation_noIntergenicSite.csv"));
        arraysCombo.addItem(new ComboItem("Infinium HumanMethylation450K", "450k_annotation_noIntergenicSite.csv"));
        arraysCombo.addItem(new ComboItem("Infinium MethylationEPIC", "epic_annotation_noIntergenicSite.csv"));
        arraysCombo.addItem(new ComboItem("WGBS",""));
        separatorCombo = new JComboBox();
        separatorCombo.addItem(new ComboItem("comma",","));
        separatorCombo.addItem(new ComboItem("tab","\t"));
        separatorCombo.addItem(new ComboItem("space","\\s+"));
        separatorCombo.addItem(new ComboItem("semicolon",";"));
        separatorCombo.addItem(new ComboItem("dash","-"));
        arraysCombo.setSelectedIndex(1);
        separatorCombo.setPreferredSize(arraysCombo.getPreferredSize());
        
        dictionaryText.setText(System.getProperty("user.dir") + "/Dictionaries/" + ((ComboItem) arraysCombo.getSelectedItem()).getValue());
        dictionaryFile = new File(dictionaryText.getText());
        ListenForCombo lForCombo = new ListenForCombo();
        separatorCombo.addItemListener(lForCombo);
        arraysCombo.addItemListener(lForCombo);
        
		
		
//		gridConstraints.insets = new Insets(15,1,1,1);
		addComp(northPanel,inputLabel,0,0,1,1,GridBagConstraints.EAST, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(10,1,1,1);
		addComp(northPanel,inputText,1,0,2,1,GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(12,5,5,5);
		addComp(northPanel,loadInput,3,0,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(8,10,1,1);
		addComp(northPanel,outputLabel,0,1,1,1,GridBagConstraints.EAST, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(10,1,1,1);
		addComp(northPanel,outputText,1,1,2,1,GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(12,5,5,5);
		addComp(northPanel,loadOutput,3,1,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		gridConstraints.insets = new Insets(8,10,1,1);
		addComp(northPanel,dictionaryLabel,0,2,1,1,GridBagConstraints.EAST, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(10,1,1,1);
		addComp(northPanel,dictionaryText,1,2,2,1,GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		gridConstraints.insets = new Insets(12,5,5,5);
		addComp(northPanel,loadDictionary,3,2,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		addComp(northPanel,separatorLabel,0,3,1,1,GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(northPanel,separatorCombo,1,3,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		addComp(northPanel,typeLabel,0,4,1,1,GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(northPanel,arraysCombo,1,4,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		addComp(northPanel,progressBar,1,5,2,1,GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(northPanel,convertButton,3,5,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		addComp(northPanel,processLabel,1,6,2,1,GridBagConstraints.WEST, GridBagConstraints.NONE);
		
		containerPanel.add(northPanel, BorderLayout.NORTH);
		
	}

	public static void main(String[] args) {
		new Convertor();
		
	}
	
	private class ListenForCombo implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent ice) {
			
			if (ice.getSource() == separatorCombo){
				splitBy = ((ComboItem) separatorCombo.getSelectedItem()).getValue();
			}
			else if (ice.getSource() == arraysCombo){
				dictionaryText.setText(System.getProperty("user.dir") + "/" + ((ComboItem) arraysCombo.getSelectedItem()).getValue());
				dictionaryFile = new File(dictionaryText.getText());
				int arrayType = arraysCombo.getSelectedIndex();
				switch(arrayType){
					case 0 : maximumProgress = 28000;
					break;
					case 1: maximumProgress = 487000;
					break;
					case 2: maximumProgress = 870000;
					break;
					case 3: maximumProgress = 2000000;
					break;
					default : maximumProgress = 487000;
					break;
				}
			}
			
		}
		
	}
	
	private class ListenForButton implements ActionListener{

		File directory = new File(openedDirectory);
		
		public void actionPerformed(ActionEvent bce) {
			if (bce.getSource() == loadInput){
				File file;
				String outfileName;
				fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileSelector.setCurrentDirectory(directory);
				int returnVal = fileSelector.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) { 
        			file = fileSelector.getSelectedFile();
        			openedDirectory = fileSelector.getSelectedFile().getAbsolutePath();
        			inputText.setText(file.toString());
        			inputFile = new File(inputText.getText());
        			if (file.toString().lastIndexOf('.') == -1){
        				outfileName = file.toString() + "_PiiL.csv";
        			}
        			else {
        				outfileName = file.toString().substring(0,file.toString().lastIndexOf('.'));
        				outfileName += "_PiiL" + file.toString().substring(file.toString().lastIndexOf('.'),file.toString().length());
        			}
        			
        			outputText.setText(outfileName);
        			outputFile = new File(outputText.getText());
				}
			}
			else if (bce.getSource() == loadOutput){
				if (fileSelector.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					outputText.setText(fileSelector.getSelectedFile().toString());
					outputFile = new File(outputText.getText());
				}
			}
			else if (bce.getSource() == loadDictionary){
				fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileSelector.setCurrentDirectory(directory);
				int returnVal = fileSelector.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) { 
					dictionaryText.setText(fileSelector.getSelectedFile().toString());
					dictionaryFile = new File(dictionaryText.getText());
				}
			}
			else if (bce.getSource() == convertButton){
				if (inputText.getText().isEmpty() || outputText.getText().isEmpty() || dictionaryText.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please make sure all the fields are selected!","Warning", 0, icon);
				}
				else {
					Task task = new Task();
					task.execute();
				}
				
			}
			
		}
		
	}
	
	class Task extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			int progress = 0;
			progressBar.setVisible(true);
            progressBar.setEnabled(true);
            progressBar.setStringPainted(true);
            setProgress(0);
            String[] elements;
            BufferedReader br = new BufferedReader(new FileReader(dictionaryFile));
            rowCounter = 0;
            progressBar.setMaximum(maximumProgress);
            processLabel.setText("Processing the annotation file ... ");
            
            while ((line = br.readLine()) != null){
            	rowCounter++;
            	String[] info = new String[3];
				progressBar.setValue(rowCounter);
				elements = line.split(","); // elements {cpgID, position, geneName, region}	
				info[0] = elements[1]; // position
				info[1] = elements[2]; // geneName
				info[2] = elements[3]; // region
				cpgMap.put(elements[0], info);
            }
            
			return null;
		}
		public void done() {
			progressBar.setValue(progressBar.getMaximum());
            processLabel.setText("Done!");
            ConvertTask convert = new ConvertTask();
			convert.execute();
        }
		
	}
	
	class ConvertTask extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			
            setProgress(0);
            outputFile.createNewFile();
            PrintWriter outFile = null;
			try {
				outFile = new PrintWriter(outputFile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
            
            String[] elements;
            rowCounter = 0;
            progressBar.setMaximum(maximumProgress);
            processLabel.setText("Converting to PiiL format ... ");
            elements = br.readLine().split(splitBy);
            String header = "";
            for (int i = 0; i < elements.length; i ++){
            	header += elements[i] + ",";
            }
            outFile.println(header.substring(0,header.length()-1));
            String siteID, convertedLine = null;
            
            while ((line = br.readLine()) != null){
            	rowCounter++;
				progressBar.setValue(rowCounter);
				elements = line.split(splitBy);
				siteID = elements[0];
				
				if (cpgMap.containsKey(siteID)){
					
					convertedLine = cpgMap.get(siteID)[1] + "_" + siteID + "_" +
							cpgMap.get(siteID)[0] + "_" + cpgMap.get(siteID)[2] + ",";
					for (int i = 1; i < elements.length; i ++){
						convertedLine += elements[i] + ",";
					}
					outFile.println(convertedLine.substring(0, convertedLine.length()-1));					
				}
            }
            outFile.close();
            progressBar.setValue(progressBar.getMaximum());
            
			return null;
		}
		public void done() {
            processLabel.setText("Done!");
            cpgMap.clear();
        }
		
	}
	
	private class ComboItem{
	    private String key;
	    private String value;

	    public ComboItem(String key, String value)
	    {
	        this.key = key;
	        this.value = value;
	    }

	    @Override
	    public String toString()
	    {
	        return key;
	    }

	    public String getKey()
	    {
	        return key;
	    }

	    public String getValue()
	    {
	        return value;
	    }
	}
	
	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place, int stretch){
		
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = 1;
		gridConstraints.weighty = 1;
		gridConstraints.insets = new Insets(5,5,5,1);
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;
		
		thePanel.add(comp, gridConstraints);
		
	}
	
	
}
