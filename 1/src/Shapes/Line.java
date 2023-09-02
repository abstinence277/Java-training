package Shapes;

import java.awt.Graphics;

public class Line extends Shape {

	private int x1,x2,y1,y2;
	
	public Line(int x1,int y1,int x2,int y2) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	public Line(Line p) {
		this.x1=p.getX1();
		this.x2=p.getX2();
		this.y1=p.getY1();
		this.y2=p.getY2();
	}
	protected int getY1() {
		return y1;
	}
	protected int getX1() {
		return x1;
	}
	protected int getY2() {
		return y1;
	}
	protected int getX2() {
		return x1;
	}
	@Override
	public void draw(Graphics g) {
		g.drawLine(x1, y1, x2, y2);
	}
}
