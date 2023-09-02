package Shapes;

import java.awt.Graphics;

public class Point extends Shape {

	private int x1,y1;
	
	public Point(int x1,int y1) {
		this.x1=x1;
		this.y1=y1;
	}
	public Point(Point p) {
		this.x1=p.getX();
		this.y1=p.getY();
	}
	protected int getY() {
		return y1;
	}
	protected int getX() {
		return x1;
	}
	@Override
	public void draw(Graphics g) {
		g.drawLine(x1, y1,x1,y1);
	}
}
