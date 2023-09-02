package Shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Haxagon extends Polygon{
	private int x,y,r;
	private final double PI=Math.PI;
	private static int x0, y0, x1, y1, x2, y2, x3, y3, x4, y4,x5,y5;
	public Haxagon(int x,int y, int r)
	{
		this.x=x;
		this.y=y;
		this.r=r;
		 x0 = (int) (x - r * Math.sin(PI / 6));
		 y0 = (int) (y - r * Math.cos(PI / 6));
		 x1 = (int) (x + r * Math.sin(PI / 6));
		 y1 = y0;
		 x2 = x-r;
		 y2 = y ;
		 x3 = x+r;
		 y3=y;
		 x4=x0;
		 y4=(int) (y + r * Math.cos(PI / 6));
		 x5 = x1;
		 y5 = y4;
	}
	public Haxagon(Haxagon p) {
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
    	int x[]= {x0,x1,x3,x5,x4,x2,x0};
    	int y[]= {y0,y1,y3,y5,y4,y2,y0};
    	g.fillPolygon(x, y, 6);
    	g.setColor(Color.blue);
        g.drawPolygon(x, y, 6);
    }
}
