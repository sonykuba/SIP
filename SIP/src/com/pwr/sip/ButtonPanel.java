package com.pwr.sip;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;


public class ButtonPanel{
	
	private int GRID_SIZE = 20;
	private String panelMessage = "Current Network size: "+GRID_SIZE;
	private List<String> buttonsNames = Arrays.asList("TRAIN", "RECOGNITION", "LOAD IMAGE", "SAVE IMAGE","CLEAR", "+", "-");
	private List<JButton> actionButtonsList = new ArrayList<JButton>();
	private JButton[] pixelsButtons;
	private double[][] pixelsArray;

	public boolean ifDefine = true;
	
	public ButtonPanel(ActionListener actionListener)
	{
		pixelsButtons = new JButton[GRID_SIZE*GRID_SIZE];
		if(ifDefine)
		{
			pixelsArray = new double[GRID_SIZE][GRID_SIZE];
			this.fillPixelsArray(-1.0f);
		}
		this.preparePixelsButtons(actionListener);
		this.prepareActionsButtons(actionListener);
	}
	
	public void refreshButtonPanel(ActionListener actionListener)
	{
		pixelsButtons = new JButton[GRID_SIZE*GRID_SIZE];
		if(ifDefine)
		{
			pixelsArray = new double[GRID_SIZE][GRID_SIZE];
			this.fillPixelsArray(-1.0f);
		}
		this.preparePixelsButtons(actionListener);
		//this.prepareActionsButtons(actionListener);
	}
	
	/**
	 * 		Functions for PanelMessage
	 */
	
	public String getPanelMessage() {
		return panelMessage;
	}

	public void setPanelMessage(String panelMessage) {
		this.panelMessage = panelMessage;
	}
	
	/**
	 * 		Functions for Plus and Minus to GRID_SIZE
	 */
	
	public int getGRID_SIZE() {
		return GRID_SIZE;
	}

	public void setGRID_SIZE(int gRID_SIZE) {
		GRID_SIZE = gRID_SIZE;
	}
	public void addToGRID_SIZE(int value)
	{
		if(GRID_SIZE <= 75)
			GRID_SIZE += 5;
		else
		{
			System.out.println("Max cells limit reached!");
			GRID_SIZE = 80;
		}

		ifDefine = true;
		panelMessage = "Current network size: " + GRID_SIZE;
	}
	public void substractFromGRID_SIZE(int value)
	{
		if(GRID_SIZE >= 10)
			GRID_SIZE -= 5;
		else
		{
			System.out.println("Min cellse limit reached!");
			GRID_SIZE = 5;
		}
		ifDefine = true;
		panelMessage = "Current network size: " + GRID_SIZE;
	}
	
	/**
	 * 		Functions for buttonsNames
	 */

	public void setButtonsNames(List<String> buttonsNames) {
		this.buttonsNames = buttonsNames;
	}
	public void setButtonName(String buttonName, int index) 
	{
		if(index < actionButtonsList.size() && index >= 0)
			this.buttonsNames.set(index, buttonName);
		else
			System.err.println("Index out of boundary while in getButtonName method!");
	}
	private void addButtonName(String buttonName) 
	{
		this.buttonsNames.add(buttonName);
	}
	public List<String> getButtonsNames() {
		return this.buttonsNames;
	}
	public String getButtonName(int index) 
	{
		if(index < actionButtonsList.size() && index >= 0)
			return buttonsNames.get(index);
		else
		{
			System.err.println("Index out of boundary while in getButtonName method!");
			return null;
		}
	}

	/**
	 * 		Functions for actionsButtonsList
	 */

	public void setButtonsList(List<JButton> buttonsList) {
		this.actionButtonsList = buttonsList;
	}
	public void setButton(JButton button, int index) 
	{
		if(index < actionButtonsList.size() && index >= 0)
			this.actionButtonsList.set(index, button);
		else
			System.err.println("Index out of boundary while in setButton method!");
	}
	public void addButton(JButton button, String buttonName) 
	{
		this.actionButtonsList.add(button);
		this.addButtonName(buttonName);
	}

	public List<JButton> getButtonsList() {
		return this.actionButtonsList;
	}
	public JButton getButton(int index) {
		if(index < actionButtonsList.size() && index >= 0)
			return actionButtonsList.get(index);
		else
		{
			System.err.println("Index out of boundary while in getButton method!");
			return null;
		}
	}
	
