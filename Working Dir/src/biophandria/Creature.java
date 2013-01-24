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

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import mathematik.*;

public class Creature {
	PApplet p;
	
	AnimatorRenderer r;
	G g;
	
	public PVector loc;
	public PVector acc;
	public PVector vel;

	private Body bodyCopy;// toda criatura tem um corpo, costituido de no
							// mínimo: cabeça,torax e órgãos vitais.
	private DNA dna;// toda criatura tem suas características herdadas.
	private Brain brain;
	
	// Decision decision;
	// ArrayList<Decision> otherDecision;

	private int brainInput;
	private int brainHidden;
	private int brainOutput;
	private int repeat_teaching = 1000;

	private boolean canWalk;// pode se movimentar?
	private boolean canRun;// pode correr?

	private boolean walking;// está andando ou parado?
	private boolean running;// está correndo ou andando?

	private boolean haveMouth;// a criatura tem boca?
	private boolean haveEye;// olhos?
	private boolean haveEar;// orelhas?
	private boolean haveNose;// nariz?
	private boolean haveHand;// braço,mão,barbatanas?
	private boolean haveLeg;// pernas,pé,barbatanas,rabo?
	private boolean haveReproductive;// aparelho reprodutor?

	//private boolean haveShape;// carrega shape

	private boolean isPredator; // boolean para identificar predadores
	private boolean isHunting;

	public float health = 100f;
	private float hungry = 1f;

	// float fitness;

	private int id;
	
	CreatureDrawer creatureDrawer;
	
	// SEM DNA VVVVVV
	public Creature(PApplet _p,int _id, PVector _loc, boolean _mouth, boolean _eye,
			boolean _ear, boolean _nose, boolean _hand, boolean _leg,
			boolean _reproductive, boolean _isPredator) {
		
		p = _p;
		
		id = _id;
		isPredator = _isPredator;

		loc = _loc;
		acc = new PVector(0, 0);
		vel = new PVector(p.random(-1, 1), p.random(-1, 1));
		dna = new DNA();
		// numero fixos de neurons
		dna.setGene(0, 0.02f);// in
		dna.setGene(1, 0.03f);// hidden
		dna.setGene(2, 0.02f);// out
		dna.setGene(3, 0.25f);// learn

		// fitness = random(1);

		brainInput = p.round(dna.getGene(0) * 100);
		brainHidden = p.round(dna.getGene(1) * 100);
		brainOutput = p.round(dna.getGene(2) * 100);
		//println("input: " + brainHidden);

		// muitas das capacidades do cérebro são herdadas pelo dna
		// UTILIZAR MAIS DE UM "BRAIN" PARA SEPARAR FUNCIONALIDADES
		brain = new Brain(dna);
		// movementCortex = new Brain(dna);
		// socialCortex = new Brain(dna);
		// enviromentCortex = new Brain(dna);

		haveMouth = _mouth;
		haveEye = _eye;
		haveEar = _ear;
		haveNose = _nose;
		haveHand = _hand;
		haveLeg = _leg;
		haveReproductive = _reproductive;

		//decision = new Decision();
		//otherDecision = new ArrayList<Decision>();

		// //toda criatura tem um corpo físico com habilidades de movimento
		bodyCopy = new Body(p, dna, loc, vel, acc, id);
		creatureDrawer = new CreatureDrawer(p,dna, loc, vel);
	}

	// ////////////
	// ///////////
	// //////////
	// COM DNA VVVVVVVV//
	public Creature(PApplet _p,DNA _dna, int _id, PVector _loc, boolean _mouth, boolean _eye,
			boolean _ear, boolean _nose, boolean _hand, boolean _leg,
			boolean _reproductive, boolean _isPredator) {
		
		p = _p;
		
		id = _id;
		isPredator = _isPredator;

		loc = _loc;
		acc = new PVector(0, 0);
		vel = new PVector(p.random(-1, 1), p.random(-1, 1));
		dna = _dna;
		// numero fixos de neurons
		dna.setGene(0, .02f);// in
		dna.setGene(1, .03f);// hidden
		dna.setGene(2, .01f);// out
		dna.setGene(3, .25f);// learn

		brainInput = (int) dna.getGene(0) * 100;
		brainHidden = (int) dna.getGene(1) * 100;
		brainOutput = (int) dna.getGene(2) * 100;
		//println("input: " + brainHidden);

		// muitas das capacidades do cérebro são herdadas pelo dna
		// UTILIZAR MAIS DE UM "BRAIN" PARA SEPARAR FUNCIONALIDADES
		brain = new Brain(dna);
		// movementCortex = new Brain(dna);
		// socialCortex = new Brain(dna);
		// enviromentCortex = new Brain(dna);

		haveMouth = _mouth;
		haveEye = _eye;
		haveEar = _ear;
		haveNose = _nose;
		haveHand = _hand;
		haveLeg = _leg;
		haveReproductive = _reproductive;

		//haveShape = false;

		//decision = new Decision();
		//otherDecision = new ArrayList<Decision>();

		// //toda criatura tem um corpo físico
		bodyCopy = new Body(p, dna, loc, vel, acc, id);
		creatureDrawer = new CreatureDrawer(p,dna, loc, vel);
	}

