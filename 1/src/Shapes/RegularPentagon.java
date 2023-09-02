package Shapes;

import java.awt.Color;
import java.awt.Graphics;

public class RegularPentagon extends Polygon{
	private int x,y,r;
	private final double PI=Math.PI;
	private static int x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
	public RegularPentagon(int x,int y, int r)
	{
		this.x=x;
		this.y=y;
		this.r=r;
		 x0 = (int) (x - r * Math.sin(PI / 5));
		 y0 = (int) (y + r * Math.cos(PI / 5));
		 x1 = x;
		 y1 = y - r;
		 x2 = (int) (x + r * Math.sin(PI / 5));
		 y2 = y0;
		 x3 = (int) (x0 - 2 * r * Math.sin(PI / 5) * Math.sin(PI / 10));
		 y3 = (int) (y0 - 2 * r * Math.sin(PI / 5) * Math.cos(PI / 10));
		 x4 = (int) (x2 + 2 * r * Math.sin(PI / 5) * Math.sin(PI / 10));
		 y4 = y3;
	}
	public RegularPentagon(RegularPentagon p) {
		this.x=p.getX();
		this.y=p.getY();
		this.r=p.getR();
	}
    private int getR() {
		return r;
	}
	private int getY() {
		return y;
	}
	private int getX() {
		return x;
	}
    @Override
    public void draw(Graphics g) 
    {
    	g.setColor(new Color(70,130,180));
    	int x[]= {x0,x2,x4,x1,x3};
    	int y[]= {y0,y2,y4,y1,y3};
    	g.fillPolygon(x, y, 5);
    	g.setColor(Color.blue);
        g.drawPolygon(x, y, 5);
}
}
