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

public class NetworkLayer extends PApplet{

	  private int nodesNumber; //numero de neurons
	  private int childNodesNumber;//numero de neurons depois
	  private int parentNodesNumber;//numero de neurons antes
	  
	  private float[][] weights;
	  public float[] nodeValues;
	  public float[] desiredValues;
	  private float[] errors;
	  private float learningRate;
	  
	  public NetworkLayer parent, child;
	  
	  public void initialize(int _nodesNumber, int _parentNumber, int _childNumber, NetworkLayer _parent, NetworkLayer _child, float _learninRate) {
	  
	    nodesNumber = _nodesNumber;
	    learningRate = _learninRate;
	    errors=new float[nodesNumber];
	    nodeValues=new float[nodesNumber];
	    desiredValues=new float[nodesNumber];

	    parent = _parent;
	    child = _child;
	    if (parent!=null)parentNodesNumber=_parentNumber;
	    if (child!=null)childNodesNumber=_childNumber;
	    if (child!=null) {
	      weights=new float[nodesNumber][childNodesNumber];
	      for (int i=0; i<nodesNumber;i++) {
	        for (int j=0; j<childNodesNumber;j++) {
	          weights[i][j]=random(-1, 1);
	        }
	      }
	    }
	  }

	  public void calculateNodeValues() {
	    float val;
	    if (parent!=null) {
	      for (int i = 0;i<nodesNumber;i++) {
	        val = 0;
	        for (int j = 0;j<parentNodesNumber;j++) {
	          val+=parent.nodeValues[j]*parent.weights[j][i];
	        }
	        nodeValues[i]=1/(1+exp(-val));//função de ativação.
	      }
	    }
	  }

	  public void calculateErrors() {
	    float sum;
	    if (child==null) {
	      for (int i = 0; i<nodesNumber;i++) {
	        errors[i]=(desiredValues[i]-nodeValues[i])*nodeValues[i]*(1-nodeValues[i]);
	      }
	    }
	    else if (parent==null) {
	      for (int i = 0;i<nodesNumber;i++) {
	        errors[i]=0;
	      }
	    }
	    else {
	      for (int i = 0; i<nodesNumber;i++) {
	        sum=0;
	        for (int j=0;j<childNodesNumber;j++) {
	          sum+=child.errors[j]*weights[i][j];
	        }
	        errors[i]=sum*nodeValues[i]*(1-nodeValues[i]);
	      }
	    }
	  }

	  public void adjustWeights() {
	    float adj;
	    if (child!=null) {
	      for (int i = 0 ;i<nodesNumber;i++) {
	        for (int j = 0;j<childNodesNumber;j++) {
	          adj=learningRate*child.errors[j]*nodeValues[i];
	          weights[i][j]+=adj;
	        }
	      }
	    }
	  }
}