	// ///////
	// ///////
	// //////////////////////
	// ///////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////
	//GESTALT METHOD
	public Creature(PApplet _p, AnimatorRenderer _r, G _g,int _id, PVector _loc, boolean _mouth, boolean _eye,
			boolean _ear, boolean _nose, boolean _hand, boolean _leg,
			boolean _reproductive, boolean _isPredator) {
		p = _p;
		r = _r;
		g = _g;
		
		id = _id;
		isPredator = _isPredator;

		loc = _loc;
		acc = new PVector(0, 0);
		vel = new PVector(p.random(-1, 1), p.random(-1, 1));
		dna = new DNA();
		// numero fixos de neurons
		dna.setGene(0, 0.02f);// in
		dna.setGene(1, 0.03f);// hidden
		dna.setGene(2, 0.02f);// out
		dna.setGene(3, 0.25f);// learn

		// fitness = random(1);

		brainInput = p.round(dna.getGene(0) * 100);
		brainHidden = p.round(dna.getGene(1) * 100);
		brainOutput = p.round(dna.getGene(2) * 100);
		//println("input: " + brainHidden);

		// muitas das capacidades do cérebro são herdadas pelo dna
		// UTILIZAR MAIS DE UM "BRAIN" PARA SEPARAR FUNCIONALIDADES
		brain = new Brain(dna);
		// movementCortex = new Brain(dna);
		// socialCortex = new Brain(dna);
		// enviromentCortex = new Brain(dna);

		haveMouth = _mouth;
		haveEye = _eye;
		haveEar = _ear;
		haveNose = _nose;
		haveHand = _hand;
		haveLeg = _leg;
		haveReproductive = _reproductive;

		//decision = new Decision();
		//otherDecision = new ArrayList<Decision>();

		// //toda criatura tem um corpo físico com habilidades de movimento
		bodyCopy = new Body(p, dna, loc, vel, acc, id);
		creatureDrawer = new CreatureDrawer(p,r,g,dna, loc, vel);
	}
	
	
	public void run(ArrayList<Creature> creatures) {
		loc = bodyCopy.loc;
		vel = bodyCopy.vel;
		acc = bodyCopy.acc;
		
		bodyCopy.run(creatures);

		beHunted(creatures);
		if (isPredator && hungry < 3) {
			isHunting = true;
		} else {
			isHunting = false;
		}
		// println(relativeness(creatures,4));
	}

	// /sem shape
	public void render() {
		//bodyCopy.render();
		creatureDrawer.render(loc,vel);
	}
	
	public void glRender() {
		//bodyCopy.render();
		creatureDrawer.glRender(loc,vel);
	}

	// TODO: implementar métodos para ações comportamentais.
	void action() {
	}

	// TODO: acções baseadas nos outputs da rede neural.
	void brainAction() {
		if (brain.getOutput(0) > 0.5) {
			// walkTogheter();
		}
	}

	// TODO: passar métodos de comer para métodos nas classes correspondentes!!
	// método para consumir comida! diferente de caçar comida!!
	void eat(ArrayList<Food> theFood) {
		for (Food f : theFood) {
			float d = PVector.dist(bodyCopy.loc, f.loc);
			if (d < 20) {
				// TODO: implementar métodos de parar para comer, (é uma
				// decisão).

			}
		}
	}

	void defecate() {
	}
    
	/*
	// ///////////////////////
	// ////////////////////////////////////////////////////////////
	// MÉTODOS DE DECISÕES.
	void walkAlone() {
		if (decision.move) {
			bodyCopy.move();
		}
	}

	void walkTogheter(ArrayList<Creature> creatures) {
		if (decision.moveTogheter) {
			bodyCopy.flock(creatures);
		}
	}

	void stop() {
		if (decision.move == false) {
			bodyCopy.stop();
		}
	}

	public void rest() {
		float amount = 100;
		if (decision.rest) {
			amount -= 100;
		}
		if (amount == 0) {
			// TODO: actions here.
		}
	}
*/
	// se outro é predator
	public void beHunted(ArrayList<Creature> creatures) {
		for (Creature other : creatures) {
			float d = PVector.dist(loc, other.loc);

			if (((d > 0) && (d < 40))
					&& (other.isPredator == true && isPredator == false)) {
				if (other.isHunting) {
					fight(other);
					bodyCopy.fleeFromEnemy(other);
				}
			}
		}
	}

