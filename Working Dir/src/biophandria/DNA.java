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

public class DNA extends PApplet{
	
	/*genes:
	   BRAIN 0-19
	   0:brain input
	   1:brain hidden
	   2:brain output
	   3:learning rate
	   
	   HEAD 20-39
	   
	   20:HUE
	   21:SATURATION
	   22:BRIGHTNESS
	   
	   TORAX 40-59
	   
	   40:HUE1
	   41:HUE2
	   42:HUE3
	   43:HUE4
	   
	   BODY ATTRIBUTES:
	   100:MAXSPEED
	   101:SWT
	   102:AWT
	   103:CWT
	   104:WANDERR
	   105:WANDERD
	   106:DOMOVEMENTTILT
	   107:MOVEMENTTILT
	   
	   SPECIE:
	   300:C1
	   299:C2
	   298:C3
	   */


	  float[] dna;
	  int len = 302;

	  public DNA() {
	    dna = new float[len];

	    for (int i = 0; i<dna.length; i++) {
	      dna[i] = random(0, 1);
	    }
	  }

	  public DNA(float[] newdna) {
	  }

	  public DNA copy() {
	    float[] newdna = new float[dna.length];
	    arraycopy(dna, newdna);
	    return new DNA(newdna);
	  }

	  public DNA mate(DNA partner) {
	    float[] child = new float[dna.length];
	    int crossover = round(random(dna.length));

	    for (int i = 0; i<dna.length; i++) {
	      if (i>crossover) child[i] = getGene(i);
	      else            child[i] = partner.getGene(i);
	    }

	    DNA newdna = new DNA(child);
	    return newdna;
	  }

	  public DNA mateRange(DNA partner, int from, int to) {
	    float[] child = new float[dna.length];
	    int crossover = round(random(dna.length));

	    if (to<=dna.length) {

	      for (int i = from; i<to; i++) {
	        if (i>crossover) child[i] = getGene(i);
	        else            child[i] = partner.getGene(i);
	      }

	    }

	    DNA newdna = new DNA(child);
	    return newdna;
	  }

	  public DNA forceMate(float[] gene, int i) {
	    float[] child = new float[dna.length];
	    child[i] = gene[i];
	    DNA newdna = new DNA(child);
	    return newdna;
	  }

	  public void mutate(float m) {
	    for (int i = 0; i<dna.length; i++) {

	      if (random(1)<m) {
	        dna[i] = random(0, 1);
	      }

	    }
	  }

	  public float getGene(int index) {
	    return dna[index];
	  }
	  
	  public float[] getAllGenes() {
	    return dna;
	  }
	  
	  public void setGene(int index, float value) {
	    dna[index] = value;
	  }
}
