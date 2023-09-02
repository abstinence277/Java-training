package Shapes;

import java.awt.Graphics;

public class Ladder extends Polygon{
	protected int[] x = new int[4];
	protected int[] y = new int[4];
	public Ladder(int x1, int y1, int x2, int y2, int x3, int y3,int x4,int y4)
	{
		x[0] = x1; x[1] = x2; x[2] = x3;x[3]=x4;
		y[0] = y1; y[1] = y2; y[2] = y3;y[3]=y4;
	}
	public Ladder(Ladder p) {
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
		g.drawLine(x[0], y[0], x[1],y[1]);
		g.drawLine(x[1], y[1], x[2],y[2]);
		g.drawLine(x[2], y[2], x[3],y[3]);
		g.drawLine(x[3], y[3], x[0],y[0]);
	}
}
