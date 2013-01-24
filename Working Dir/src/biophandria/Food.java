package biophandria;

import processing.core.PApplet;
import processing.core.PVector;

public class Food {
	PApplet p;
	int kind; // 0 = vegetarian, 1 = meat;
	float energyGiven;
	float life;
	boolean isRotten;
	PVector loc;

	public Food(PApplet _p) {
		p = _p;
		loc = new PVector(p.random(0, Global.worldSize), p.random(0,
				Global.worldSize));

		energyGiven = p.random(10, 100);

		if (p.random(0, 1) < 0.5) {
			kind = 0;
		} else {
			kind = 1;
		}

		if (kind == 0) {
			life = p.random(300, 500);
		} else if (kind == 1) {
			life = p.random(100, 800);
		}

	}

	Food(PVector _loc, int _kind) {
		life = 100;
		loc = _loc;
		kind = _kind;
		energyGiven = p.random(10, 100);
	}

	public void run() {

	}

	public void render() {
		p.pushStyle();
		p.noStroke();
		p.pushMatrix();
		p.translate(loc.x, loc.y);
		if (kind == 0 && getRottenState() == false) {
			p.fill(84, 200, life, 360);
		} else if (kind == 1 && getRottenState() == false) {
			p.fill(26, 200, life, 360);
		} else if (getRottenState() == true) {
			p.fill(0, 100, 100, 360);
		}
		p.ellipse(0, 0, energyGiven, energyGiven);
		p.popMatrix();
		p.popStyle();
		life -= 0.1;
	}

	public int getKind() {
		return kind;
	}

	public void setEnergyGiven(float amount) {
		energyGiven = amount;
	}

	public void setLive(float amount) {
		life = amount;
	}

	public boolean getRottenState() {
		if (life <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canRemove() {
		if (life <= -1000) {
			return true;
		} else {
			return false;
		}
	}

}
