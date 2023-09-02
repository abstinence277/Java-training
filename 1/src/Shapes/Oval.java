package Shapes;

import java.awt.Graphics;

public class Oval extends Shape{
	protected int x,y,radius1,radius2;
	public Oval() {}
	public Oval(int x, int y, int radius1,int radius2)
	{
		this.x = x;
		this.y = y;
		this.radius1 = radius1;
		this.radius2=radius2;
	}
	public Oval(Oval p) {
		this.x=p.getX();
		this.y=p.getY();
		this.radius1=getR1();
		this.radius2=getR2();
	}
	private int getR2() {	
		return radius2;
	}
	private int getR1() {
		return radius1;
	}
	private int getY() {
		return y;
	}
	private int getX() {
		return x;
	}
	@Override
	public void draw(Graphics g) {
		g.drawOval(x-radius1, y-radius2, radius1*2, radius2*2);
	}
}
