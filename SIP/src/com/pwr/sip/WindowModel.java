package com.pwr.sip;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WindowModel implements ActionListener
{
	private ButtonPanel buttonPanel; 
	
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	
	public JFrame frame;
	private JTextField input;

	HopfieldNetwork network;
	
	public WindowModel()
	{
		// Create the neural network.
		buttonPanel = new ButtonPanel(this);
		network = new HopfieldNetwork(buttonPanel.getGRID_SIZE());
		createAndShowGUI();
	}
	
	public JPanel CreateContentPanel(int GRID_SIZE )
	{
		JPanel TotalGUI = new JPanel(new BorderLayout(10,10));

		JPanel fieldButtons = new JPanel(new GridLayout(buttonPanel.getGRID_SIZE(),buttonPanel.getGRID_SIZE()));
		JPanel action_buttons = new JPanel(new GridLayout(7,1));
		TotalGUI.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		input = new JTextField("Current network size: "+buttonPanel.getGRID_SIZE());
		input.setEditable(false);
		input.setHorizontalAlignment(JTextField.CENTER);

		for(int i = 0; i < buttonPanel.getGRID_SIZE(); i++)
			for(int j = 0; j < buttonPanel.getGRID_SIZE(); j++)
				fieldButtons.add(buttonPanel.getPixelButton((buttonPanel.getGRID_SIZE()*i)+j));

			
		for(int i = 0; i < buttonPanel.getButtonsList().size(); i++)
			action_buttons.add(buttonPanel.getButton(i));

        TotalGUI.add(input, BorderLayout.NORTH);
        TotalGUI.add(fieldButtons, BorderLayout.CENTER);
        TotalGUI.add(action_buttons, BorderLayout.WEST);

        buttonPanel.ifDefine = false;
        return TotalGUI;
	}
	
    private void createAndShowGUI()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Hoppfield Network");
        Toolkit tk = frame.getToolkit();
        Image icon = tk.getImage( "icon.png"); // load application icon
        frame.setIconImage( icon );
        // Set the content pane.
        frame.setContentPane(CreateContentPanel(20));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }
    
    private void refreshGUI()
    {
		// Resize the neural network.
    	buttonPanel.refreshButtonPanel(this);
    	frame.setContentPane(CreateContentPanel(buttonPanel.getGRID_SIZE()));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();	
		
		for(int i = 0; i < buttonPanel.getGRID_SIZE()*buttonPanel.getGRID_SIZE(); i++)
			if(source == buttonPanel.getPixelButton(i))
		{
			buttonPanel.colorPixelButton(i);
			break;
		}
		
		if(source == buttonPanel.getButton(0)) // TRAIN
		{
			boolean actionResult = false;
			BufferedImage images[] = WindowHelper.loadImage(frame, "Choose images for learning process:");
			if(images.length != 0)
			{
				if(WindowHelper.checkImagesSize(images))
				{
					ArrayList<boolean[]> arrayedImages = WindowHelper.loadImagesIntoBooleanList(images);
					LearningWindow learningWindow = new LearningWindow(frame , "Learning parameters", "");
					network = new HopfieldNetwork(arrayedImages.get(0).length);
					actionResult = WindowHelper.processTraining(learningWindow.getMethod(), network, arrayedImages, null);
				}
			}
			else
			{
				LearningWindow learningWindow = new LearningWindow(frame , "Learning parameters", "");
				network = new HopfieldNetwork(buttonPanel.getGRID_SIZE()*buttonPanel.getGRID_SIZE());
				actionResult = WindowHelper.processTraining(learningWindow.getMethod(), network, null, buttonPanel);
			}
			if(actionResult == true)
				buttonPanel.setPanelMessage("Learning process completed");
			else
				buttonPanel.setPanelMessage("Learning process interrupted");
			input.setText(buttonPanel.getPanelMessage());
			WindowHelper.weightMatrixToFile(network.getMatrix(), "MacierzWag_"+(int)Math.sqrt(network.getMatrix().getCols())+"x"+(int)Math.sqrt(network.getMatrix().getCols())+"px.txt");
			//refreshGUI(); 					// We do not refresh as it can be uncomfortable if we would like to use the same pattern to train more than once

		}
		else if(source == buttonPanel.getButton(1)) // RECOGNIZE
		{
			boolean[] result = WindowHelper.processRecognition(buttonPanel, network);
			buttonPanel.setPixelsArray(BiPolarUtil.bipolar_2_2ddouble(result));
			buttonPanel.ifDefine = false;
			refreshGUI();
			
			input.setText(buttonPanel.getPanelMessage());
		}
		if(source == buttonPanel.getButton(2)) // LOAD IMAGE
		{
			BufferedImage images[] = WindowHelper.loadImage(frame, "Choose an image for recognition process:");
			if(images != null && images.length == 1)
			{
				buttonPanel.loadImageIntoPixelsArray(images[0]);
				buttonPanel.setPanelMessage("Image loaded");
				refreshGUI();
				input.setText(buttonPanel.getPanelMessage());
			}
			else if(images != null)
			{
				JOptionPane.showMessageDialog(frame, "Loaded too many images!!! (Select only one you want to read)");
			}
		}
		else if(source == buttonPanel.getButton(3)) // SAVE IMAGE
		{
			WindowHelper.saveImage(frame, buttonPanel);						
			refreshGUI();
			input.setText(buttonPanel.getPanelMessage());
		}
		else if(source == buttonPanel.getButton(4)) // CLEAR
		{
			buttonPanel.ifDefine = true;
			refreshGUI();
		}
		else if(source == buttonPanel.getButton(5))
		{			
			buttonPanel.addToGRID_SIZE(5);			
			network = new HopfieldNetwork(buttonPanel.getGRID_SIZE()*buttonPanel.getGRID_SIZE());
			refreshGUI();
			input.setText(buttonPanel.getPanelMessage());
		}
		else if(source == buttonPanel.getButton(6))
		{

			buttonPanel.substractFromGRID_SIZE(5);			
			network = new HopfieldNetwork(buttonPanel.getGRID_SIZE()*buttonPanel.getGRID_SIZE());
			refreshGUI();
			input.setText(buttonPanel.getPanelMessage());
		}
	}
	
}
