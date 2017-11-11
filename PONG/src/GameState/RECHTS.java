package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import GameState.LINKS.vec;
import Main.GamePanel;

public class RECHTS extends GameState {
	
	Scanner in = null;
	PrintWriter out = null;
	
	/**holds the size of the ball in pixels*/
	private final int ballSize = 30;
	/**holds the height of the bats*/		
	private final int batHeight=150;
	/**holds the width of the bats*/
	private final int batWidth = 20;
	
	/**enum with possible movement directions of the bat*/
	enum vec{UP, DOWN, STOP;} 
	

	/**holds the movement direction of the left bat*/
	private vec batVec = vec.STOP;
	
	/**X-coordinate of the right bat*/
	private double rightX = GamePanel.WIDTH*2-batWidth;
	/**Y-coordinate of the right bat*/
	private double rightY = GamePanel.HEIGHT/2-batHeight/2;
	/**X-coordinate of the ball*/
	private double ballX = GamePanel.WIDTH-15;
	/**Y-coordinate of the ball*/
	private double ballY = GamePanel.HEIGHT/2-15;
	
	/**String to show the score*/
	private String points = "0:0";
	
	public RECHTS(GameStateManager gsm) {
		
	}
	
	@Override
	public void init() {
		try {
			waitconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void waitconnect() throws IOException {
		ServerSocket server = new ServerSocket(25522);

		Socket client = null;
		while (true) {
			client = null;
			try {
				client = server.accept();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		in = new Scanner(client.getInputStream());
		out = new PrintWriter(client.getOutputStream(), true);
	}

	@Override
	public void update() {
		String s = "";
		try {
			s = in.nextLine();
			out.println(batVec);
		} catch(Exception e) {}
		try {
			String[] st = s.split(";");
			ballX = Integer.valueOf(st[0])-GamePanel.WIDTH;
			ballY = Integer.valueOf(st[1]);
			rightX = Integer.valueOf(st[2])-GamePanel.WIDTH;
			rightY = Integer.valueOf(st[3]);
			points = st[4];
		} catch (Exception e) {}
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect((int)ballX, (int)ballY, ballSize, ballSize);
		g.fillRect((int)rightX, (int)rightY, batWidth, batHeight);
		g.drawString(points, 300, 100);
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_UP) {
				batVec = vec.UP;
		}
		if(k == KeyEvent.VK_DOWN) {	
				batVec = vec.DOWN;
		}
				
	}
	
	
	@Override
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_DOWN || batVec == vec.DOWN) {
			batVec = vec.STOP;
		} 
		if(k == KeyEvent.VK_UP || batVec == vec.UP) {
			batVec = vec.STOP;
		}	
	}
		
}






















