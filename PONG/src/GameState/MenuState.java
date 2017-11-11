package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import TileMap.Background;

public class MenuState extends GameState {

	private Background bg;
	
	private String[] options = {"Links", "Rechts","Quit"};
	
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg=new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			titleFont = new Font("Arial", Font.PLAIN, 12);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		//draw bg
		bg.update();
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("SMASH", 140, 70);
		
		try {
			g.drawString(InetAddress.getLocalHost().getHostAddress(), 140, 110);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//draw menu
		g.setFont(font);
		for(int i = 0; i<options.length; i++) {
			if(i== currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140+i*15);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LINKS);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.RECHTS);
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
