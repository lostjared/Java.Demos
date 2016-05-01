package ssgame;

public class Hero extends Character {

	public Hero(int hero_x, int hero_y) {
		super(hero_x, hero_y);
	}
	
	public void jump() {
		jumping = true;
	}
	
	public void attack() {
		attacking = true;
	}
	
}