	public void prepareActionsButtons(ActionListener actionListener)
	{
        for(int i = 0; i < buttonsNames.size(); i++)
        {
        	actionButtonsList.add(new JButton(buttonsNames.get(i)));
        	actionButtonsList.get(i).addActionListener(actionListener);
        }
	}

	/**
	 * 		Functions for PixelsButtons
	 */

	public JButton[] getPixelsButtons() {
		return pixelsButtons;
	}
	public JButton getPixelButton(int index) 
	{
		if(index < pixelsButtons.length && index >= 0)
			return pixelsButtons[index];
		else
		{
			System.err.println("Index out of boundary while in getButton method!");
			return null;
		}
	}
	public void setPixelsButtons(JButton[] pixelsButtons) {
		this.pixelsButtons = pixelsButtons;
	}
	public void setPixelButton(JButton button, int index) {
		this.pixelsButtons[index] = button;
	}
	public void colorPixelButton(int index)
	{
		String[] string = pixelsButtons[index].getName().split("x");
		pixelsArray[Integer.parseInt(string[0])][Integer.parseInt(string[1])] = -pixelsArray[Integer.parseInt(string[0])][Integer.parseInt(string[1])];
		if(pixelsButtons[index].getBackground() == Color.WHITE)
			pixelsButtons[index].setBackground(Color.BLACK);
		else
			pixelsButtons[index].setBackground(Color.WHITE);
	}
	public void colorPixelsButtons()
	{
		for(int i = 0; i < pixelsArray.length; i++)
			for(int j = 0; j < pixelsArray.length; j++)
				pixelsButtons[(GRID_SIZE*i)+j].setBackground(pixelsArray[i][j] == -1.0? Color.WHITE : Color.BLACK);	
	}
	public void preparePixelsButtons(ActionListener actionListener)
	{
		for(int i = 0; i < GRID_SIZE; i++)
		{	
			for(int j = 0; j < GRID_SIZE; j++)
			{
			//JButton jButton = new JButton();
				this.pixelsButtons[(GRID_SIZE*i)+j] = new JButton();
				this.pixelsButtons[(GRID_SIZE*i)+j].setName(i+"x"+j);
				this.pixelsButtons[(GRID_SIZE*i)+j].addActionListener(actionListener);
				this.pixelsButtons[(GRID_SIZE*i)+j].setBackground(pixelsArray[i][j] == -1.0? Color.WHITE : Color.BLACK);			
			}
		}
	}

	/**
	 * 		Functions for PixelsArray
	 */

	public double[][] getPixelsArray() {
		return pixelsArray;
	}
	public double getPixelFromArray(int x, int y) 
	{
		if(x < pixelsArray.length && x >= 0 && y < pixelsArray.length && y >= 0)
			return pixelsArray[x][y];
		else
		{
			System.err.println("Index out of boundary while in getPixelFromArray method!");
			return 0.0;
		}
	}
	public void setPixelsArray(double[][] pixelsArray) {
		this.pixelsArray = pixelsArray;
	}
	public void setPixelFromArray(int x, int y, double value) 
	{
		if(x < pixelsArray.length && x >= 0 && y < pixelsArray.length && y >= 0)
			pixelsArray[x][y] = value;
		else
		{
			System.err.println("Index out of boundary while in getPixelFromArray method!");
		}
	}
	
	public void loadImageIntoPixelsArray(BufferedImage image)
	{
		setPixelsArray(new double[image.getHeight()][image.getWidth()]);
		for (int x = 0; x < image.getWidth(); x++) {
		    for (int y = 0; y < image.getHeight(); y++) 
		    {
		        setPixelFromArray(y, x, (double) (image.getRGB(x, y) == 0xFFFFFFFF ? -1 : 1));
		    }
		}
		ifDefine = false;
		GRID_SIZE = image.getHeight();
	}

	public void fillPixelsArray(float value)
	{
		if(value != -1.0 && value != 1.0)
			value = -1.0f;
		
		this.pixelsArray = new double[GRID_SIZE][GRID_SIZE];
		
		for(int i = 0; i < this.pixelsArray.length; i++)
			Arrays.fill(this.pixelsArray[i], value);
	}


}