	public void fight(Creature other) {
		float r = p.random(0, 1);
		if (r < 5) { // substituir por stregth
			other.health -= 1;
		} else {
			health -= 1;
		}
	}

	// ////////////////////////////////////////////////////////////////

	// MÉTODOS DE PROCRIAÇÃO
	public Creature copulate(ArrayList<Creature> creatures) {
		Creature c = null;
		for (Creature other : creatures) {
			DNA partner = other.dna;

			if (relativeness(creatures, 3, 297, 391) == true) {
				DNA childDNA = dna.mate(partner);
				childDNA.mutate(0.01f); // mudar (isPredator)
				c = new Creature(childDNA, p.round(p.random(9999999)), loc,
						true, true, true, true, true, true, true, false);
			}
		}
		return c;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// BRAIN FUNCTIONS
	// TODO: implementar esses métodos em brainActions().
	public void train() {
		for (int i = 0; i < repeat_teaching; i++) {
			float r = p.random(1);
			if (r < 0.25f) {
				brain.setInput(0, p.random(0.5f, 1f));
				brain.setInput(1, p.random(0.5f, 1f));
				brain.setDesiredOutput(0, 1f);
				brain.setDesiredOutput(1, 0f);
				brain.feedForward();
				brain.backPropagate();
			} else if (r >= 0.25f && r < 0.5f) {
				brain.setInput(0, p.random(0f, 0.5f));
				brain.setInput(1, p.random(0f, 0.5f));
				brain.setDesiredOutput(0, 0.5f);
				brain.setDesiredOutput(1, 0.5f);
				brain.feedForward();
				brain.backPropagate();
			} else if (r >= 0.5f && r < 0.75f) {
				brain.setInput(0, p.random(0.5f, 1f));
				brain.setInput(1, p.random(0f, 0.5f));
				brain.setDesiredOutput(0, 0.5f);
				brain.setDesiredOutput(1, 0.5f);
				brain.feedForward();
				brain.backPropagate();
			} else {
				brain.setInput(0, p.random(0f, 0.5f));
				brain.setInput(1, p.random(0f, 0.5f));
				brain.setDesiredOutput(0, 0f);
				brain.setDesiredOutput(1, 0f);
				brain.feedForward();
				brain.backPropagate();
			}
		}
	}

	public void ask(float inOne, float inTwo) {
		brain.setInput(0, inOne);
		brain.setInput(1, inTwo);
		brain.feedForward();
		//p.println(id + ": " + "output 0   " + brain.getOutput(0));
		//p.println(id + ": " + "output 1   " + brain.getOutput(1));
	}

	/*
	 * void teach(float getIn, float getOut, int i) { for (int i = 0;
	 * i<repeat_teaching; i++) { brain.setInput(i, getIn); //recebe input
	 * brain.setOutput(i.getOut);// e output brain.feedForward();
	 * brain.backPropagate(); // e "ensina" } }
	 */
	public void teach(float in, int i) {
		brain.setDesiredOutput(i, in);
		brain.feedForward();
		brain.backPropagate();
	}

	// //////////////////////////////////////////////////////////////////////////////////////

	public void die() {
		/*
		 * if(life<0){ castRemoveMe(); }
		 */
	}

	// TODO: criar métodos para que criatures andem em conjunto com seus
	// familiares, quando novos.

	private boolean relativeness(ArrayList<Creature> creatures, int number,
			int from, int to) {
		// analiza um conjunto de genes e retorna verdadeiro se dada porcentagem
		// for positiva.

		int count = 0;
		for (Creature other : creatures) {
			DNA mate = other.dna;
			if (other.id != id) {
				count = 0;
				for (int i = from; i < to; i++) {

					if ((mate.getGene(i) == dna.getGene(i))) {
						// println("the genes: " + i + ", of creatures " +
						// other.id + " and " + id + " are equal");
						count++;
					}
				}
			}
		}
		// println("count: " + count);
		if (count >= number) {
			return true;
		} else {
			return false;
		}
	}

	// /////////////////////////////
	// /////////////////////

	DNA getDna() {
		return dna;
	}

	void setDna(DNA d) {
		dna = d;
	}

	Body getBody() {
		return bodyCopy;
	}

	int getId() {
		return id;
	}
}
