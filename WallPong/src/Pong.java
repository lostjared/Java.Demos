
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class GameObject {
	int x,y,w,h;
	public void set(int xx, int yy, int ww, int hh) {
		x = xx;
		y = yy;
		w = ww;
		h = hh;
	}
}

class Ball extends GameObject {
	public int dir = 1;
	
	public void update() {
		
	}
}

class Paddle extends GameObject {

	public Paddle() {
		w = 20;
	}
	
}

class Game {
	
	public Ball game_ball;
	public Paddle player1, player2;
	boolean key1 = false, key2 = false;
	
	public Game() {
		game_ball = new Ball();
		player1 = new Paddle();
		player2 = new Paddle();		
		player2.set(640-20, 0, 20, 480);
		player1.set(10, (480/2), 20, 100);
		game_ball.set(640/2, 480/2, 25, 25);
	}
	
	public void draw(Graphics g) {
		
		g.setColor(new Color(255,255,255));;
		g.fillRect(player2.x, player2.y, player2.w, player2.h);
		g.fillOval(game_ball.x, game_ball.y, game_ball.w, game_ball.h);
		g.fillRect(player1.x,  player1.y,  player1.w,  player1.h);
	}
	
	public void moveUp() {
		if(player1.y > 10) player1.y -= 5;
	
	}
	
	public void moveDown() {
		if(player1.y+player1.h < 480) {
			player1.y += 5;
		}
	}
	
	public void setkeys(boolean one, boolean two) {
		key1 = one;
		key2 = two;
		
	}
	
	public void procGame(int speed) {
		
		if(key1 == true) {
			moveUp();
		} else if(key2 == true) {
			moveDown();
		}
		
		Random rand = new Random();
		Paddle one = player1;
		Paddle two = player2;
		
	    if(game_ball.dir == 1 && game_ball.x > 5 && game_ball.y > 17) {
	        
	        if(game_ball.x == one.x + 10 && game_ball.y >= one.y && game_ball.y <= one.y + 100)
	            game_ball.dir = rand.nextInt(2) + 3;
	        else {
	            game_ball.x -= speed;
	            game_ball.y -= speed;
	        }
	    }
	    else if(game_ball.dir == 2 && game_ball.x > 5 && game_ball.y < 480) {
	        
	        if(game_ball.x == one.x + 10 && game_ball.y >= one.y && game_ball.y <= one.y + 100)
	            game_ball.dir = rand.nextInt(2) + 3;
	        else {
	            game_ball.x -= speed;
	            game_ball.y += speed;
	        }
	    }
	    else if(game_ball.dir == 3 && game_ball.x < 640 && game_ball.y > 17) {
	        
	        if(game_ball.x > 620)
	            game_ball.dir = rand.nextInt(2) + 1;
	        else {
	            game_ball.x += speed;
	            game_ball.y -= speed;
	        }
	    }
	    else if(game_ball.dir == 4 && game_ball.x < 640 && game_ball.y < 480) {
	        
	        if(game_ball.x > 620)
	            game_ball.dir = rand.nextInt(2) + 1;
	        else {
	            game_ball.x += speed;
	            game_ball.y += speed;
	        }
	    }
	    else {
	        if(game_ball.dir == 1 || game_ball.dir == 3) game_ball.dir++;
	        else if(game_ball.dir == 2 || game_ball.dir == 4) game_ball.dir--;
	    }
	    
	    if(game_ball.x < 6) {
	        game_ball.x = 315;
	        game_ball.y = 240;
	        game_ball.dir = 1+(rand.nextInt(4));
	        //two.score();
	    }
	}
	
	public void proc() {
		procGame(5);
	}
}


public class Pong extends Frame implements KeyListener, Runnable {

	private Image back_buffer = null;
	private Graphics graphics;
	private Game game;
	private Thread thread1;
	
	public Pong() {
		
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		game = new Game();
		super.setSize(640, 480);
		super.setTitle("Pong");
		super.setVisible(true);
		addKeyListener(this);
		
		thread1 = new Thread(this);
		thread1.start();
	}
	
	
	
	
	public void run() {
		while(true) {
			try {
				game.proc();
				repaint();
				Thread.sleep(10);
			} 
			catch(InterruptedException e) {
				
			}
		}
	}
	
	
    public void keyReleased(KeyEvent ke) {
    	game.setkeys(false, false);
    }

    public void keyPressed(KeyEvent ke) {
    		
    	int key = ke.getKeyCode();
    	if(key == KeyEvent.VK_UP) {
    		game.setkeys(true,  false);;
    	}
    	else if(key == KeyEvent.VK_DOWN) {
    		game.setkeys(false, true);
    	}
    }

    public void keyTyped(KeyEvent ke) {
            
    }
	
	@Override public void update(Graphics g) {
		paint(g);
	}
	
	@Override public void paint(Graphics g) {
		if(back_buffer == null) {
			back_buffer = createImage(640, 480);
			graphics = back_buffer.getGraphics();
		}
		graphics.setColor( new Color(0,0,0));
		graphics.fillRect(0, 0, 640, 480);
		game.draw(graphics);
		g.drawImage(back_buffer, 0, 0, this);
	}
	
	public static void main(String[] args) {
		Pong pong = new Pong();	
	}
	
}
