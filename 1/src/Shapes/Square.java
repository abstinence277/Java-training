package Shapes;

import java.awt.Graphics;

public class Square extends Rectangle{
	private int a;
	public Square(int x,int y,int a) {
		super(x,y,a,a);
	}
	public Square(Square p) {
		this.x=p.getX();
		this.y=p.getY();
		this.a=p.getA();
	}
	private int getA() {
		return a;
	}
	@Override
	public void draw(Graphics g) {
		g.drawRect(x, y, a, a);
	}
}
