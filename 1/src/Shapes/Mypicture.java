package Shapes;

public class Mypicture {
	public static void main(String[] args) 
	{
		Picture pic = new Picture(420,300);
		//实例化1
		Star s=new Star(100,200,100);
		RegularPentagon r=new RegularPentagon(400,200,100);
		Haxagon h=new Haxagon(600,200,100);
		//实例化2
		Star s1=new Star(s);
		RegularPentagon r1=new RegularPentagon(r);
		Haxagon h1=new Haxagon(h);
		//
		pic.add(s1);
		pic.add(r1);
		pic.add(h1);
		pic.draw();	
	}
}
