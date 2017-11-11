/**
 * Gamestate for the left Screen. This contains all the Game-Mechanics and the network-communication Interface.
 * @author Lukas Thyroff & Felix Kühn
 *
 */

package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Main.GamePanel;

public class LINKS extends GameState {

	/**holds the size of the ball in pixels*/
	private final int ballSize = 30;
	/**holds the height of the bats*/		
	private final int batHeight=150;
	/**holds the width of the bats*/
	private final int batWidth = 20;
	
	/**Used for interrupt after someone scored*/
	private int flag = -1;
	
	/**IP-Address of RIGHT*/
	private String IP; 
	
	/**Socket to tell RIGHT the Coordinates of things*/
	private Socket socket;
	
	/**X-coordinate of the left bat*/
	private double leftX = 0;
	/**Y-coordinate of the left bat*/
	private double leftY = GamePanel.HEIGHT/2-batHeight/2;
	/**X-coordinate of the right bat*/
	private double rightX = GamePanel.WIDTH*2-batWidth;
	/**Y-coordinate of the right bat*/
	private double rightY = GamePanel.HEIGHT/2-batHeight/2;
	/**X-coordinate of the ball*/
	private double ballX = GamePanel.WIDTH-15;
	/**Y-coordinate of the ball*/
	private double ballY = GamePanel.HEIGHT/2-15;
	
	/**Holds the score of the left player*/
	private int pointLeft=0;	
	/**Holds the score of the right player*/
	private int pointRight=0;
	
	/**seconds to pause after a player scored*/
	private final double waitingSeconds = 1.5;
	
	
	
	/**Socket to connect to RIGHT*/
	private Socket server = null;
	/** sends data to RIGHT*/
	private PrintWriter out = null;
	/**receives data from RIGHT*/
	private Scanner in = null;
	
	/**enum with possible movement directions of the bat*/
	enum vec{UP, DOWN, STOP;} 
	
	/**holds the movement direction of the left bat*/
	private vec leftBat = vec.STOP;
	/**holds the movement direction of the left bat*/
	private vec rightBat = vec.STOP;
	
	/**holds the movement direction of the ball*/
	private double ballVecX = Math.random()*2+0.5, ballVecY = Math.random()*2+0.5;
	
	/**
	 * Constructor of the class LINKS
	 * @param gsm GamestateManager of the game
	 */
	public LINKS(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	@Override
	public void init() {
		System.out.print("hallo");
		try {
            server = new Socket(GameStateManager.IP , 25522);
            out = new PrintWriter(server.getOutputStream(), true);
            in = new Scanner(server.getInputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the positions and vectors of ball and bats every time the gameloop is executed
	 */
	@Override
	public void update() {
		
		// Don't move the ball for x seconds after a player scored
		if(flag >= 0){
			flag++;
			if(flag > waitingSeconds*GamePanel.FPS) {
				flag = -1;
				ballVecX = ((int)((Math.random()*2)))==1 ? -2 : 2;
				ballVecY = Math.random()*2-1;
			}
		}
		
		//move the left bat
		switch(leftBat) {
			case UP:
				if(!(leftY <= 0)) leftY -= 2;//move up if it isnt at the top
				break;
			case DOWN:
				if(!(leftY >= GamePanel.HEIGHT-batHeight)) leftY += 2; // move down, if it isnt at the bottom
				break;
		}
		
		//move the right bat
		switch(rightBat) {
			case UP:
				if(!(rightY <= 0)) rightY -= 2; //move up if it isn't at the top
				break;
			case DOWN:
				if(!(rightY >= GamePanel.HEIGHT-batHeight)) rightY += 2;//move down if it isn't at the top
				break;
		}
		
		//change Yvector of the ball, when it hits the top or bottom of the screen
		if(ballY <= 0) {
			ballVecY= -ballVecY;
		} else if(ballY >= GamePanel.HEIGHT-ballSize) {
			ballVecY= -ballVecY;
		}
				
		//add point and reset the play area if player scores
		if(ballX < -ballSize) {
			pointRight++;
			ballVecX = 0;
			ballVecY = 0;
			ballX = GamePanel.WIDTH-ballSize/2;
			ballY = GamePanel.HEIGHT/2-ballSize/2;
			flag=0;
		} else if(ballX >= GamePanel.WIDTH*2) {
			pointLeft++;
			ballVecY = 0;
			ballX = GamePanel.WIDTH-ballSize/2;
			ballY = GamePanel.HEIGHT/2-ballSize/2;
			ballVecX = 0;
			flag = 0;
		}
		
		//bounce the ball off the left bat
		if(ballX > leftX && ballX < leftX+batWidth) {
			if(ballY + ballSize > leftY && ballY < leftY+batHeight) {
				ballVecX = -ballVecX;
				ballVecX += 0.3;
				switch(leftBat) {
					case UP:
						ballVecY -= 1;						
						break;
					case DOWN:
						ballVecY += 1;
						break;
					case STOP:
						break;
				}
			}
		}
		
		//bounce the ball off the right bat
		if(ballX+ballSize >= rightX && ballX <= rightX+batWidth) {
			if(ballY+ballSize > rightY && ballY < rightY+batHeight) {
				ballVecX = -ballVecX;
				ballVecX -= 0.3;
				switch(rightBat) {
					case UP:
						ballVecY -= 1;
						break;
					case DOWN:
						ballVecY += 1;
						break;
					case STOP:
						break;
				}
			}
		}
		
		
		ballX += ballVecX;//move the balls X-Location by the x-part of its vector
		ballY += ballVecY;//move the balls Y-Location by the y-part of its vector
		
		try {
			out.println((int)ballX + ";" + (int)ballY + ";" + (int)rightX + ";" + (int)rightY + ";" + pointLeft+":"+ pointRight);
			
			String s = in.nextLine();
			
			rightBat = s.equals("UP") ? vec.UP : s.equals("DOWN") ? vec.DOWN : vec.STOP;
		} catch(Exception e) {}
	}
	
	
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect((int)leftX, (int)leftY, batWidth, batHeight);
		g.fillRect((int)ballX, (int)ballY, ballSize, ballSize);
		g.drawString(pointLeft +" : " + pointRight, 300, 100);
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_DOWN) {
			leftBat = vec.DOWN;
		} 
		if(k == KeyEvent.VK_UP) {
			leftBat = vec.UP;
		}		
	}

	@Override
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_DOWN || leftBat == vec.DOWN) {
			leftBat = vec.STOP;
		} 
		if(k == KeyEvent.VK_UP || leftBat == vec.UP) {
			leftBat = vec.STOP;
		}	
	}

}
