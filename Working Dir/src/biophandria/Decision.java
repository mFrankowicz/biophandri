package biophandria;

//classe para guardar decisões das criaturas, facilita as decisões em coletivo.
//assim cada criatura pode guardar um array de desisões de seus vizinhos.
public class Decision {

	// Decisões:
	/*
	 * - mover-se - descansar - comer - fugir - perseguir - atacar - copular -
	 * defecar - brincar
	 */

	private boolean move;

	private boolean moveTogheter;

	private boolean rest; // diferente de parar!

	private boolean eat;

	private boolean escape;

	private boolean folow;

	private boolean attack;

	private boolean copulate;

	private boolean defecate;

	private boolean play;
	
	
	public Decision(boolean move, boolean moveTogheter, boolean rest,
			boolean eat, boolean escape, boolean folow, boolean attack,
			boolean copulate, boolean defecate, boolean play) {
		this.move = move;
		this.moveTogheter = moveTogheter;
		this.rest = rest;
		this.eat = eat;
		this.escape = escape;
		this.folow = folow;
		this.attack = attack;
		this.copulate = copulate;
		this.defecate = defecate;
		this.play = play;
	}
	
	public Decision(boolean[] decisions) {
		this.move = decisions[0];
		this.moveTogheter = decisions[1];
		this.rest = decisions[2];
		this.eat = decisions[3];
		this.escape = decisions[4];
		this.folow = decisions[5];
		this.attack = decisions[6];
		this.copulate = decisions[7];
		this.defecate = decisions[8];
		this.play = decisions[9];
	}

	public Decision() {

		move = false;
		moveTogheter = false;
		rest = false;
		eat = false;
		escape = false;
		folow = false;
		attack = false;
		copulate = false;
		defecate = false;
		play = false;

	}
	
	
	//GETTERS

	public boolean isMoving() {
		return move;
	}

	public boolean isMovingTogheter() {
		return moveTogheter;
	}

	public boolean isResting() {
		return rest;
	}

	public boolean isEating() {
		return eat;
	}

	public boolean isEscaping() {
		return escape;
	}

	public boolean isFolowing() {
		return folow;
	}

	public boolean isAttacking() {
		return attack;
	}

	public boolean isCopulating() {
		return copulate;
	}

	public boolean isDefecating() {
		return defecate;
	}

	public boolean isPlaying() {
		return play;
	}
	
	//SETTERS
	
	public void setMove(boolean move) {
		this.move = move;
	}

	public void setMoveTogheter(boolean moveTogheter) {
		this.moveTogheter = moveTogheter;
	}

	public void setRest(boolean rest) {
		this.rest = rest;
	}

	public void setEat(boolean eat) {
		this.eat = eat;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public void setFolow(boolean folow) {
		this.folow = folow;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public void setCopulate(boolean copulate) {
		this.copulate = copulate;
	}

	public void setDefecate(boolean defecate) {
		this.defecate = defecate;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}
	
	
}
