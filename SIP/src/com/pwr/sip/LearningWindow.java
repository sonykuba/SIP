package com.pwr.sip;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LearningWindow extends JDialog implements ActionListener, ListSelectionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JDialog jDialog;
	String[] options = {"10","20", "30", "40", "50","100","150","200","500","1000"};
	JRadioButton deltaMethod = new JRadioButton("Delta Method");
	JRadioButton pseudoInversionMethod = new JRadioButton("Pseudoinversion Method");
	JTextField methodsField = new JTextField("Choose one learning method:");
	JTextField iterationsField = new JTextField("Choose number of iterations:");
	JList listField = new JList();
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JScrollPane listScroller;
	
	private int method;
	private int iterations;
	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	
	public LearningWindow(JFrame parent, String title, String message)
	{
		
		super(parent, title, true);
		Dimension parentSize = parent.getSize(); 
		Point p = parent.getLocation(); 
		setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		CreateAndShowGUI(parent, title);
		
	}

	private void CreateAndShowGUI(JFrame parent, String title) {
		// TODO Auto-generated method stub		
		jDialog = new JDialog(parent, title);  // Creates dialog
		jDialog.setModal(true);           // Means it will wait
		jDialog.add(CreatePanel());                   // Add your panel
		jDialog.pack();        // Set size (probably want this relating to your panel
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jDialog.setLocation(dim.width/2 - jDialog.getWidth()/2, dim.height/2 - jDialog.getHeight()/2);
		jDialog.setVisible(true);
		jDialog.setResizable(false);
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void exit()
	{
		jDialog.dispose();
	}
	
	private JPanel CreatePanel()
	{
		JPanel TotalGUI = new JPanel(new GridLayout(2,2));
		
		JPanel radioButtonPanel = new JPanel(new GridLayout(2,1));
		
		deltaMethod.addActionListener(this);
		deltaMethod.setSelected(true);
		pseudoInversionMethod.addActionListener(this);
		radioButtonPanel.add(deltaMethod);
		radioButtonPanel.add(pseudoInversionMethod);
		
		listField = new JList(prepareListElements());
		listField.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listField.setLayoutOrientation(JList.VERTICAL);
		listField.addListSelectionListener(this);
		listField.setSelectedIndex(0);
		listScroller = new JScrollPane(listField);
		listScroller.setPreferredSize(new Dimension(250, 85));
		
		methodsField.setEditable(false);
		iterationsField.setEditable(false);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		TotalGUI.add(methodsField);
		TotalGUI.add(radioButtonPanel);
		//TotalGUI.add(iterationsField);
		//TotalGUI.add(listScroller);
		TotalGUI.add(okButton);
		TotalGUI.add(cancelButton);
		
		return TotalGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if(source == deltaMethod)
		{
			method = 1;
			if(pseudoInversionMethod.isSelected())
				pseudoInversionMethod.setSelected(false);
		}
		else if (source == pseudoInversionMethod) 
		{
			method = 2;
			if(deltaMethod.isSelected())
				deltaMethod.setSelected(false);
		}
		else if (source == okButton)
			exit();
		else if (source == cancelButton)
			exit();
		
	}
	
	private ListModel prepareListElements()
	{
		
		ListModel listModel = new DefaultListModel();
		for (String string : options) {			
			((DefaultListModel) listModel).addElement(string);
		}	
		return listModel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

	        if (listField.getSelectedIndex() == -1) {

	        } else {
	        	iterations = Integer.parseInt(options[listField.getSelectedIndex()]);
	        }
	    }
		
	}
	
}
