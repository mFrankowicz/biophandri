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

	/*
	 *  métodos atuais de movimentação:
	 *  	1: flock comum
	 *  	2: flock por visinhos
	 *  
	 *  métodos pretendidos
	 *  	1: caçar/movimentar sozinho
	 *  	2: caçar/movimentar em grupos pequenos
	 *   	3: decisões baseada em grupo
	 *   	4: decisões baseada em liderança
	 * 		5: movimentação causada pelo ambiente (ex: pólem)
	 */

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

public class Body {

	PApplet p;

	private boolean DIST_DEBUG = false;
	public PVector loc;// iniciamos a criatura em algum lugar no espaço.
	public PVector vel;
	public PVector acc;
	private float r;

	private float swt; // separation ratio
	private float awt; // alignment ratio
	private float cwt; // cohesion ratio

	private float wanderR; // 50 Radius for our "wander circle"
	private float wanderD; // 180 Distance for our "wander circle"
	private float change = 0.5f;

	private boolean doMovementTilt;
	private float movementTilt;

	private float wandertheta;

	private float maxspeed;
	private float maxforce = 0.015f;

	private int specieDebug;

	private int id;

	private boolean lowDensityFlock = true;

	private ArrayList<Creature> neighbords = new ArrayList<Creature>();
	private int countNeighbords = 0;
	private float neighAddDist = 500;

