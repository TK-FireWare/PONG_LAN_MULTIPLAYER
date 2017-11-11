package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GameStateManager {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	
	public static String IP;
	
	public static final int MENUSTATE = 0;
	public static final int LINKS = 1;
	public static final int RECHTS = 2;
	
	public GameStateManager() {
		
		gameStates = new ArrayList<GameState>();
		
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LINKS(this));

		gameStates.add(new RECHTS(this));
}


	public void setState(int state) {
		if(state == LINKS) {
			IP = JOptionPane.showInputDialog("IP");
			System.out.println("IP: " + IP);
		}
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}

	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}


}
