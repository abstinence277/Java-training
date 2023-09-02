package Shapes;

import java.awt.Graphics;

public class Rectangle extends Shape{
	protected int x,y,width,height;
	public Rectangle() {}
	public Rectangle(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rectangle(Rectangle p) {
		this.x=p.getX();
		this.y=p.getY();
	}
	protected int getY() {
		return y;
	}
	protected int getX() {
		return x;
	}
	@Override
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
}
