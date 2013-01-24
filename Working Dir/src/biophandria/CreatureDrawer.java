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

import data.Resource;
import gestalt.G;
import gestalt.model.Model;
import gestalt.render.AnimatorRenderer;
import processing.core.*;

public class CreatureDrawer {

	PApplet p;
	G g;
	private DNA dna;
	private PVector loc;
	private PVector vel;
	private float h = 100;

	private float thet = 0;
	
	Model myModel;
	AnimatorRenderer r;
	
	public CreatureDrawer(PApplet _p, DNA _dna, PVector _loc, PVector _vel) {
		p = _p;
		dna = _dna;
		loc = _loc;
		vel = _vel;

		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 255;
		float h2 = dna.getGene(41) * 255;
		float h3 = dna.getGene(42) * 255;
		float h4 = dna.getGene(43) * 360;

		int siz = 25;

	}

	public CreatureDrawer(PApplet _p,AnimatorRenderer _r, G _g, DNA _dna, PVector _loc, PVector _vel) {
		p = _p;
		r = _r;
		g = _g;
		dna = _dna;
		loc = _loc;
		vel = _vel;

		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 255;
		float h2 = dna.getGene(41) * 255;
		float h3 = dna.getGene(42) * 255;
		float h4 = dna.getGene(43) * 360;

		int siz = 25;

		myModel = g.model(
				Resource.getStream("resource/data/besouro.obj"),
				Resource.getStream("resource/data/besouro.png"),
				true);
		myModel.mesh().material().lit = true;
		myModel.mesh().scale(10, 10, 10);

	}
	
	// TODO: criar métodos de renderização OPENGL.
	public void glRender(PVector l, PVector v) {
		loc = l;
		vel = v;
		
		float x = loc.x;
		float y = loc.y;
		
		
		float theta = vel.heading2D();
		
		myModel.mesh().position(x, y);
		myModel.mesh().rotation(0, 0, theta);
		
		
	}

	// sem shape
	public void render(PVector l, PVector v) {
		// fill(h, s, b);
		// espinha dorçal:
		loc = l;
		vel = v;
		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 360;
		float h2 = dna.getGene(41) * 360;
		float h3 = dna.getGene(42) * 360;
		float h4 = dna.getGene(43) * 360;

		float theta = vel.heading2D();

		p.pushStyle();
		p.pushMatrix();
		p.stroke(h1, s, b + 100, 255);
		p.translate(loc.x, loc.y);
		p.rotate(theta);
		p.fill(h1, s, b, 255);
		p.ellipse(0, 0, 15, 5); // <>//
		p.stroke(h2, s, b, 200);
		p.noFill();
		p.ellipse(0, 0, 30, 30);
		p.popMatrix();
		p.popStyle();

	}
	

}
