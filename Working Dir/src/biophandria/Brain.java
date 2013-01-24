//Biophandria
//
//Copyright (C) 2013 Marcos Frankowicz + Jack de Castro Holmer
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.


package biophandria;

import processing.core.PApplet;

public class Brain extends PApplet {

	private DNA dna;
	private NetworkLayer input, hidden, output;

	public Brain(int _in, int _hid, int _out, float _learningRate) {
		input = new NetworkLayer();
		hidden = new NetworkLayer();
		output = new NetworkLayer();
	}

	public Brain(DNA _dna) {
		dna = _dna;

		int inputLayerNumbers = round(dna.getGene(0) * 100);
		int hiddenLayerNumbers = round(dna.getGene(1) * 100);
		int outputLayerNumbers = round(dna.getGene(2) * 100);
		float learninRate = dna.getGene(3) / 1;

		// test
		if (Global.BRAIN_DEBUG == true) {
			println("BRAIN: number of input neurons = " + inputLayerNumbers);
			println("BRAIN: number of hidden neurons = " + hiddenLayerNumbers);
			println("BRAIN: number of output neurons = " + outputLayerNumbers);
			println("BRAIN: learning rate = " + learninRate);
			println("---------------------------------------");
		}

		input = new NetworkLayer();
		hidden = new NetworkLayer();
		output = new NetworkLayer();

		input.initialize(inputLayerNumbers, 0, hiddenLayerNumbers, null,
				hidden, learninRate);
		hidden.initialize(hiddenLayerNumbers, inputLayerNumbers,
				outputLayerNumbers, input, output, learninRate);
		output.initialize(outputLayerNumbers, hiddenLayerNumbers, 0, hidden,
				null, learninRate);

	}

	public void setInput(int n, float val) {
		input.nodeValues[n] = val;
	}

	public float getOutput(int n) {
		return output.nodeValues[n];
	}

	public void setDesiredOutput(int n, float val) {
		output.desiredValues[n] = val;
	}

	public void feedForward() {
		input.calculateNodeValues();
		hidden.calculateNodeValues();
		output.calculateNodeValues();
	}

	public void backPropagate() {
		output.calculateErrors();
		hidden.calculateErrors();
		hidden.adjustWeights();
		input.adjustWeights();
	}

}
