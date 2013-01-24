package biophandria;

import processing.core.*;

public class Main extends PApplet {

	public Biome bio;

	public void setup() {
		size(600, 400, JAVA2D);
		bio = new Biome(this);
	}

	public void draw() {
		background(0);
		bio.run();
		bio.render();
	}

	static public void main(String[] args) {
		PApplet.main(new String[] { "--bgcolor=#ECE9D8", "Main" });

	}

}
