package Shapes;

import java.awt.Graphics;

public class Triangle extends Polygon{
	protected  int[] x = new int[3];
	protected  int[] y = new int[3];
	public Triangle() {}
	public Triangle(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		x[0] = x1; x[1] = x2; x[2] = x3;
		y[0] = y1; y[1] = y2; y[2] = y3;
	}
	public Triangle(Triangle p) {
		this.x=p.getX();
		this.y=p.getY();
	}
	protected int[] getY() {
		return y;
	}
	protected int[] getX() {
		return x;
	}
	@Override
	public void draw(Graphics g) {
		g.drawPolygon(x, y, x.length);
	}
}
