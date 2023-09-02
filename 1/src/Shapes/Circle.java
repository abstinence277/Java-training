package Shapes;

import java.awt.Graphics;

public class Circle extends Oval{
	private int radius;
	public Circle(int x, int y, int radius)
	{
		super(x,y,radius,radius);
	}
	public Circle(Circle p) {
		this.radius=p.getR();
	}
	private int getR() {
		return radius;
	}
	@Override
	public void draw(Graphics g) {
		g.drawOval(x-radius, y-radius, radius*2, radius*2);
	}
}
