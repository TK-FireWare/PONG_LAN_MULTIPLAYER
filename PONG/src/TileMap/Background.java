package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {
	
	BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void setPosition(double x, double y) {
		this.x = x* moveScale;
		this.y = y* moveScale;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x+=dx;
		y+=dy;
		
		if(x<0-image.getWidth()) {
			x = 0;
		}
		if(x>0+image.getWidth()) {
			x = image.getWidth();
		}
		if(y<0-image.getWidth()) {
			y = 0;
		}
		if(y>0+image.getWidth()) {
			y = image.getWidth();
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int) x, (int)y, null);
		if(x<0) {
			g.drawImage(image, (int)x+image.getWidth(), (int)y, null);
			
		}
		if(x>0) {
			g.drawImage(image, (int)x-image.getWidth(), (int)y, null);
			
		}
		
	}
}
























