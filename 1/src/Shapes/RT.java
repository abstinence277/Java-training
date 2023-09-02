package Shapes;

import java.awt.Graphics;

public class RT extends Triangle{
	public RT(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		super(x1,y1,x2,y2,x3,y3);
	}
	public RT(RT p) {
		this.x=p.getX();
		this.y=p.getY();
	}
	@Override
	public void draw(Graphics g) {
		g.drawPolygon(x, y, x.length);
	}
}
