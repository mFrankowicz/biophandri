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

import gestalt.G;
import gestalt.render.AnimatorRenderer;

import mathematik.Random;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Biome {

	PApplet p;

	AnimatorRenderer r;
	G g;

	ArrayList<Creature> creatures;// ArrayList que contém a classe Creature.
	ArrayList<Food> foods;
	ArrayList<PVector> foodLoc; // hold food loc.
	// ArrayList<Creature> creatures;//ArrayList que contém a classe Creature.
	int num = 50;// numero de criaturas a serem inicializadas

	int foodNum = 5;

	int randomSpawn = 500;

	// Interactor interactor;

	public Biome(PApplet _p) {
		p = _p;
		// interactor = new Interactor();

		creatures = new ArrayList<Creature>();
		foods = new ArrayList<Food>();
		foodLoc = new ArrayList<PVector>();

		// creatures = new ArrayList<Creature>();
		for (int i = 0; i < num; i++) {
			boolean predator;
			float r = p.random(1);

			if (r < .9) {
				predator = true;
			} else {
				predator = false;
			}

			if (predator == true) {
				creatures.add(new Creature(p, i,
						new PVector(p.random(
								Global.worldSize / 2 - randomSpawn,
								Global.worldSize / 2 + randomSpawn), p.random(
								Global.worldSize / 2 - randomSpawn,
								Global.worldSize / 2 + randomSpawn)), true,
						true, true, true, true, true, true, false));
			} else {
				creatures.add(new Creature(p, i, new PVector(p.random(2000,
						2100), p.random(2000, 2100)), true, true, true, true,
						true, true, true, false));
			}
			// creatures.add(new Creature(new PVector(random(0, width),
			// random(0, height)), true, true, true, true, true, true, true));
		}

		for (int i = 0; i < foodNum; i++) {
			foods.add(new Food(p));
			Food f = (Food) foods.get(i);

			foodLoc.add(new PVector(f.loc.x, f.loc.y));
		}
	}

	public Biome(PApplet _p, AnimatorRenderer _r,  G _g) {
		
		p = _p;
		r = _r;
		g = _g;
		
		// interactor = new Interactor();

		creatures = new ArrayList<Creature>();
		foods = new ArrayList<Food>();
		foodLoc = new ArrayList<PVector>();

		// creatures = new ArrayList<Creature>();
		for (int i = 0; i < num; i++) {
			boolean predator;
			float r1 = p.random(1);

			if (r1 < .9) {
				predator = true;
			} else {
				predator = false;
			}

			if (predator == true) {
				creatures.add(new Creature(p,r,g, i,
						new PVector(p.random(
								Global.worldSize / 2 - randomSpawn,
								Global.worldSize / 2 + randomSpawn), p.random(
								Global.worldSize / 2 - randomSpawn,
								Global.worldSize / 2 + randomSpawn)), true,
						true, true, true, true, true, true, false));
			} else {
				creatures.add(new Creature(p,r,g, i, new PVector(p.random(2000,
						2100), p.random(2000, 2100)), true, true, true, true,
						true, true, true, false));
			}
			// creatures.add(new Creature(new PVector(random(0, width),
			// random(0, height)), true, true, true, true, true, true, true));
		}

		for (int i = 0; i < foodNum; i++) {
			foods.add(new Food(p));
			Food f = (Food) foods.get(i);

			foodLoc.add(new PVector(f.loc.x, f.loc.y));
		}
	}

	public void run() {
		/*
		 * for (CreatureCopy c : creatures) { //for (Creature c : creatures) {
		 * //Creature c =(Creature) creatures.get(i); c.run(creatures); //
		 * println(c.loc.x); }
		 */

		for (int i = 0; i < creatures.size(); i++) {
			Creature c = (Creature) creatures.get(i);
			c.run(creatures);
			if (c.health < 0) {
				creatures.remove(i);
			}
		}

		for (int i = 0; i < foods.size(); i++) {
			Food f = (Food) foods.get(i);
			if (f.canRemove() == true) {
				foods.remove(i);
			}
		}

		// println(creatures.size());
	}

	// ///////////////////////////////////////////////////////////
	public void render() {

		// interactor.render();

		for (Creature c : creatures) {
			c.render();
		}
		for (Food f : foods) {
			f.render();
		}
	}

	// /////////////////////////////////////////////
	// GLRENDERERS
	public void glRender() {
		for (Creature c : creatures) {
			c.glRender();
		}
	}

	// //////////////////////////////////////////////
	public void addCreature() {
	}

	// /////////////////////////////////////////////
	public void removeCreature() {
	}

	// /////////////////////////////////////////////
	public DNA makeRelative(Creature first, Creature second, int from, int to) {
		DNA dnafirst = first.getDna();
		DNA dnasecond = second.getDna();
		for (int i = from; i <= to; i++) {
			dnafirst.getAllGenes()[i] = dnasecond.getAllGenes()[i];
		}
		return dnafirst;
	}

	// /////////////////////////////////////////////
	public void addPlant() {
	}

	// /////////////////////////////////////////////
	public void removePlant() {
	}

	// /////////////////////////////////////////////
	public void addPolluition() {
	}

	// /////////////////////////////////////////////
	public void removePolluition() {
	}
	// /////////////////////////////////////////////
}
