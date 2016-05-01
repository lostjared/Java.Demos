package ssgame;

public abstract class Character implements CharacterActions {

	public Character(int x, int y) {
		this.x = x;
		this.y = y;
		frame = 0;
	}
	
	public void setLevelSize(int lx, int ly, int lw, int lh) {
		level_x = lx;
		level_y = ly;
		level_w = lw;
		level_h = lh;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public void moveLeft() {
		if(x > 0) --x;
	}
	
	public void moveRight() {
		if(x < level_w-1) ++x;
	}
	
	
	public int x,y,frame;
	protected int level_x = 0, level_y = 0, level_w = 0, level_h = 0;
	protected boolean jumping = false;
	protected boolean attacking = false;
}
