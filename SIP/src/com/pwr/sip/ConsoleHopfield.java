package com.pwr.sip;

public class ConsoleHopfield {

	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(final boolean b[]) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.length - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}

	/**
	 * A simple main method to test the Hopfield neural network.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {

		// Create the neural network.
		final HopfieldNetwork network = new HopfieldNetwork(8);
		// This pattern will be trained
		final boolean[] pattern1 = { true, true, false, false,true, false,true,true };
		// This pattern will be presented
		final boolean[] pattern2 = { true, false, false, false,false,true ,true,true };
		
		final boolean[] pattern3 = { true, false, true, false,true,true ,true,true };

		boolean[] result;

		// train the neural network with pattern1

		//network.learnPseudoInversion(pattern1);

		int count=0;
		while(true){
			network.learnDelta(pattern1,1);
			network.learnDelta(pattern2,2);
			network.learnDelta(pattern3,3);
			network.getMatrix().show();
			if(network.errorMatrixChanged(pattern1, 1)&&network.errorMatrixChanged(pattern2, 2)&&network.errorMatrixChanged(pattern3, 3))
				break;
			count++;


			System.out.println("Iteration:"+count);
		}
		
		result = network.present(pattern1);
		System.out.println("Presenting pattern:" + formatBoolean(pattern1)
				+ ", and got " + formatBoolean(result));
		// Present pattern2, which is similar to pattern 1. Pattern 1
		// should be recalled.
		result = network.present(pattern2);
		System.out.println("Presenting pattern:" + formatBoolean(pattern2)
				+ ", and got " + formatBoolean(result));

	}

}
