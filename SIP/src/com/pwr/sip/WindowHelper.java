package com.pwr.sip;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WindowHelper {

	public static BufferedImage[] loadImage(JFrame frame, String dialogTitle) {

		File imageFile;
		File imageFiles[] = null;
		BufferedImage image = null;

		final JFileChooser fc = new JFileChooser(".");
		fc.setMultiSelectionEnabled(true);
		fc.setDialogTitle(dialogTitle);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Images", "bmp");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			imageFiles = fc.getSelectedFiles();
			imageFile = fc.getSelectedFile();

			BufferedImage images[] = new BufferedImage[imageFiles.length];

			try {
				for (int i = 0; i < imageFiles.length; i++) {
					images[i] = ImageIO.read(imageFiles[i]);
				}
				image = ImageIO.read(imageFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return images;
		} else {
			JOptionPane.showMessageDialog(frame, "Error when trying to open a file!");
			return null;
		}

	}

	public static void saveImage(JFrame frame, ButtonPanel buttonPanel) {
		final JFileChooser fc = new JFileChooser(".");
		int GRID_SIZE = buttonPanel.getGRID_SIZE();
		File imageFile;
		String imagePath;
		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			imageFile = fc.getSelectedFile();
			imagePath = imageFile.getPath();
			if (!imagePath.contains(".bmp"))
				imagePath += ".bmp";

			BufferedImage image = new BufferedImage(GRID_SIZE, GRID_SIZE, BufferedImage.TYPE_BYTE_BINARY);
			Graphics g = image.getGraphics();
			for (int i = 0; i < GRID_SIZE; i++)
				for (int j = 0; j < GRID_SIZE; j++) {
					g.setColor(buttonPanel.getPixelFromArray(j, i) == -1.0 ? Color.WHITE : Color.BLACK);
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

	public static boolean processTraining(int method, HopfieldNetwork network, ArrayList<double[][]> arrayedImages, ButtonPanel buttonPanel) {
		if (method == 0)
			method = 1; // Delta method from default

		boolean[][] patternsArray = null;

		if (arrayedImages == null) {
			patternsArray = new boolean[1][buttonPanel.getGRID_SIZE() * buttonPanel.getGRID_SIZE()];
			patternsArray[0] = BiPolarUtil.double2d_2_1dbipolar(buttonPanel.getPixelsArray());
			System.out.println("Training Hopfield network with: " + MatrixMath.formatBoolean(patternsArray[0]));
		} else {
			patternsArray = new boolean[arrayedImages.size()][];
			for (int i = 0; i < arrayedImages.size(); i++) {
				patternsArray[i] = new boolean[arrayedImages.get(i).length * arrayedImages.get(i).length];
				patternsArray[i] = BiPolarUtil.double2d_2_1dbipolar(arrayedImages.get(i));
				System.out.println("Training Hopfield network with: " + MatrixMath.formatBoolean(patternsArray[i]));
			}
		}
		// This pattern will be trained
		// final boolean[] pattern =
		// BiPolarUtil.double2d_2_1dbipolar(buttonPanel.getPixelsArray());
		// train the neural network with the pattern
		// System.out.println("Training Hopfield network with: "
		// + MatrixMath.formatBoolean(pattern));

		// choose patern for learning
		int count = 0;
		if (method == 1) {

			int patternsNotChanged = 0;
			while (true) {
				// network.learnDelta(pattern);
				for (int i = 0; i < patternsArray.length; i++) {
					System.out.println(patternsArray[i].length);
					network.learnDelta(patternsArray[i], i + 1);
					if (network.errorMatrixChanged(patternsArray[i], i + 1))
						patternsNotChanged++;
				}
				// network.getMatrix().show();
				if (count != 0)
					if (patternsNotChanged == patternsArray.length)
						break;
				count++;

				System.out.println("Iteration:" + count);
				patternsNotChanged = 0;
			}

		} else {
			for (int i = 0; i < patternsArray.length; i++)
				network.learnPseudoInversion(patternsArray[i]);
		}
		System.out.println(">>>> " + count);
		return true;
	}

	public static boolean[] processRecognition(ButtonPanel buttonPanel, HopfieldNetwork network) {
		// This pattern will be presented
		final boolean[] pattern1 = BiPolarUtil.double2d_2_1dbipolar(buttonPanel.getPixelsArray());

		boolean[] result;
		result = network.present(pattern1);
		System.out.println("Presenting pattern:" + MatrixMath.formatBoolean(pattern1) + ", and got " + MatrixMath.formatBoolean(result));
		buttonPanel.setPanelMessage("Recognition completed");
		return result;
	}

	public static ArrayList<double[][]> loadImagesIntoArrayList(BufferedImage bufferedImages[]) {
		ArrayList<double[][]> arrayedImages = new ArrayList<double[][]>();
		for (BufferedImage bf : bufferedImages) {
			double[][] tmp = new double[bf.getHeight()][bf.getWidth()];
			for (int x = 0; x < bf.getWidth(); x++) {
				for (int y = 0; y < bf.getHeight(); y++) {
					tmp[y][x] = (double) (bf.getRGB(x, y) == 0xFFFFFFFF ? -1 : 1);
				}
			}
			arrayedImages.add(tmp);
		}
		return arrayedImages;
	}
}