	private DNA dna;

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// COSTRUCTORS
	public Body(PApplet _p, DNA _dna, PVector _loc, PVector _vel, PVector _acc,
			int _id) {
		p = _p;
		// neighbords.add(new Creature());
		id = _id;

		dna = _dna;

		loc = _loc;
		acc = _acc;
		vel = _vel;
		r = 2.0f;

		// setAttributesByDna(dna);

		specieDebug = p.round(p.random(0, 4));

		float wr = 80;
		float wd = 80;

		if (specieDebug == 0) {
			setAttributes(5, 8, 5, 5, wr, wd);
		}

		else if (specieDebug == 1) {
			setAttributes(5, 6, 4, 5, wr, wd);
		}

		else if (specieDebug == 2) {
			setAttributes(5, 8, 3, 5, wr, wd);
		}

		else if (specieDebug == 3) {
			setAttributes(5, 6, 3, 5, wr, wd);
		}

		else if (specieDebug == 4) {
			setAttributes(5, 10, 4, 5, wr, wd);
		}

		wandertheta = 0;

	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// ATTRIBUTES

	// SET ATTRIBUTES BY ARGUMENTS
	private void setAttributes(float spd, float sep, float ali, float coh,
			float wr, float wd) {
		maxspeed = spd;

		swt = sep;
		awt = ali;
		cwt = coh;

		wanderR = wr;
		wanderD = wd;

		doMovementTilt = true;
	}

	// SET ATTRIBUTES BY DNA
	public void setAttributesByDna(DNA d) {
		maxspeed = (d.getGene(100) * 5) + 3;
		swt = d.getGene(101) * 100;
		awt = d.getGene(102) * 20;
		cwt = d.getGene(103) * 5;

		wanderR = (d.getGene(104) * 30) + 20;
		wanderD = (d.getGene(105) * 200) + 50;

		if (d.getGene(106) < 0.8) {
			doMovementTilt = true;
		} else {
			doMovementTilt = false;
		}
		movementTilt = (d.getGene(107));
		// println(movementTilt);
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// MAIN METHODO TO RUN METHODOS

	// RUN
	// TODO: passar métodos para decisões na classe Creature
	// TODO: implementar outros métodos de movimentação
	public void run(ArrayList<Creature> creatures) {
		// TODO: criat métodos para debug visual
		if (DIST_DEBUG == true) {
			p.noFill();
			p.stroke(120, 200, 200, 150);
			p.ellipse(loc.x, loc.y, neighAddDist, neighAddDist);
			for (Creature n : neighbords) {
				p.line(loc.x, loc.y, n.loc.x, n.loc.y);
			}
		}

		addNeighbord(creatures);
		move();
		flock(creatures);
		removeNeighbord();

		if (p.keyPressed && p.key == 's') {
			lowDensityFlock = false;
		} else {
			lowDensityFlock = true;
		}
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// MÉTODOS DE MOVIMENTAÇÃO
	
	// APPLYFORCE
	// TODO: Adicionar peso (acc=f/m);
	private void applyForce(PVector force) {
		acc.add(force);
	}

	// MOVE
	// TODO: passar métodos para decisões na classe Creature
	public void move() {

		vel.add(acc);
		vel.limit(maxspeed);
		loc.add(vel);

		PVector wan = wander();
		wan.mult(2);

		applyForce(wan);

		acc.mult(0.3f);

		borders();
	}

	// STOP
	public void stop() {
		vel.mult(0);
		acc.mult(0);
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// FLOCK, SEEK & FLEE METHODS
	// TODO: passar métodos para decisões na classe Creature.

	// FLOCK
	public void flock(ArrayList<Creature> creatures) {

		PVector sep = separate(creatures);

		// PVector ali = align(creatures);
		PVector ali = alignByNeighbord();

		// PVector coh = cohesion(creatures);
		PVector coh = cohesionByNeighbord();

		PVector wan = wander();
		// PVector fle = flee();

		sep.mult(swt);
		ali.mult(awt);
		coh.mult(cwt);
		// wan.mult(2);
		// fle.mult(10);

		applyForce(sep);
		applyForce(ali);
		applyForce(coh);
		// applyForce(wan);

		// TODO: fazer com que os agentes reajam com métodos "separation"
		// mudando a densidade do enxame.

		if (p.keyPressed && p.key == 'f') {
			// applyForce(fle);
		}
	}

	// SEEK
	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, loc);// um vetor apontando da
													// localização para o alvo
		// normalizar e multiplicação escalar
		desired.normalize();

		desired.mult(maxspeed);
		// steer = pretendido menos velocidade
		PVector steer = PVector.sub(desired, vel);
		steer.limit(maxforce);
		return steer;
	}

	// FLEE FROM ENEMY
	public PVector fleeFromEnemy(Creature other) {
		float radi = 200;

		PVector steer = new PVector(0, 0);
		PVector otherLoc = other.loc;

		float d = PVector.dist(loc, otherLoc);
		if ((d < radi)) {
			PVector diff = PVector.sub(loc, otherLoc);
			diff.normalize();
			diff.div(d);
			steer.add(diff);
		}
		return steer;
	}

	// FLEE (FROM MOUSE)
	// TODO: método de camera e posição do mouse!!
	/*
	 * PVector flee() { float radi = 200; //float mx = mouseX; //float my =
	 * mouseY;
	 * 
	 * // float mx =
	 * map(mouseX,0,width,-worldSize/((float)cam.getWheelScale()*1.8
	 * ),worldSize/((float)cam.getWheelScale()*1.8)); // float my =
	 * map(mouseY,0,height,-worldSize/3,worldSize/3);
	 * 
	 * // float mx = map(mouseX,-worldSize/2,worldSize/2,-width,width); // float
	 * my = map(mouseY,-worldSize/2,worldSize/2,-height,height);
	 * 
	 * float mx = map(mouseX, 0, width, -(float)( cam.getDistance() ), (float)(
	 * cam.getDistance() ) ); float my = map(mouseY, 0, height, -(float)(
	 * cam.getDistance() ), (float)( cam.getDistance() ) );
	 * 
	 * 
	 * PVector newMouse = new PVector(); PVector screenMouse = new PVector(mx,
	 * my);
	 * 
	 * newMouse.set(cam.getPosition()[0], cam.getPosition()[1], 0);
	 * newMouse.add(screenMouse); pushStyle(); noFill(); stroke(255);
	 * //ellipse(screenMouse.x, screenMouse.y, radi, radi); ellipse(newMouse.x,
	 * newMouse.y, radi, radi); popStyle();
	 * 
	 * PVector steer = new PVector(0, 0); PVector mouse = new
	 * PVector(newMouse.x, newMouse.y);
	 * 
	 * float d = PVector.dist(loc, mouse); if ((d<radi)) { PVector diff
	 * =PVector.sub(loc, mouse); diff.normalize(); diff.div(d); steer.add(diff);
	 * } return steer; }
	 */

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// SEPARATE, ALIGN & COHESION (3 BASIV RULES OF FLOCK)

	// SEPARATE
	// regra de separação
	// este método checa corpos próximos and aplica o steer contrário
	public PVector separate(ArrayList<Creature> creatures) {
		float desiredseparation = 40;// numero para definir a distância de
										// separação
		/*
		 * if (lowDensityFlock) { desiredseparation = 40; } else {
		 * desiredseparation = 200; }
		 */
		PVector steer = new PVector(0, 0);
		int count = 0;
		// para cada Creature no sistema, checar se está muito perto
		for (Creature other : creatures) {
			float d = PVector.dist(loc, other.loc);
			// se a distância é maior que 0 e menor que um número abistrário (ja
			// definido)
			if ((d > 0) && (d < desiredseparation)) {
				// calcula o vetor apontando para o vizinho
				PVector diff = PVector.sub(loc, other.loc);
				diff.normalize();
				// divisão de vetor = a.b-¹ (a vezes o inverso de b);
				diff.div(d);
				steer.add(diff);
				count++; // contador
			}
		}
		// média: dividir pelo tanto contado

		if (count > 0) {
			steer.div((float) count);
			// implementar equação de reynolds: steer = desejado - velocidade
			steer.normalize();
			steer.mult(maxspeed);// escalear
			steer.sub(vel);
			steer.limit(maxforce);
		}
		return steer;
	}

	// ALIGN
	// regra de alinhamento
	// para cada Creature perto, calcula a velocidade média
	public PVector align(ArrayList<Creature> creatures) {
		float neighbordist = 40.0f;
		PVector steer = new PVector();
		int count = 0;
		for (Creature other : creatures) {
			float d = PVector.dist(loc, other.loc); // V- passar para classe
													// creature
			if (((d > 0) && (d < neighbordist))) {
				steer.add(other.vel);
				count++;
				p.stroke(255);
				p.line(loc.x, loc.y, other.loc.x, other.loc.y);
			}
		}
		if (count > 0) {
			steer.div((float) count);
			steer.normalize();
			steer.mult(maxspeed);
			steer.sub(vel);
			steer.limit(maxforce);
		}
		return steer;
	}

	// COHESION
	// método para coesão
	// para cada localização média (ex: centro) de todos os Creature perto,
	// ...calcula o vetor steer em direção à aquela localização
	public PVector cohesion(ArrayList<Creature> creatures) {
		float neighbordist = 100.0f;
		PVector sum = new PVector(0, 0); // iniciar com um vetor vazio para
											// acumular todas as localizações
		int count = 0;
		for (Creature other : creatures) {
			float d = PVector.dist(loc, other.loc);
			if (((d > 0) && (d < neighbordist))) {
				sum.add(other.loc);
				count++;
			}
		}
		if (count > 0) {
			sum.div((float) count);
			return seek(sum);
		}
		return sum;
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// NEIGHBORDS FLOCK RULES METHODS

	// COHESION BY NEIGHBORD
	public PVector cohesionByNeighbord() {
		float neighbordist = 1000.0f;
		PVector sum = new PVector(0, 0); // iniciar com um vetor vazio para
											// acumular todas as localizações
		int count = 0;
		for (Creature other : neighbords) {
			float d = PVector.dist(loc, other.loc);
			if (((d > 0) && (d < neighbordist))) {
				sum.add(other.loc);
				count++;
			}
		}
		if (count > 0) {
			sum.div((float) count);
			return seek(sum);
		}
		return sum;
	}

	// ALIGN BY NEIGHBORD
	public PVector alignByNeighbord() {

		float neighbordist = 2000.0f;
		PVector steer = new PVector();
		int count = 0;
		for (Creature other : neighbords) {
			float d = PVector.dist(loc, other.loc); // V- passar para classe
													// creature
			if (((d > 0) && (d < neighbordist))) {
				steer.add(other.vel);
				count++;
				// stroke(dna.getGene(40)*255, 200, 200, 50);
				// line(loc.x, loc.y, other.loc.x, other.loc.y);
			}
		}

		if (count > 0) {
			steer.div((float) count);
			steer.normalize();
			steer.mult(maxspeed);
			steer.sub(vel);
			steer.limit(maxforce);
		}
		return steer;
		// return null;
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// WANDER > THIS GIVE MOVE() A WANDERING MOVEMENT
	
	public PVector wander() {
		// float wanderR = 50; // Radius for our "wander circle"
		// float wanderD = 180; // Distance for our "wander circle"
		// float change = 0.1;
		wandertheta += p.random(-change, change); // Randomly change wander
													// theta

		// Now we have to calculate the new location to steer towards on the
		// wander circle
		PVector circleloc = vel.get(); // Start with velocity
		circleloc.normalize(); // Normalize to get heading
		circleloc.mult(wanderD); // Multiply by distance
		circleloc.add(loc); // Make it relative to boid's location

		float h = vel.heading2D(); // We need to know the heading to offset
									// wandertheta

		PVector circleOffSet = new PVector(wanderR * p.cos(wandertheta + h),
				wanderR * p.sin(wandertheta + h));
		PVector target = PVector.add(circleloc, circleOffSet);
		return seek(target);
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// BORDER
	private void borders() {
		if (loc.x < -Global.worldSize + r)
			loc.x = Global.worldSize + r;
		if (loc.y < -Global.worldSize + r)
			loc.y = Global.worldSize + r;
		if (loc.x > Global.worldSize + r)
			loc.x = -Global.worldSize + r;
		if (loc.y > Global.worldSize + r)
			loc.y = -Global.worldSize + r;
	}

	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// GET GENES
	public DNA getGenes() {
		return dna;
	}
	/* /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ */
	/*                                                                         */
	/* \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ */
	// NEIGHBORDS ADD AND REMOVE > BASICALY A ARRAYLIST THA HOLD NEAR CREATURES
	
	// ADD NEIGHBORD
	private void addNeighbord(ArrayList<Creature> creatures) {
		float neighbordist = neighAddDist;
		int count = 0;
		for (Creature other : creatures) {
			float d = PVector.dist(loc, other.loc);

			if (((d > 0) && (d < neighbordist))) {
				if ((countNeighbords == 0)) {
					neighbords.add(other);
					countNeighbords++;
					// count++;
				} else if (other != neighbords.get(count)
						&& (countNeighbords < 10)) {
					neighbords.add(other);
					countNeighbords++;
					count++;
				}
			}
		}
	}
	// ADD NEIGHBORD
	private void removeNeighbord() {
		for (int i = 0; i < neighbords.size(); i++) {
			Creature neigh = (Creature) neighbords.get(i);
			float d = PVector.dist(neigh.loc, loc);
			if (d > 1100) {
				countNeighbords--;
				neighbords.remove(i);
			}
		}
	}
}
