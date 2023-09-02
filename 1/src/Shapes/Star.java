package Shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Star extends Polygon{
	private int x,y,r;
	private final double PI=Math.PI;
	private static int x0, y0, x1, y1, x2, y2, x3, y3, x4, y4,x5,y5,x6,y6,x7,y7,x8,y8,x9,y9;
	public Star(int x,int y, int r)
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
		 x5=(int) (x1-(x1 - x0) /3);
		 x6=(int) (x1+(x2 - x1) /3);
		 x7=(int) (x2-(x2 - x1) /3);
		 x8=x;
		 x9=(int) (x0+(x1 - x0) /3);
		 y5=y3;y6=y3;
		 y7=(int) (y2-(y2 - y1) /3);
		 y9=y7;
		 y8=(int) (y0-(y0 - y4) /3);
	}
	public Star(Star p) {
		this.x=p.getX();
		this.y=p.getY();
		this.r=p.getR();
	}
    public int getR() {
		return r;
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
	}
	@Override
    public void draw(Graphics g) 
    {
    g.setColor(new Color(70,130,180));
    int x[]= {x5,x1,x6,x4,x7,x2,x8,x0,x9,x3};
	int y[]= {y5,y1,y6,y4,y7,y2,y8,y0,y9,y3};
    g.fillPolygon(x, y, 10);
    g.setColor(Color.blue);
    g.drawPolygon(x, y, 10);
}
}