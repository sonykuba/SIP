package com.pwr.sip;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WindowHelper {
	
	public static BufferedImage loadImage(JFrame frame, JFileChooser fc)
	{
		File imageFile;
		int returnVal = fc.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
        	imageFile = fc.getSelectedFile();	        
        } else {
            JOptionPane.showMessageDialog(frame, "Error when trying to open a file!");
            imageFile = new File("droga.bmp");
        }
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return image;
	}
	
	public static void saveImage(JFrame frame, JFileChooser fc, ButtonPanel buttonPanel)
	{
		int GRID_SIZE = buttonPanel.getGRID_SIZE();
		File imageFile;
		String imagePath;
		int returnVal = fc.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
        	imageFile = fc.getSelectedFile();	        	
        	imagePath = imageFile.getPath();
        	if(!imagePath.contains(".bmp"))
        		imagePath += ".bmp";
        	
        	BufferedImage image = new BufferedImage(GRID_SIZE, GRID_SIZE,   
		               BufferedImage.TYPE_BYTE_BINARY);  
		      Graphics g = image.getGraphics();  
		      for(int i = 0; i < GRID_SIZE; i++)
		    	  for(int j = 0; j < GRID_SIZE; j++)
		    	  {
		    		  g.setColor(buttonPanel.getPixelFromArray(j,i)==-1.0?Color.WHITE:Color.BLACK);
		    		  g.fillRect(i, j, 1, 1);	    		  
		    	  }
		      try {
				ImageIO.write(image, "BMP", new File(imagePath));
				buttonPanel.setPanelMessage("Image saved successfuly");
				JOptionPane.showMessageDialog(frame, buttonPanel.getPanelMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				buttonPanel.setPanelMessage("Error when saving image...");
				JOptionPane.showMessageDialog(frame, buttonPanel.getPanelMessage());
				e1.printStackTrace();
			}  
		      g.dispose();  
        	
        } else {
        	buttonPanel.setPanelMessage("Image not saved");
			JOptionPane.showMessageDialog(frame, buttonPanel.getPanelMessage());
        }
        
	}
	
	public static void processTraining(int method, int iterations, ButtonPanel buttonPanel, HopfieldNetwork network)
	{
		if(method == 0)
			method = 1; // Delta method from default
		if(iterations == 0)
			iterations = 10; // 20 iterations of learning process from default
		
		// This pattern will be trained
		final boolean[] pattern = BiPolarUtil.double2d_2_1dbipolar(buttonPanel.getPixelsArray());
		// train the neural network with the pattern
		System.out.println("Training Hopfield network with: "
				+ MatrixMath.formatBoolean(pattern));
		
		// choose patern for learning
		/*for(int i=0;i<iterations;i++){
			if(method == 1)
				network.learnDelta(pattern);
			else
				network.learnPseudoInversion(pattern);
		}*/
		buttonPanel.setPanelMessage("Learning process completed");
	}
	
	public static boolean[] processRecognition(ButtonPanel buttonPanel, HopfieldNetwork network)
	{
		// This pattern will be presented
		final boolean[] pattern1 = BiPolarUtil.double2d_2_1dbipolar(buttonPanel.getPixelsArray());

		boolean[] result;
		result = network.present(pattern1);
		System.out.println("Presenting pattern:" + MatrixMath.formatBoolean(pattern1)
				+ ", and got " + MatrixMath.formatBoolean(result));
		buttonPanel.setPanelMessage("Recognition completed");
		return result;
	}
}
